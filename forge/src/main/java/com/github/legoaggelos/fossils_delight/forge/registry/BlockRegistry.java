package com.github.legoaggelos.fossils_delight.forge.registry;

import com.github.legoaggelos.fossils_delight.FossilsDelight;
import com.github.legoaggelos.fossils_delight.forge.block.AgingPotBlock;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

public class BlockRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(FossilsDelight.MOD_ID, Registry.BLOCK_REGISTRY);

    public static final RegistrySupplier<Block> AGING_POT = BLOCKS.register("aging_pot", () -> new AgingPotBlock(BlockBehaviour.Properties.of(Material.METAL).strength(0.5F, 6.0F).sound(SoundType.LANTERN)));
}
