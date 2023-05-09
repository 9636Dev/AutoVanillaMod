package org._9636dev.autosmithingtablerewrite.common.block;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org._9636dev.autosmithingtablerewrite.AutoSmithingTableMod;

public class AutoSmithingTableBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, AutoSmithingTableMod.MODID);

    public static final RegistryObject<AutoSmithingTableBlock> AUTO_SMITHING_TABLE =
            BLOCKS.register("auto_smithing_table", AutoSmithingTableBlock::new);
}
