package com.github.legoaggelos.fossils_delight.registry;

import com.github.legoaggelos.fossils_delight.FossilsDelight;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class TagRegistry {
    public static final TagKey<Item> HOLD_OVER_HEAD = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(FossilsDelight.MOD_ID, "hold_over_head"));
}
