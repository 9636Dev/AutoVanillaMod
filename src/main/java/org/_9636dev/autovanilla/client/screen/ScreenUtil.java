package org._9636dev.autovanilla.client.screen;

import com.mojang.logging.LogUtils;

@SuppressWarnings("unused")
public class ScreenUtil {
    /**
     * Maps number from a range of inputs to another range of outputs
     * @param pMinInput minimum input
     * @param pMaxInput maximum input
     * @param pMinMapped minimum output
     * @param pMaxMapped maximum output
     * @param pNumber number to map (input)
     * @return the number mapped (output)
     */
    public static int mapNumberToRange(int pMinInput, int pMaxInput, int pMinMapped, int pMaxMapped, int pNumber) {
        if (pMaxInput - pMinInput == 0) return pMaxMapped;

        if (pNumber < pMinInput || pNumber > pMaxInput) LogUtils.getLogger().debug("Could not map number not in range: {} (range: {} - {})", pNumber, pMinInput, pMaxInput);

        return (int)(pNumber / (float)(pMaxInput - pMinInput) * (pMaxMapped - pMinMapped) + pMinMapped);
    }

    /**
     * Calculates whether a point is in a rectangle or not
     * @param pX X coordinate
     * @param pY Y coordinate
     * @param pMinX minimum X
     * @param pMaxX maximum X
     * @param pMinY minimum Y
     * @param pMaxY maximum y
     * @return whether the point is in the rectangle
     */
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
