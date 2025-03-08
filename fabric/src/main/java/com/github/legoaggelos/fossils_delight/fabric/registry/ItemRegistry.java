package com.github.legoaggelos.fossils_delight.fabric.registry;

import com.github.legoaggelos.fossils_delight.FossilsDelight;
import com.github.legoaggelos.fossils_delight.fabric.item.CustomKnifeItem;
import com.github.legoaggelos.fossils_delight.registry.TabRegistry;
import com.nhoryzon.mc.farmersdelight.item.ModItemSettings;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;

import static com.github.teamfossilsarcheology.fossil.item.ModToolTiers.SCARAB;


public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(FossilsDelight.MOD_ID, Registry.ITEM_REGISTRY);

    public static final RegistrySupplier<Item> SCARAB_KNIFE = ITEMS.register("scarab_knife", () -> new CustomKnifeItem(SCARAB, new ModItemSettings().tab(TabRegistry.FOSSILS_DELIGHT_TAB)));


}
