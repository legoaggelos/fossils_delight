package com.github.legoaggelos.fossils_delight.forge.registry;

import com.github.legoaggelos.fossils_delight.FossilsDelight;
import com.github.legoaggelos.fossils_delight.forge.crafting.AgingPotRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RecipeSerializerRegistry
{
	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, FossilsDelight.MOD_ID);

	public static final RegistryObject<RecipeSerializer<?>> COOKING = RECIPE_SERIALIZERS.register("cooking", AgingPotRecipe.Serializer::new);

}
