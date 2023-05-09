package org._9636dev.autosmithingtablerewrite.common.container;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org._9636dev.autosmithingtablerewrite.AutoSmithingTableMod;

public class AutoSmithingTableContainer {
    public static final DeferredRegister<MenuType<?>> CONTAINERS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, AutoSmithingTableMod.MODID);
}
