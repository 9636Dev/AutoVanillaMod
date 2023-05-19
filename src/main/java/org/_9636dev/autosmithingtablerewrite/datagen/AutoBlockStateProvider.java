package org._9636dev.autosmithingtablerewrite.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org._9636dev.autosmithingtablerewrite.AutoSmithingTableMod;
import org._9636dev.autosmithingtablerewrite.common.block.AutoBlocks;

public class AutoBlockStateProvider extends BlockStateProvider {
    public AutoBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, AutoSmithingTableMod.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        ResourceLocation autoSmithingTableSide = new ResourceLocation(AutoSmithingTableMod.MODID,
                "block/auto_smithing_table_side");
        ResourceLocation autoSmithingTableFront = new ResourceLocation(AutoSmithingTableMod.MODID,
                "block/auto_smithing_table_front");
        ResourceLocation autoSmithingTableBottom = new ResourceLocation(AutoSmithingTableMod.MODID,
                "block/auto_smithing_table_bottom");
        ResourceLocation autoSmithingTableTop = new ResourceLocation(AutoSmithingTableMod.MODID,
                "block/auto_smithing_table_top");

        horizontalBlock(AutoBlocks.AUTO_SMITHING_TABLE.get(),
                models().cube(AutoBlocks.AUTO_SMITHING_TABLE.getId().getPath(),
                        autoSmithingTableBottom, autoSmithingTableTop, autoSmithingTableFront,
                        autoSmithingTableSide, autoSmithingTableSide, autoSmithingTableSide));
    }
}
