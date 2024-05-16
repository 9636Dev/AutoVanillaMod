package org._9636dev.autovanilla.common.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org._9636dev.autovanilla.AutoVanilla;
import org._9636dev.autovanilla.common.block.AutoBlocks;

public class AutoItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, AutoVanilla.MODID);

    public static final RegistryObject<BlockItem> AUTO_SMITHING_TABLE = ITEMS.register("smithing_table",
            () -> new BlockItem(AutoBlocks.AUTO_SMITHING_TABLE.get(), new Item.Properties().tab(AutoVanilla.creativeModeTab)));
}
