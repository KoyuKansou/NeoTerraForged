package raccoonman.reterraforged.compat.c2me.ast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import raccoonman.reterraforged.world.worldgen.GeneratorContext;
import raccoonman.reterraforged.world.worldgen.cell.Cell;
import raccoonman.reterraforged.world.worldgen.cell.heightmap.WorldLookup;
import raccoonman.reterraforged.world.worldgen.densityfunction.CellSampler;
import raccoonman.reterraforged.world.worldgen.densityfunction.NoiseFunction;

import net.minecraft.core.QuartPos;
import net.minecraft.world.level.levelgen.DensityFunction;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;


public class RTFPrefillHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(RTFPrefillHelper.class);


// Overwrites the CellSampler region of the rw_data buffer with pre-computed values.
// @param rwData      The ByteBuffer returned by CLDataUtil.createForArea()
// @param byteOffset  The byte offset in rwData where our data region starts
// @param ctx         The GeneratorContext containing the WorldLookup
// @param startBiomeX Starting biome X (from worldgen_params.startBiomeX)
// @param startBiomeZ Starting biome Z (from worldgen_params.startBiomeZ)
// @param sizeBiomeX  Biome size X (from worldgen_params.sizeBiomeX)
// @param sizeBiomeZ  Biome size Z (from worldgen_params.sizeBiomeZ)
// 
    public static void fillCellSamplerData(ByteBuffer rwData, int byteOffset,
                                           GeneratorContext ctx,
                                           int startBiomeX, int startBiomeZ,
                                           int sizeBiomeX, int sizeBiomeZ) {
        if (ctx == null || ctx.lookup == null) {
            LOGGER.warn("RTF GeneratorContext not available for CellSampler prefill");
            return;
        }

        WorldLookup lookup = ctx.lookup;
        int biomeW = sizeBiomeX + 1;
        int biomeH = sizeBiomeZ + 1;
        int fieldCount = RTFOpenCLMarkers.CELL_FIELD_COUNT;
        Cell cell = new Cell();
        CellSampler.Field[] fields = CellSampler.Field.values();

        // Bounds check: ensure we don't write past the allocated buffer
        int requiredBytes = fieldCount * biomeW * biomeH * 8;
        if (byteOffset + requiredBytes > rwData.capacity()) {
            LOGGER.error("RTF CellSampler buffer overflow: need {} bytes at offset {}, buffer capacity {}",
                    requiredBytes, byteOffset, rwData.capacity());
            return;
        }

        rwData.order(ByteOrder.nativeOrder());

        for (int bx = 0; bx < biomeW; bx++) {
            for (int bz = 0; bz < biomeH; bz++) {
                int blockX = QuartPos.toBlock(startBiomeX + bx);
                int blockZ = QuartPos.toBlock(startBiomeZ + bz);
                cell.reset();
                lookup.applyCell(cell, blockX, blockZ, true);

                for (int f = 0; f < fieldCount; f++) {
                    int idx = f * biomeW * biomeH + bx * biomeH + bz;
                    double value = fields[f].read(cell, lookup.getHeightmap());
                    rwData.putDouble(byteOffset + idx * 8, value);
                }
            }
        }
    }

    public static void fillNoiseFunctionData(ByteBuffer rwData, int byteOffset,
                                             int startBiomeX, int startBiomeZ,
                                             int sizeBiomeX, int sizeBiomeZ) {
        List<NoiseFunction> noiseFunctions = RTFNoiseFunctionRegistry.getAll();
        if (noiseFunctions.isEmpty()) {
            return;
        }

        int biomeW = sizeBiomeX + 1;
        int biomeH = sizeBiomeZ + 1;

        // Bounds check
        int requiredBytes = noiseFunctions.size() * biomeW * biomeH * 8;
        if (byteOffset + requiredBytes > rwData.capacity()) {
            LOGGER.error("RTF NoiseFunction buffer overflow: need {} bytes at offset {}, buffer capacity {}",
                    requiredBytes, byteOffset, rwData.capacity());
            return;
        }

        rwData.order(ByteOrder.nativeOrder());

        for (int i = 0; i < noiseFunctions.size(); i++) {
            NoiseFunction func = noiseFunctions.get(i);
            if (func == null) continue;

            for (int bx = 0; bx < biomeW; bx++) {
                for (int bz = 0; bz < biomeH; bz++) {
                    int blockX = QuartPos.toBlock(startBiomeX + bx);
                    int blockZ = QuartPos.toBlock(startBiomeZ + bz);
                    int idx = i * biomeW * biomeH + bx * biomeH + bz;
                    double value = func.compute(new SinglePointContext(blockX, 0, blockZ));
                    rwData.putDouble(byteOffset + idx * 8, value);
                }
            }
        }
    }


    public static int readDataOffset(ByteBuffer rwData, int dataIndex) {
        rwData.order(ByteOrder.nativeOrder());
        return rwData.getInt(128 + dataIndex * 4);
    }


    public static int readStartBiomeX(ByteBuffer rwData) {
        rwData.order(ByteOrder.nativeOrder());
        return rwData.getInt(0); // offsetof(worldgen_params_t, startBiomeX)
    }

    public static int readStartBiomeZ(ByteBuffer rwData) {
        rwData.order(ByteOrder.nativeOrder());
        return rwData.getInt(4); // offsetof(worldgen_params_t, startBiomeZ)
    }

    public static int readSizeBiomeX(ByteBuffer rwData) {
        rwData.order(ByteOrder.nativeOrder());
        return rwData.getInt(8); // offsetof(worldgen_params_t, sizeBiomeX)
    }

    public static int readSizeBiomeZ(ByteBuffer rwData) {
        rwData.order(ByteOrder.nativeOrder());
        return rwData.getInt(12); // offsetof(worldgen_params_t, sizeBiomeZ)
    }

    private record SinglePointContext(int blockX, int blockY, int blockZ) implements DensityFunction.FunctionContext {
        @Override
        public int blockX() { return blockX; }

        @Override
        public int blockY() { return blockY; }

        @Override
        public int blockZ() { return blockZ; }
    }
}
