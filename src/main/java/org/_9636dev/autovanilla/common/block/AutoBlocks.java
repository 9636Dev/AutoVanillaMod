package org._9636dev.autovanilla.common.block;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org._9636dev.autovanilla.AutoVanilla;

public class AutoBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, AutoVanilla.MODID);

    public static final RegistryObject<AutoSmithingTableBlock> AUTO_SMITHING_TABLE =
            BLOCKS.register("smithing_table", AutoSmithingTableBlock::new);
}
