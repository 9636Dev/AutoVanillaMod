package org._9636dev.autosmithingtablerewrite;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org._9636dev.autosmithingtablerewrite.client.screen.AutoSmithingTableScreen;
import org._9636dev.autosmithingtablerewrite.common.block.AutoBlocks;
import org._9636dev.autosmithingtablerewrite.common.blockenttiy.AutoBlockEntities;
import org._9636dev.autosmithingtablerewrite.common.container.AutoSmithingTableContainers;
import org._9636dev.autosmithingtablerewrite.common.item.AutoItems;
import org.slf4j.Logger;

@Mod(AutoSmithingTableMod.MODID)
public class AutoSmithingTableMod {

    public static final String MODID = "autosmithingtable";
    private static final Logger LOGGER = LogUtils.getLogger();
    public AutoSmithingTableMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::doClientStuff);

        // Registries
        AutoBlocks.BLOCKS.register(modEventBus);
        AutoItems.ITEMS.register(modEventBus);
        AutoBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        AutoSmithingTableContainers.CONTAINERS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.debug("Initializing AutoSmithingTable for both");
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        LOGGER.debug("Initializing AutoSmithingTable client");

        MenuScreens.register(AutoSmithingTableContainers.AUTO_SMITHING_TABLE.get(), AutoSmithingTableScreen::new);
    }
}
