package org._9636dev.autosmithingtablerewrite.common.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org._9636dev.autosmithingtablerewrite.AutoSmithingTableMod;
import org._9636dev.autosmithingtablerewrite.common.block.AutoSmithingTableBlocks;

public class AutoSmithingTableItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, AutoSmithingTableMod.MODID);

    public static final RegistryObject<BlockItem> AUTO_SMITHING_TABLE = ITEMS.register("smithing_table",
            () -> new BlockItem(AutoSmithingTableBlocks.AUTO_SMITHING_TABLE.get(), new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
}
