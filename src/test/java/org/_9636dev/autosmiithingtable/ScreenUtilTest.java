package org._9636dev.autosmiithingtable;

import org._9636dev.autosmithingtablerewrite.client.screen.ScreenUtil;
import org._9636dev.autosmithingtablerewrite.common.capability.AutoEnergyStorage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ScreenUtilTest {
    private static final int TEST_RANGE = 100_000;

    @Test
    public void testTruncatedNumberConversion() {
        AutoEnergyStorage energyStorage = new AutoEnergyStorage(TEST_RANGE);
        for (int i = 0; i < TEST_RANGE; i++) {
            energyStorage.setEnergy(i);
            Assertions.assertEquals(i, ScreenUtil.convertNumberFromTruncated(
                    energyStorage.getMSBofEnergy(), energyStorage.getLSBofEnergy()));
        }
    }
}
