package com.github.legoaggelos.fossils_delight.forge.client.recipebook;

import com.github.legoaggelos.fossils_delight.forge.FossilsDelightForge;
import com.github.legoaggelos.fossils_delight.forge.crafting.AgingPotRecipe;
import com.github.legoaggelos.fossils_delight.forge.registry.RecipeRegistry;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.RecipeBookRegistry;

import vectorwing.farmersdelight.common.item.DrinkableItem;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.function.Supplier;

public class RecipeCategories
{
	public static final Supplier<RecipeBookCategories> COOKING_SEARCH = Suppliers.memoize(() -> RecipeBookCategories.create("COOKING_SEARCH", new ItemStack(Items.COMPASS)));
	public static final Supplier<RecipeBookCategories> COOKING_MEALS = Suppliers.memoize(() -> RecipeBookCategories.create("COOKING_MEALS", new ItemStack(ModItems.VEGETABLE_NOODLES.get())));
	public static final Supplier<RecipeBookCategories> COOKING_DRINKS = Suppliers.memoize(() -> RecipeBookCategories.create("COOKING_DRINKS", new ItemStack(ModItems.APPLE_CIDER.get())));
	public static final Supplier<RecipeBookCategories> COOKING_MISC = Suppliers.memoize(() -> RecipeBookCategories.create("COOKING_MISC", new ItemStack(ModItems.DUMPLINGS.get()), new ItemStack(ModItems.TOMATO_SAUCE.get())));

	public static void init() {
		RecipeBookRegistry.addCategoriesToType(FossilsDelightForge.RECIPE_TYPE_COOKING, ImmutableList.of(COOKING_SEARCH.get(), COOKING_MEALS.get(), COOKING_DRINKS.get(), COOKING_MISC.get()));
		RecipeBookRegistry.addAggregateCategories(COOKING_SEARCH.get(), ImmutableList.of(COOKING_MEALS.get(), COOKING_DRINKS.get(), COOKING_MISC.get()));
		RecipeBookRegistry.addCategoriesFinder(RecipeRegistry.COOKING.get(), recipe ->
		{
			if (recipe instanceof AgingPotRecipe cookingRecipe) {
				AgingPotRecipeBookTab tab = cookingRecipe.getRecipeBookTab();
				if (tab != null) {
					return switch (tab) {
						case MEALS -> COOKING_MEALS.get();
						case DRINKS -> COOKING_DRINKS.get();
						case MISC -> COOKING_MISC.get();
					};
				}
			}

			// If no tab is specified in recipe, this fallback organizes them instead
			if (recipe.getResultItem().getItem() instanceof DrinkableItem) {
				return COOKING_DRINKS.get();
			}
			return COOKING_MISC.get();
		});
	}
}
