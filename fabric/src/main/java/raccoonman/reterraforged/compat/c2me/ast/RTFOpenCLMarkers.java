package raccoonman.reterraforged.compat.c2me.ast;

import raccoonman.reterraforged.world.worldgen.densityfunction.CellSampler;

public class RTFOpenCLMarkers {

    public static final Object MARKER_rtf_cellSampler = new Object();

    public static final Object MARKER_rtf_noiseFunction = new Object();

    // Number of CellSampler.Field enum values we prefill.
    public static final int CELL_FIELD_COUNT = CellSampler.Field.values().length;

    private static int noiseFunctionDataOffset = -1;

    private static int noiseFunctionCount = 0;

    public static int getNoiseFunctionCount() {
        return noiseFunctionCount;
    }

    public static int registerNoiseFunction() {
        return noiseFunctionCount++;
    }

    public static void resetNoiseFunctionCount() {
        noiseFunctionCount = 0;
    }

    public static int getCellSamplerDataOffset() {
        return cellSamplerDataOffset;
    }

    public static void setCellSamplerDataOffset(int offset) {
        cellSamplerDataOffset = offset;
    }

    public static int getNoiseFunctionDataOffset() {
        return noiseFunctionDataOffset;
    }

    public static void setNoiseFunctionDataOffset(int offset) {
        noiseFunctionDataOffset = offset;
    }

    public static void resetAll() {
        cellSamplerDataOffset = -1;
        noiseFunctionDataOffset = -1;
        noiseFunctionCount = 0;
    }
}
