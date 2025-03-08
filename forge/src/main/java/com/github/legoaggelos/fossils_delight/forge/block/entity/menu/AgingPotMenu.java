package com.github.legoaggelos.fossils_delight.forge.block.entity.menu;

import com.github.legoaggelos.fossils_delight.forge.FossilsDelightForge;
import com.github.legoaggelos.fossils_delight.forge.registry.BlockRegistry;
import com.github.legoaggelos.fossils_delight.forge.registry.MenuRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import vectorwing.farmersdelight.FarmersDelight;
import com.github.legoaggelos.fossils_delight.forge.block.entity.AgingPotBlockEntity;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.Objects;

public class AgingPotMenu extends RecipeBookMenu<RecipeWrapper>
{
	public static final ResourceLocation EMPTY_CONTAINER_SLOT_BOWL = new ResourceLocation(FarmersDelight.MODID, "item/empty_container_slot_bowl");

	public final AgingPotBlockEntity blockEntity;
	public final ItemStackHandler inventory;
	private final ContainerData agingPotData;
	private final ContainerLevelAccess canInteractWithCallable;
	protected final Level level;

	public AgingPotMenu(final int windowId, final Inventory playerInventory, final AgingPotBlockEntity blockEntity, ContainerData agingPotDataIn) {
		super(MenuRegistry.AGING_POT.get(), windowId);
		this.blockEntity = blockEntity;
		this.inventory = blockEntity.getInventory();
		this.agingPotData = agingPotDataIn;
		this.level = playerInventory.player.level;
		this.canInteractWithCallable = ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos());

		// Ingredient Slots - 2 Rows x 3 Columns
		int startX = 8;
		int startY = 18;
		int inputStartX = 12;
		int inputStartY = 9;
		int borderSlotSize = 18;
		for (int row = 0; row < 3; ++row) {
			for (int column = 0; column < 4; ++column) {
				this.addSlot(new SlotItemHandler(inventory, (row * 4) + column,
						inputStartX + (column * borderSlotSize),
						inputStartY + (row * borderSlotSize)));
			}
		}

		// Meal Display
		this.addSlot(new AgingPotMealSlot(inventory, 12, 124, 26));

		// Bowl Input
		this.addSlot(new SlotItemHandler(inventory, 13, 92, 55)
		{
			@OnlyIn(Dist.CLIENT)
			public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
				return Pair.of(InventoryMenu.BLOCK_ATLAS, EMPTY_CONTAINER_SLOT_BOWL);
			}
		});

		// Bowl Output
		this.addSlot(new AgingPotResultSlot(playerInventory.player, blockEntity, inventory, 14, 124, 55));

		// Main Player Inventory
		int startPlayerInvY = startY * 4 + 12;
		for (int row = 0; row < 3; ++row) {
			for (int column = 0; column < 9; ++column) {
				this.addSlot(new Slot(playerInventory, 9 + (row * 9) + column, startX + (column * borderSlotSize),
						startPlayerInvY + (row * borderSlotSize)));
			}
		}

		// Hotbar
		for (int column = 0; column < 9; ++column) {
			this.addSlot(new Slot(playerInventory, column, startX + (column * borderSlotSize), 142));
		}

		this.addDataSlots(agingPotDataIn);
	}

	private static AgingPotBlockEntity getTileEntity(final Inventory playerInventory, final FriendlyByteBuf data) {
		Objects.requireNonNull(playerInventory, "playerInventory cannot be null");
		Objects.requireNonNull(data, "data cannot be null");
		final BlockEntity tileAtPos = playerInventory.player.level.getBlockEntity(data.readBlockPos());
		if (tileAtPos instanceof AgingPotBlockEntity) {
			return (AgingPotBlockEntity) tileAtPos;
		}
		throw new IllegalStateException("Tile entity is not correct! " + tileAtPos);
	}

	public AgingPotMenu(final int windowId, final Inventory playerInventory, final FriendlyByteBuf data) {
		this(windowId, playerInventory, getTileEntity(playerInventory, data), new SimpleContainerData(4));
	}

	@Override
	public boolean stillValid(Player playerIn) {
		return stillValid(canInteractWithCallable, playerIn, BlockRegistry.AGING_POT.get());
	}

	@Override
	public ItemStack quickMoveStack(Player playerIn, int index) {
		int indexMealDisplay = 12;
		int indexContainerInput = 13;
		int indexOutput = 14;
		int startPlayerInv = indexOutput + 1;
		int endPlayerInv = startPlayerInv + 36;
		ItemStack slotStackCopy = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot.hasItem()) {
			ItemStack slotStack = slot.getItem();
			slotStackCopy = slotStack.copy();
			if (index == indexOutput) {
				if (!this.moveItemStackTo(slotStack, startPlayerInv, endPlayerInv, true)) {
					return ItemStack.EMPTY;
				}
			} else if (index > indexOutput) {
				boolean isValidContainer = slotStack.is(ModTags.SERVING_CONTAINERS) || slotStack.is(blockEntity.getContainer().getItem());
				if (isValidContainer && !this.moveItemStackTo(slotStack, indexContainerInput, indexContainerInput + 1, false)) {
					return ItemStack.EMPTY;
				} else if (!this.moveItemStackTo(slotStack, 0, indexMealDisplay, false)) {
					return ItemStack.EMPTY;
				} else if (!this.moveItemStackTo(slotStack, indexContainerInput, indexOutput, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(slotStack, startPlayerInv, endPlayerInv, false)) {
				return ItemStack.EMPTY;
			}

			if (slotStack.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}

			if (slotStack.getCount() == slotStackCopy.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(playerIn, slotStack);
		}
		return slotStackCopy;
	}

	@OnlyIn(Dist.CLIENT)
	public int getCookProgressionScaled() {
		int i = this.agingPotData.get(0);
		int j = this.agingPotData.get(1);
		return j != 0 && i != 0 ? i * 24 / j : 0;
	}

	@OnlyIn(Dist.CLIENT)
	public boolean isHeated() {
		return blockEntity.isHeated();
	}

	@Override
	public void fillCraftSlotsStackedContents(StackedContents helper) {
		for (int i = 0; i < inventory.getSlots(); i++) {
			helper.accountSimpleStack(inventory.getStackInSlot(i));
		}
	}

	@Override
	public void clearCraftingContent() {
		for (int i = 0; i < 11; i++) {
			this.inventory.setStackInSlot(i, ItemStack.EMPTY);
		}
	}

	@Override
	public boolean recipeMatches(Recipe<? super RecipeWrapper> recipe) {
		return recipe.matches(new RecipeWrapper(inventory), level);
	}

	@Override
	public int getResultSlotIndex() {
		return 13;
	}

	@Override
	public int getGridWidth() {
		return 3;
	}

	@Override
	public int getGridHeight() {
		return 2;
	}

	@Override
	public int getSize() {
		return 13;
	}

	@Override
	public RecipeBookType getRecipeBookType() {
		return FossilsDelightForge.RECIPE_TYPE_COOKING;
	}

	@Override
	public boolean shouldMoveToInventory(int slot) {
		return slot < (getGridWidth() * getGridHeight());
	}
}
