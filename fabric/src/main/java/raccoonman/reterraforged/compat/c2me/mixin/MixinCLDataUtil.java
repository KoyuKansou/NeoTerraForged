package raccoonman.reterraforged.compat.c2me.mixin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import it.unimi.dsi.fastutil.objects.Reference2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;

import com.ishland.c2me.opts.accel.opencl.common.gen.CLDataUtil;
import raccoonman.reterraforged.compat.c2me.ast.RTFOpenCLMarkers;
import raccoonman.reterraforged.compat.c2me.ast.RTFPrefillHelper;
import raccoonman.reterraforged.world.worldgen.GeneratorContext;
import raccoonman.reterraforged.world.worldgen.RTFWorldLookupHolder;

import java.nio.ByteBuffer;


@Mixin(targets = "com.ishland.c2me.opts.accel.opencl.common.gen.CLDataUtil", remap = false)
public class MixinCLDataUtil {

    @Unique
    private static final Logger rtf$LOGGER = LoggerFactory.getLogger("RTF-CLDataUtil");


    @Inject(method = "transformGlobalDynamicDataOffsets", at = @At("RETURN"), cancellable = true)
    private static void rtf$replaceMarkersWithBlobs(
            Reference2IntLinkedOpenHashMap<Object> globalDynamicDataOffsets,
            CallbackInfoReturnable<Reference2IntLinkedOpenHashMap<Object>> cir
    ) {
        Reference2IntLinkedOpenHashMap<Object> result = cir.getReturnValue();
        boolean modified = false;

        Reference2IntLinkedOpenHashMap<Object> newResult = new Reference2IntLinkedOpenHashMap<>();

        for (Reference2IntMap.Entry<Object> entry : result.reference2IntEntrySet()) {
            Object key = entry.getKey();
            int value = entry.getIntValue();

            if (key == RTFOpenCLMarkers.MARKER_rtf_cellSampler) {
                // Max biome grid: chunkSize=32 → 129×129 (chunkSize*4+1)
                // chunkSize=16 (default) → 65×65. Use 129 for safety.
                int maxBiomeSize = 129;
                int bufferSize = RTFOpenCLMarkers.CELL_FIELD_COUNT * maxBiomeSize * maxBiomeSize * 8;
                newResult.put(new CLDataUtil.ConstantBlob(new byte[bufferSize], 8), value);
                modified = true;
            } else if (key == RTFOpenCLMarkers.MARKER_rtf_noiseFunction) {
                int maxBiomeSize = 129;
                int noiseFuncCount = Math.max(1, RTFOpenCLMarkers.getNoiseFunctionCount());
                int bufferSize = noiseFuncCount * maxBiomeSize * maxBiomeSize * 8;
                newResult.put(new CLDataUtil.ConstantBlob(new byte[bufferSize], 8), value);
                modified = true;
            } else {
                newResult.put(key, value);
            }
        }

        if (modified) {
            rtf$LOGGER.info("Replaced RTF markers with ConstantBlob placeholders");
            cir.setReturnValue(newResult);
        }
    }


    @Inject(method = "worldgen_data_root$createForArea", at = @At("RETURN"))
    private static void rtf$fillBufferData(CallbackInfoReturnable<ByteBuffer> cir) {
        ByteBuffer rwData = cir.getReturnValue();
        if (rwData == null) return;

        try {
            GeneratorContext ctx = RTFWorldLookupHolder.get();
            rtf$fillRTFData(rwData, ctx);
        } catch (Exception e) {
            rtf$LOGGER.error("Error filling RTF buffer data in createForArea", e);
        }
    }


    @Inject(method = "worldgen_data_root$createForFlatCacheOnly", at = @At("RETURN"))
    private static void rtf$fillFlatCacheBufferData(CallbackInfoReturnable<ByteBuffer> cir) {
        ByteBuffer rwData = cir.getReturnValue();
        if (rwData == null) return;

        try {
            GeneratorContext ctx = RTFWorldLookupHolder.get();
            rtf$fillRTFData(rwData, ctx);
        } catch (Exception e) {
            rtf$LOGGER.error("Error filling RTF buffer data in createForFlatCacheOnly", e);
        }
    }

    @Unique
    private static void rtf$fillRTFData(ByteBuffer rwData, GeneratorContext ctx) {
        if (ctx == null) {
            rtf$LOGGER.warn("RTF GeneratorContext not available for buffer fill");
            return;
        }

        int cellDataOffset = RTFOpenCLMarkers.getCellSamplerDataOffset();
        int noiseDataOffset = RTFOpenCLMarkers.getNoiseFunctionDataOffset();

        if (cellDataOffset >= 0) {
            int byteOffset = RTFPrefillHelper.readDataOffset(rwData, cellDataOffset);
            if (byteOffset > 0) {
                int startBiomeX = RTFPrefillHelper.readStartBiomeX(rwData);
                int startBiomeZ = RTFPrefillHelper.readStartBiomeZ(rwData);
                int sizeBiomeX = RTFPrefillHelper.readSizeBiomeX(rwData);
                int sizeBiomeZ = RTFPrefillHelper.readSizeBiomeZ(rwData);
                RTFPrefillHelper.fillCellSamplerData(rwData, byteOffset, ctx,
                        startBiomeX, startBiomeZ, sizeBiomeX, sizeBiomeZ);
            }
        }

        if (noiseDataOffset >= 0) {
            int byteOffset = RTFPrefillHelper.readDataOffset(rwData, noiseDataOffset);
            if (byteOffset > 0) {
                int startBiomeX = RTFPrefillHelper.readStartBiomeX(rwData);
                int startBiomeZ = RTFPrefillHelper.readStartBiomeZ(rwData);
                int sizeBiomeX = RTFPrefillHelper.readSizeBiomeX(rwData);
                int sizeBiomeZ = RTFPrefillHelper.readSizeBiomeZ(rwData);
                RTFPrefillHelper.fillNoiseFunctionData(rwData, byteOffset,
                        startBiomeX, startBiomeZ, sizeBiomeX, sizeBiomeZ);
            }
        }
    }
}
