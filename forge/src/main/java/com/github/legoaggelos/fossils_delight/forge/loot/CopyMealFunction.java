package com.github.legoaggelos.fossils_delight.forge.loot;

import com.github.legoaggelos.fossils_delight.FossilsDelight;
import com.github.legoaggelos.fossils_delight.forge.block.entity.AgingPotBlockEntity;
import com.github.legoaggelos.fossils_delight.forge.registry.FunctionRegistry;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;
import vectorwing.farmersdelight.common.registry.ModLootFunctions;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CopyMealFunction extends LootItemConditionalFunction
{
	public static final ResourceLocation ID = new ResourceLocation(FossilsDelight.MOD_ID, "copy_meal");

	private CopyMealFunction(LootItemCondition[] conditions) {
		super(conditions);
	}

	public static Builder<?> builder() {
		return simpleBuilder(CopyMealFunction::new);
	}

	@Override
	protected ItemStack run(ItemStack stack, LootContext context) {
		BlockEntity tile = context.getParamOrNull(LootContextParams.BLOCK_ENTITY);
		if (tile instanceof AgingPotBlockEntity) {
			CompoundTag tag = ((AgingPotBlockEntity) tile).writeMeal(new CompoundTag());
			if (!tag.isEmpty()) {
				stack.addTagElement("BlockEntityTag", tag);
			}
		}
		return stack;
	}

	@Override
	public LootItemFunctionType getType() {
		return FunctionRegistry.COPY_MEAL.get();
	}

	public static class Serializer extends LootItemConditionalFunction.Serializer<CopyMealFunction>
	{
		@Override
		public CopyMealFunction deserialize(JsonObject json, JsonDeserializationContext context, LootItemCondition[] conditions) {
			return new CopyMealFunction(conditions);
		}
	}
}
