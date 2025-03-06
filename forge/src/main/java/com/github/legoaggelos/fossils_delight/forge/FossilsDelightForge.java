package com.github.legoaggelos.fossils_delight.forge;

import com.github.legoaggelos.fossils_delight.forge.items.ItemRegistry;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import com.github.legoaggelos.fossils_delight.FossilsDelight;

@Mod(FossilsDelight.MOD_ID)
public final class FossilsDelightForge {
    public FossilsDelightForge() {
        // Submit our event bus to let Architectury API register our content on the right time.
        EventBuses.registerModEventBus(FossilsDelight.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        ItemRegistry.ITEMS.register();

        // Run our common setup.
        FossilsDelight.init();
    }
}
