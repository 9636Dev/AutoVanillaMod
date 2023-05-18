package org._9636dev.autosmithingtablerewrite.common.container;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org._9636dev.autosmithingtablerewrite.AutoSmithingTableMod;

public class AutoSmithingTableContainers {
    public static final DeferredRegister<MenuType<?>> CONTAINERS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, AutoSmithingTableMod.MODID);

    public static final RegistryObject<MenuType<AutoSmithingTableContainer>> AUTO_SMITHING_TABLE = CONTAINERS
            .register("smithing_table", () -> new MenuType<>(AutoSmithingTableContainer::new));
}
