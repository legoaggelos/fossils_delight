package com.github.legoaggelos.fossils_delight.forge;

import com.github.legoaggelos.fossils_delight.forge.client.ClientSetup;
import com.github.legoaggelos.fossils_delight.forge.registry.*;
import dev.architectury.platform.forge.EventBuses;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import com.github.legoaggelos.fossils_delight.FossilsDelight;

@Mod(FossilsDelight.MOD_ID)
public final class FossilsDelightForge {
    public static final RecipeBookType RECIPE_TYPE_COOKING = RecipeBookType.create("COOKING");
    public FossilsDelightForge() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        // Submit our event bus to let Architectury API register our content on the right time.
        EventBuses.registerModEventBus(FossilsDelight.MOD_ID, modEventBus);

        modEventBus.addListener(ClientSetup::init);

        ItemRegistry.ITEMS.register();
        BlockRegistry.BLOCKS.register();
        BlockEntityRegistry.BLOCK_ENTITIES.register(modEventBus);
        MenuRegistry.MENUS.register(modEventBus);
        RecipeSerializerRegistry.RECIPE_SERIALIZERS.register(modEventBus);
        RecipeRegistry.RECIPE_TYPES.register(modEventBus);



        // Run our common setup.
        FossilsDelight.init();
    }
}
