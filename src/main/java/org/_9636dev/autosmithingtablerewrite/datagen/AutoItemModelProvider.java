package org._9636dev.autosmithingtablerewrite.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org._9636dev.autosmithingtablerewrite.AutoSmithingTableMod;
import org._9636dev.autosmithingtablerewrite.common.item.AutoItems;

public class AutoItemModelProvider extends ItemModelProvider {
    public AutoItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, AutoSmithingTableMod.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        withExistingParent(AutoItems.AUTO_SMITHING_TABLE.getId().getPath(),
                modLoc("block/smithing_table"));
    }
}
