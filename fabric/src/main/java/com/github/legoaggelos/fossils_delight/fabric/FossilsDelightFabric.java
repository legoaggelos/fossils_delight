package com.github.legoaggelos.fossils_delight.fabric;

import com.github.legoaggelos.fossils_delight.fabric.registry.ItemRegistry;
import com.github.legoaggelos.fossils_delight.registry.TabRegistry;
import net.fabricmc.api.ModInitializer;

import com.github.legoaggelos.fossils_delight.FossilsDelight;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

public final class FossilsDelightFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        FossilsDelight.init();
        ItemRegistry.ITEMS.register();
    }
}
