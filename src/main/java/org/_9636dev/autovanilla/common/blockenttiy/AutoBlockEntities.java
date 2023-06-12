package org._9636dev.autovanilla.common.blockenttiy;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org._9636dev.autovanilla.AutoVanilla;
import org._9636dev.autovanilla.common.block.AutoBlocks;

public class AutoBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, AutoVanilla.MODID);

    @SuppressWarnings("DataFlowIssue")
    public static final RegistryObject<BlockEntityType<AutoSmithingTableBlockEntity>> AUTO_SMITHING_TABLE =
            BLOCK_ENTITIES.register("smithing_table", () -> BlockEntityType.Builder
                    .of(AutoSmithingTableBlockEntity::new, AutoBlocks.AUTO_SMITHING_TABLE.get()).build(null)
            );
}
