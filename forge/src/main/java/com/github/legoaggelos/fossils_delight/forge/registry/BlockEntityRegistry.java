package com.github.legoaggelos.fossils_delight.forge.registry;

import com.github.legoaggelos.fossils_delight.FossilsDelight;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import com.github.legoaggelos.fossils_delight.forge.block.entity.AgingPotBlockEntity;

public class BlockEntityRegistry {
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, FossilsDelight.MOD_ID);

	public static final RegistryObject<BlockEntityType<AgingPotBlockEntity>> AGING_POT = BLOCK_ENTITIES.register("aging_pot",
			() -> BlockEntityType.Builder.of(AgingPotBlockEntity::new, BlockRegistry.AGING_POT.get()).build(null));
}