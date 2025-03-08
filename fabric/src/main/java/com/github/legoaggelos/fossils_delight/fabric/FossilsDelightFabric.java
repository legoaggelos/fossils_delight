package com.github.legoaggelos.fossils_delight.fabric;

import com.github.legoaggelos.fossils_delight.fabric.registry.ItemRegistry;
import net.fabricmc.api.ModInitializer;

import com.github.legoaggelos.fossils_delight.FossilsDelight;

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
