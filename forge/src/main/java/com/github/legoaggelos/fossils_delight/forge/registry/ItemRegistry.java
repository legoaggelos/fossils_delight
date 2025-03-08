package com.github.legoaggelos.fossils_delight.forge.registry;

import com.github.legoaggelos.fossils_delight.FossilsDelight;
import com.github.legoaggelos.fossils_delight.registry.TabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import vectorwing.farmersdelight.common.item.KnifeItem;

import static com.github.teamfossilsarcheology.fossil.item.ModToolTiers.SCARAB;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(FossilsDelight.MOD_ID, Registry.ITEM_REGISTRY);

    public static final RegistrySupplier<Item> SCARAB_KNIFE = ITEMS.register("scarab_knife", () -> new KnifeItem(SCARAB, 0.5F, -1.8F, new Item.Properties().tab(TabRegistry.FOSSILS_DELIGHT_TAB)));
    public static final RegistrySupplier<BlockItem> AGING_POT = ITEMS.register("aging_pot", (() -> new BlockItem(BlockRegistry.AGING_POT.get(), new Item.Properties().tab(TabRegistry.FOSSILS_DELIGHT_TAB).stacksTo(1))));
}
