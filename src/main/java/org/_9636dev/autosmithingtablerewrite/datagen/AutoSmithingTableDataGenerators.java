package org._9636dev.autosmithingtablerewrite.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org._9636dev.autosmithingtablerewrite.AutoSmithingTableMod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = AutoSmithingTableMod.MODID)
public class AutoSmithingTableDataGenerators {
    @SubscribeEvent
    public static void onGatherData(final GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        generator.addProvider(event.includeClient(), new AutoBlockStateProvider(generator, event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new AutoItemModelProvider(generator, event.getExistingFileHelper()));
    }
}
