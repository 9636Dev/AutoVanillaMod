package org._9636dev.autovanilla;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org._9636dev.autovanilla.client.screen.AutoSmithingTableScreen;
import org._9636dev.autovanilla.common.block.AutoBlocks;
import org._9636dev.autovanilla.common.blockenttiy.AutoBlockEntities;
import org._9636dev.autovanilla.common.config.AutoCommonConfig;
import org._9636dev.autovanilla.common.container.AutoContainers;
import org._9636dev.autovanilla.common.item.AutoItems;
import org._9636dev.autovanilla.common.recipe.AutoRecipes;
import org._9636dev.autovanilla.integration.AutoModHooks;
import org.slf4j.Logger;

@Mod(AutoVanilla.MODID)
public class AutoVanilla {

    public static final String MODID = "autovanilla";
    private static final Logger LOGGER = LogUtils.getLogger();

    private final AutoModHooks hooks;
    public AutoVanilla() {
        this.hooks = new AutoModHooks();

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::doClientStuff);
        modEventBus.addListener(this::enqueueIMC);

        // Registries
        AutoBlocks.BLOCKS.register(modEventBus);
        AutoItems.ITEMS.register(modEventBus);
        AutoBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        AutoContainers.CONTAINERS.register(modEventBus);
        AutoRecipes.RECIPE_TYPES.register(modEventBus);
        AutoRecipes.RECIPE_SERIALIZERS.register(modEventBus);

        ForgeConfigSpec.Builder commonConfigBuilder = new ForgeConfigSpec.Builder();
        AutoCommonConfig.setInstance(new AutoCommonConfig(commonConfigBuilder));
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, commonConfigBuilder.build());

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.debug("Initializing AutoSmithingTable for both");
        hooks.commonSetup(event);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        LOGGER.debug("Initializing AutoSmithingTable client");

        MenuScreens.register(AutoContainers.AUTO_SMITHING_TABLE.get(), AutoSmithingTableScreen::new);
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        hooks.enqueueIMC(event);
    }

    public static final CreativeModeTab creativeModeTab = new CreativeModeTab(MODID) {
        @Override
        public net.minecraft.world.item.ItemStack makeIcon() {
            return new net.minecraft.world.item.ItemStack(AutoItems.AUTO_SMITHING_TABLE.get());
        }
    };
}
