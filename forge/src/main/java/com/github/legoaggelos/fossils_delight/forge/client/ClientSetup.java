package com.github.legoaggelos.fossils_delight.forge.client;

import com.github.legoaggelos.fossils_delight.forge.client.recipebook.RecipeCategories;
import com.github.legoaggelos.fossils_delight.forge.registry.BlockRegistry;
import com.github.legoaggelos.fossils_delight.forge.registry.MenuRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import com.github.legoaggelos.fossils_delight.forge.client.gui.AgingPotScreen;

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
