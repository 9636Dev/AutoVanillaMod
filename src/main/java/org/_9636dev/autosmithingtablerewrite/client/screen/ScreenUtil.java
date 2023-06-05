package org._9636dev.autosmithingtablerewrite.client.screen;

import com.mojang.logging.LogUtils;

@SuppressWarnings("unused")
public class ScreenUtil {
    public static int mapNumberToRange(int pMinInput, int pMaxInput, int pMinMapped, int pMaxMapped, int pNumber) {
        if (pMaxInput - pMinInput == 0) return pMaxMapped;

        if (pNumber < pMinInput || pNumber > pMaxInput) LogUtils.getLogger().debug("Could not map number not in range: {} (range: {} - {})", pNumber, pMinInput, pMaxInput);

        return (int)(pNumber / (float)(pMaxInput - pMinInput) * (pMaxMapped - pMinMapped) + pMinMapped);
    }

    public static boolean isPointInRect(int pX, int pY, int pMinX, int pMaxX, int pMinY, int pMaxY) {
        return (pMinX <= pX && pX <= pMaxX) && (pMinY <= pY && pY <= pMaxY);
    }

    /**
     * Converts truncated 16bit numbers to 32bit int type
     * @param pMSig most significant bits
     * @param pLSig least significant bits
     * @return 32bit int
     */
    public static int convertNumberFromTruncated(int pMSig, int pLSig) {
        return (pMSig << 16) | (pLSig & 0xffff);
    }
}
