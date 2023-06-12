package org._9636dev.autovanilla.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org._9636dev.autovanilla.AutoVanilla;
import org._9636dev.autovanilla.common.block.AutoBlocks;

public class AutoBlockStateProvider extends BlockStateProvider {
    public AutoBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, AutoVanilla.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        ResourceLocation autoSmithingTableSide = new ResourceLocation(AutoVanilla.MODID,
                "block/auto_smithing_table_side");
        ResourceLocation autoSmithingTableFront = new ResourceLocation(AutoVanilla.MODID,
                "block/auto_smithing_table_front");
        ResourceLocation autoSmithingTableBottom = new ResourceLocation(AutoVanilla.MODID,
                "block/auto_smithing_table_bottom");
        ResourceLocation autoSmithingTableTop = new ResourceLocation(AutoVanilla.MODID,
                "block/auto_smithing_table_top");

        horizontalBlock(AutoBlocks.AUTO_SMITHING_TABLE.get(),
                models()
                        .cube(AutoBlocks.AUTO_SMITHING_TABLE.getId().getPath(),
                        autoSmithingTableBottom, autoSmithingTableTop, autoSmithingTableFront,
                        autoSmithingTableSide, autoSmithingTableSide, autoSmithingTableSide)
                        .texture("particle", autoSmithingTableFront)
        );
    }
}
