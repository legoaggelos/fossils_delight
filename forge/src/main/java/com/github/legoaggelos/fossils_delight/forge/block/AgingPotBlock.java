package com.github.legoaggelos.fossils_delight.forge.block;

import com.github.legoaggelos.fossils_delight.forge.block.entity.AgingPotBlockEntity;
import com.github.legoaggelos.fossils_delight.forge.registry.BlockEntityRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;
import com.github.legoaggelos.fossils_delight.forge.block.entity.state.*;
import vectorwing.farmersdelight.common.registry.ModSounds;
import vectorwing.farmersdelight.common.tag.ModTags;
import vectorwing.farmersdelight.common.utility.MathUtils;
import vectorwing.farmersdelight.common.utility.TextUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

@SuppressWarnings("deprecation")
public class AgingPotBlock extends BaseEntityBlock implements SimpleWaterloggedBlock
{
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final EnumProperty<AgingPotSupport> SUPPORT = EnumProperty.create("support", AgingPotSupport.class);
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 10.0D, 14.0D);
	protected static final VoxelShape SHAPE_WITH_TRAY = Shapes.or(SHAPE, Block.box(0.0D, -1.0D, 0.0D, 16.0D, 0.0D, 16.0D));

	public AgingPotBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(SUPPORT, AgingPotSupport.NONE).setValue(WATERLOGGED, false));
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
								 InteractionHand hand, BlockHitResult result) {
		ItemStack heldStack = player.getItemInHand(hand);
		if (heldStack.isEmpty() && player.isShiftKeyDown()) {
			level.setBlockAndUpdate(pos, state.setValue(SUPPORT, state.getValue(SUPPORT).equals(AgingPotSupport.HANDLE)
					? getTrayState(level, pos) : AgingPotSupport.HANDLE));
			level.playSound(null, pos, SoundEvents.LANTERN_PLACE, SoundSource.BLOCKS, 0.7F, 1.0F);
		} else if (!level.isClientSide) {
			BlockEntity tileEntity = level.getBlockEntity(pos);
			if (tileEntity instanceof AgingPotBlockEntity agingPotEntity) {
				ItemStack servingStack = agingPotEntity.useHeldItemOnMeal(heldStack);
				if (servingStack != ItemStack.EMPTY) {
					if (!player.getInventory().add(servingStack)) {
						player.drop(servingStack, false);
					}
					level.playSound(null, pos, SoundEvents.ARMOR_EQUIP_GENERIC, SoundSource.BLOCKS, 1.0F, 1.0F);
				} else {
					NetworkHooks.openGui((ServerPlayer) player, agingPotEntity, pos);
				}
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	public RenderShape getRenderShape(BlockState pState) {
		return RenderShape.MODEL;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return state.getValue(SUPPORT).equals(AgingPotSupport.TRAY) ? SHAPE_WITH_TRAY : SHAPE;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockPos pos = context.getClickedPos();
		Level level = context.getLevel();
		FluidState fluid = level.getFluidState(context.getClickedPos());

		BlockState state = this.defaultBlockState()
				.setValue(FACING, context.getHorizontalDirection().getOpposite())
				.setValue(WATERLOGGED, fluid.getType() == Fluids.WATER);

		if (context.getClickedFace().equals(Direction.DOWN)) {
			return state.setValue(SUPPORT, AgingPotSupport.HANDLE);
		}
		return state.setValue(SUPPORT, getTrayState(level, pos));
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
		if (state.getValue(WATERLOGGED)) {
			level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		}
		if (facing.getAxis().equals(Direction.Axis.Y) && !state.getValue(SUPPORT).equals(AgingPotSupport.HANDLE)) {
			return state.setValue(SUPPORT, getTrayState(level, currentPos));
		}
		return state;
	}

	private AgingPotSupport getTrayState(LevelAccessor level, BlockPos pos) {
		if (level.getBlockState(pos.below()).is(ModTags.TRAY_HEAT_SOURCES)) {
			return AgingPotSupport.TRAY;
		}
		return AgingPotSupport.NONE;
	}

	@Override
	public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
		ItemStack stack = super.getCloneItemStack(level, pos, state);
		AgingPotBlockEntity agingPotEntity = (AgingPotBlockEntity) level.getBlockEntity(pos);
		if (agingPotEntity != null) {
			CompoundTag nbt = agingPotEntity.writeMeal(new CompoundTag());
			if (!nbt.isEmpty()) {
				stack.addTagElement("BlockEntityTag", nbt);
			}
			if (agingPotEntity.hasCustomName()) {
				stack.setHoverName(agingPotEntity.getCustomName());
			}
		}
		return stack;
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			BlockEntity tileEntity = level.getBlockEntity(pos);
			if (tileEntity instanceof AgingPotBlockEntity agingPotEntity) {
				Containers.dropContents(level, pos, agingPotEntity.getDroppableInventory());
				agingPotEntity.getUsedRecipesAndPopExperience(level, Vec3.atCenterOf(pos));
				level.updateNeighbourForOutputSignal(pos, this);
			}

			super.onRemove(state, level, pos, newState, isMoving);
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, level, tooltip, flagIn);
		CompoundTag nbt = stack.getTagElement("BlockEntityTag");
		ItemStack mealStack = AgingPotBlockEntity.getMealFromItem(stack);

		if (!mealStack.isEmpty()) {
			MutableComponent textServingsOf = mealStack.getCount() == 1
					? new TranslatableComponent("fossils_delight.tooltip.aging_pot.single_serving")
					: new TranslatableComponent("fossils_delight.tooltip.aging_pot.many_servings", mealStack.getCount());
			tooltip.add(textServingsOf.withStyle(ChatFormatting.GRAY));
			MutableComponent textMealName = mealStack.getHoverName().copy();
			tooltip.add(textMealName.withStyle(mealStack.getRarity().color));
		} else {
			MutableComponent textEmpty = new TranslatableComponent("fossils_delight.tooltip.aging_pot.empty");
			tooltip.add(textEmpty.withStyle(ChatFormatting.GRAY));
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING, SUPPORT, WATERLOGGED);
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		if (stack.hasCustomHoverName()) {
			BlockEntity tileEntity = level.getBlockEntity(pos);
			if (tileEntity instanceof AgingPotBlockEntity) {
				((AgingPotBlockEntity) tileEntity).setCustomName(stack.getHoverName());
			}
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, Level level, BlockPos pos, Random rand) {
		BlockEntity tileEntity = level.getBlockEntity(pos);
		if (tileEntity instanceof AgingPotBlockEntity agingPotEntity && agingPotEntity.isHeated()) {
			SoundEvent boilSound = !agingPotEntity.getMeal().isEmpty()
					? ModSounds.BLOCK_COOKING_POT_BOIL_SOUP.get()
					: ModSounds.BLOCK_COOKING_POT_BOIL.get();
			double x = (double) pos.getX() + 0.5D;
			double y = pos.getY();
			double z = (double) pos.getZ() + 0.5D;
			if (rand.nextInt(10) == 0) {
				level.playLocalSound(x, y, z, boilSound, SoundSource.BLOCKS, 0.5F, rand.nextFloat() * 0.2F + 0.9F, false);
			}
		}
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos) {
		BlockEntity tileEntity = level.getBlockEntity(pos);
		if (tileEntity instanceof AgingPotBlockEntity) {
			ItemStackHandler inventory = ((AgingPotBlockEntity) tileEntity).getInventory();
			return MathUtils.calcRedstoneFromItemHandler(inventory);
		}
		return 0;
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return BlockEntityRegistry.AGING_POT.get().create(pos, state);
	}

	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntity) {
		if (level.isClientSide) {
			return createTickerHelper(blockEntity, BlockEntityRegistry.AGING_POT.get(), AgingPotBlockEntity::animationTick);
		}
		return createTickerHelper(blockEntity, BlockEntityRegistry.AGING_POT.get(), AgingPotBlockEntity::cookingTick);
	}
}
