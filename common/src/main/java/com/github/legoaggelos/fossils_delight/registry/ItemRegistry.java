package com.github.legoaggelos.fossils_delight.registry;

import com.github.legoaggelos.fossils_delight.FossilsDelight;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(FossilsDelight.MOD_ID, Registry.ITEM_REGISTRY);
}
