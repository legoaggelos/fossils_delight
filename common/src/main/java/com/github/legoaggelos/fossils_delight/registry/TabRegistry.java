package com.github.legoaggelos.fossils_delight.registry;

import com.github.teamfossilsarcheology.fossil.entity.prehistoric.base.PrehistoricEntityInfo;
import dev.architectury.registry.CreativeTabRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;

public class TabRegistry {
    public static final CreativeModeTab FOSSILS_DELIGHT_TAB = CreativeTabRegistry.create(
            new ResourceLocation("fossils_delight", "fossils_delight_tab"), // Tab ID
            () -> new ItemStack(Arrays.stream(PrehistoricEntityInfo.values()).filter(info->info==PrehistoricEntityInfo.NAUTILUS).toList().get(0).cookedFoodItem) // Icon
    );
}
