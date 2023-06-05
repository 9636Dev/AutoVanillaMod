package org._9636dev.autosmithingtablerewrite.integration.top;

import mcjty.theoneprobe.api.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org._9636dev.autosmithingtablerewrite.AutoSmithingTableMod;
import org._9636dev.autosmithingtablerewrite.common.block.AutoBlocks;
import org._9636dev.autosmithingtablerewrite.common.blockenttiy.AutoSmithingTableBlockEntity;

import java.util.function.Function;

public class AutoTOPProvider implements IProbeInfoProvider, Function<ITheOneProbe, Void> {
    @Override
    public ResourceLocation getID() {
        return new ResourceLocation(AutoSmithingTableMod.MODID, "top_provider");
    }

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo iProbeInfo, Player player, Level level, BlockState blockState, IProbeHitData iProbeHitData) {
        if (blockState.is(AutoBlocks.AUTO_SMITHING_TABLE.get()) && blockState.hasBlockEntity() &&
                level.getBlockEntity(iProbeHitData.getPos()) instanceof AutoSmithingTableBlockEntity be) {

            iProbeInfo.progress(
                    be.data.get(AutoSmithingTableBlockEntity.DATA_PROGRESS),
                    be.data.get(AutoSmithingTableBlockEntity.DATA_MAX_PROGRESS),
                    iProbeInfo.defaultProgressStyle().showText(false)
            );
        }
    }

    @Override
    public Void apply(ITheOneProbe iTheOneProbe) {
        iTheOneProbe.registerProvider(this);
        return null;
    }
}
