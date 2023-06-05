package org._9636dev.autosmithingtablerewrite.integration;

import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import org._9636dev.autosmithingtablerewrite.integration.top.AutoTOPProvider;

@SuppressWarnings("unused")
public class AutoModHooks {
    public boolean TOPLoaded;

    public AutoModHooks() {
        TOPLoaded = false;
    }

    public void commonSetup(final FMLCommonSetupEvent event) {
        TOPLoaded = ModList.get().isLoaded("theoneprobe");
    }

    public void enqueueIMC(final InterModEnqueueEvent event) {
        if (TOPLoaded) {
            InterModComms.sendTo("theoneprobe", "getTheOneProbe", AutoTOPProvider::new);
        }
    }
}
