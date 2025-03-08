package com.github.legoaggelos.fossils_delight.forge.client;

import com.github.legoaggelos.fossils_delight.forge.client.recipebook.RecipeCategories;
import com.github.legoaggelos.fossils_delight.forge.registry.BlockRegistry;
import com.github.legoaggelos.fossils_delight.forge.registry.MenuRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import vectorwing.farmersdelight.client.gui.ComfortHealthOverlay;
import vectorwing.farmersdelight.client.gui.CookingPotScreen;
import vectorwing.farmersdelight.client.gui.NourishmentHungerOverlay;

import com.github.legoaggelos.fossils_delight.forge.client.gui.AgingPotScreen;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModMenuTypes;

public class ClientSetup {
    public static void init(final FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(BlockRegistry.AGING_POT.get(), RenderType.cutout());
        event.enqueueWork(() ->
        {
            MenuScreens.register(MenuRegistry.AGING_POT.get(), AgingPotScreen::new);
            RecipeCategories.init();
        });

    }
}
