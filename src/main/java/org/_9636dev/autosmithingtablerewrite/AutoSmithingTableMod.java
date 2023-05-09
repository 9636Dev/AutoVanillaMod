package org._9636dev.autosmithingtablerewrite;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org._9636dev.autosmithingtablerewrite.common.block.AutoSmithingTableBlocks;
import org._9636dev.autosmithingtablerewrite.common.item.AutoSmithingTableItems;
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
        AutoSmithingTableBlocks.BLOCKS.register(modEventBus);
        AutoSmithingTableItems.ITEMS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.debug("Initializing AutoSmithingTable for both");
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        LOGGER.debug("Initializing AutoSmithingTable client");
    }
}
