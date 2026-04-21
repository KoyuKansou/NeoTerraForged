package raccoonman.reterraforged.compat.c2me.ast;

import com.ishland.c2me.opts.dfc.common.ast.misc.DelegateNode;
import com.ishland.c2me.opts.dfc.common.gen.opencl.OpenCLGen;
import raccoonman.reterraforged.world.worldgen.densityfunction.NoiseFunction;

public class RTFNoiseFunctionNode extends DelegateNode {

    private final NoiseFunction noiseFunction;
    private int registeredOrdinal = -1;

    public RTFNoiseFunctionNode(NoiseFunction noiseFunction) {
        super(noiseFunction);
        this.noiseFunction = noiseFunction;
    }

    public NoiseFunction getNoiseFunction() {
        return this.noiseFunction;
    }

    @Override
    public String doCLGen(OpenCLGen.Context context) {
        int dataOffset = context.allocGlobalDynamicData(RTFOpenCLMarkers.MARKER_rtf_noiseFunction);
        RTFOpenCLMarkers.setNoiseFunctionDataOffset(dataOffset);

        this.registeredOrdinal = RTFOpenCLMarkers.registerNoiseFunction();
        RTFNoiseFunctionRegistry.register(this.registeredOrdinal, this.noiseFunction);

        // Bilinear interpolation between biome-resolution samples
        return "if (ctx.rw_data) {\n" +
               "    global const worldgen_params_t * restrict params = ctx.rw_data;\n" +
               "    global const double * restrict rtf_data = df_data_offset_global(ctx.rw_data, " + dataOffset + ");\n" +
               "    if (rtf_data) {\n" +
               "        int biomeW = params->sizeBiomeX + 1;\n" +
               "        int biomeH = params->sizeBiomeZ + 1;\n" +
               "        double fbx = ((double)ctx.x) / 4.0 - (double)params->startBiomeX;\n" +
               "        double fbz = ((double)ctx.z) / 4.0 - (double)params->startBiomeZ;\n" +
               "        int ix0 = (int)floor(fbx);\n" +
               "        int iz0 = (int)floor(fbz);\n" +
               "        int ix1 = ix0 + 1;\n" +
               "        int iz1 = iz0 + 1;\n" +
               "        double fx = fbx - (double)ix0;\n" +
               "        double fz = fbz - (double)iz0;\n" +
               "        if (ix0 < 0) { ix0 = 0; fx = 0.0; }\n" +
               "        if (iz0 < 0) { iz0 = 0; fz = 0.0; }\n" +
               "        if (ix1 >= biomeW) { ix1 = biomeW - 1; fx = 0.0; }\n" +
               "        if (iz1 >= biomeH) { iz1 = biomeH - 1; fz = 0.0; }\n" +
               "        int nf = " + this.registeredOrdinal + ";\n" +
               "        double v00 = rtf_data[nf * biomeW * biomeH + ix0 * biomeH + iz0];\n" +
               "        double v10 = rtf_data[nf * biomeW * biomeH + ix1 * biomeH + iz0];\n" +
               "        double v01 = rtf_data[nf * biomeW * biomeH + ix0 * biomeH + iz1];\n" +
               "        double v11 = rtf_data[nf * biomeW * biomeH + ix1 * biomeH + iz1];\n" +
               "        double top = v00 * (1.0 - fx) + v10 * fx;\n" +
               "        double bot = v01 * (1.0 - fx) + v11 * fx;\n" +
               "        return top * (1.0 - fz) + bot * fz;\n" +
               "    }\n" +
               "}\n" +
               "return 0.0;\n";
    }
}
