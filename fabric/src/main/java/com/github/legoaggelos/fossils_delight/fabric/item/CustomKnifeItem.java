package com.github.legoaggelos.fossils_delight.fabric.item;

import com.nhoryzon.mc.farmersdelight.item.ModItemSettings;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Tier;

import com.nhoryzon.mc.farmersdelight.registry.EnchantmentsRegistry;
import com.nhoryzon.mc.farmersdelight.registry.TagsRegistry;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CarvedPumpkinBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

public class CustomKnifeItem extends DiggerItem {
    public static final Set<Enchantment> ALLOWED_ENCHANTMENTS;
    private static final Set<Material> EFFECTIVE_ON_MATERIAL;

    public CustomKnifeItem(Tier material, Properties properties) {
        super(0.5F, -1.8F, material, TagsRegistry.KNIVES_CUTTABLE, properties);
    }

    public float getDestroySpeed(ItemStack stack, BlockState state) {
        Material material = state.getMaterial();
        return !state.is(TagsRegistry.KNIVES_CUTTABLE) && !EFFECTIVE_ON_MATERIAL.contains(material) ? super.getDestroySpeed(stack, state) : this.speed;
    }

    public boolean canAttackBlock(BlockState state, Level world, BlockPos pos, Player miner) {
        return !miner.isCreative();
    }

    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, (user) -> user.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        return true;
    }

    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        ItemStack tool = context.getItemInHand();
        BlockPos pos = context.getClickedPos();
        BlockState state = world.getBlockState(pos);
        Direction facing = context.getClickedFace();
        if (state.getBlock() == Blocks.PUMPKIN && tool.is(TagsRegistry.KNIVES)) {
            Player player = context.getPlayer();
            if (player != null && !world.isClientSide()) {
                Direction direction = facing.getAxis() == Axis.Y ? player.getDirection().getOpposite() : facing;
                world.playSound((Player)null, pos, SoundEvents.PUMPKIN_CARVE, SoundSource.BLOCKS, 1.0F, 1.0F);
                world.setBlock(pos, (BlockState)Blocks.CARVED_PUMPKIN.defaultBlockState().setValue(CarvedPumpkinBlock.FACING, direction), 11);
                ItemEntity itemEntity = new ItemEntity(world, (double)pos.getX() + (double)0.5F + (double)direction.getStepX() * 0.65, (double)pos.getY() + 0.1, (double)pos.getZ() + (double)0.5F + (double)direction.getStepZ() * 0.65, new ItemStack(Items.PUMPKIN_SEEDS, 4));
                itemEntity.setDeltaMovement(0.05 * (double)direction.getStepX() + world.getRandom().nextDouble() * 0.02, 0.05, 0.05 * (double)direction.getStepZ() + world.getRandom().nextDouble() * 0.02);
                world.addFreshEntity(itemEntity);
                tool.hurtAndBreak(1, player, (playerIn) -> playerIn.broadcastBreakEvent(context.getHand()));
            }

            return InteractionResult.sidedSuccess(world.isClientSide());
        } else {
            return InteractionResult.PASS;
        }
    }

    static {
        ALLOWED_ENCHANTMENTS = Set.of(Enchantments.SHARPNESS, Enchantments.SMITE, Enchantments.BANE_OF_ARTHROPODS, Enchantments.KNOCKBACK, Enchantments.FIRE_ASPECT, Enchantments.MOB_LOOTING, Enchantments.MENDING, EnchantmentsRegistry.BACKSTABBING.get());
        EFFECTIVE_ON_MATERIAL = Set.of(Material.WOOL, Material.CLOTH_DECORATION, Material.CAKE, Material.WEB);
    }
}
