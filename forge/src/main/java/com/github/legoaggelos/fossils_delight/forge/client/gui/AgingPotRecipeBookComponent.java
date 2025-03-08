package com.github.legoaggelos.fossils_delight.forge.client.gui;

import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import vectorwing.farmersdelight.FarmersDelight;
import com.github.legoaggelos.fossils_delight.forge.crafting.AgingPotRecipe;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.utility.TextUtils;

import javax.annotation.Nonnull;
import java.util.List;

public class AgingPotRecipeBookComponent extends RecipeBookComponent
{
	protected static final ResourceLocation RECIPE_BOOK_BUTTONS = new ResourceLocation(FarmersDelight.MODID, "textures/gui/recipe_book_buttons.png");

	@Override
	protected void initFilterButtonTextures() {
		this.filterButton.initTextureValues(0, 0, 28, 18, RECIPE_BOOK_BUTTONS);
	}

	public void hide() {
		this.setVisible(false);
	}

	@Override
	@Nonnull
	protected Component getRecipeFilterName() {
		return new TranslatableComponent("fossils_delight.container.recipe_book.cookable");
	}

	@Override
	public void setupGhostRecipe(Recipe<?> recipe, List<Slot> slots) {
		ItemStack resultStack = recipe.getResultItem();
		this.ghostRecipe.setRecipe(recipe);
		if (slots.get(12).getItem().isEmpty()) {
			this.ghostRecipe.addIngredient(Ingredient.of(resultStack), (slots.get(12)).x, (slots.get(12)).y);
		}
		setupGhostContainer(recipe, slots);

		this.placeRecipe(this.menu.getGridWidth(), this.menu.getGridHeight(), this.menu.getResultSlotIndex(), recipe, recipe.getIngredients().iterator(), 0);
	}

	public void setupGhostContainer(Recipe<?> recipe, List<Slot> slots){
		String recipeType = recipe.getType().toString();
		if (recipeType.equals("fossils_delight:cooking")){
			AgingPotRecipe potRecipe = (AgingPotRecipe) recipe;
			ItemStack containerStack = potRecipe.getOutputContainer();
			if (!containerStack.isEmpty()) {
				this.ghostRecipe.addIngredient(Ingredient.of(containerStack), (slots.get(13)).x, (slots.get(13)).y);
			}
		} else if(recipeType.equals("farmersdelight:cooking")){
			CookingPotRecipe potRecipe = (CookingPotRecipe) recipe;
			ItemStack containerStack = potRecipe.getOutputContainer();
			if (!containerStack.isEmpty()) {
				this.ghostRecipe.addIngredient(Ingredient.of(containerStack), (slots.get(13)).x, (slots.get(13)).y);
			}
		}
	}
}
