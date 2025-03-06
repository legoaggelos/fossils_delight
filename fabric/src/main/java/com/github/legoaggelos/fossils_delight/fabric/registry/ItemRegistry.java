package com.github.legoaggelos.fossils_delight.fabric.registry;

import com.github.legoaggelos.fossils_delight.FossilsDelight;
import com.nhoryzon.mc.farmersdelight.item.KnifeItem;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import static com.github.teamfossilsarcheology.fossil.item.ModToolTiers.SCARAB;
import static com.nhoryzon.mc.farmersdelight.registry.TagsRegistry.KNIVES;


public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(FossilsDelight.MOD_ID, Registry.ITEM_REGISTRY);

    public static final RegistrySupplier<Item> SCARAB_KNIFE = ITEMS.register("scarab_knife", () -> new KnifeItem(SCARAB));


}
