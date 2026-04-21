package raccoonman.reterraforged.compat.c2me.mixin;

import com.ishland.c2me.opts.dfc.common.ast.AstNode;
import com.ishland.c2me.opts.dfc.common.ast.McToAst;
import net.minecraft.world.level.levelgen.DensityFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import raccoonman.reterraforged.compat.c2me.ast.*;
import raccoonman.reterraforged.world.worldgen.densityfunction.CellSampler;
import raccoonman.reterraforged.world.worldgen.densityfunction.ClampToNearestUnit;
import raccoonman.reterraforged.world.worldgen.densityfunction.ConditionalFlatCache;
import raccoonman.reterraforged.world.worldgen.densityfunction.LinearSplineFunction;
import raccoonman.reterraforged.world.worldgen.densityfunction.NoiseFunction;

@Mixin(McToAst.class)
public class MixinMcToAst {
    @Inject(method = "toAst", at = @At("HEAD"), cancellable = true)
    private static void interceptRTFDensityFunctions(DensityFunction df, CallbackInfoReturnable<AstNode> cir) {
        if (df instanceof CellSampler sampler) {
            cir.setReturnValue(new RTFCellSamplerNode(sampler));
        } else if (df instanceof CellSampler.CacheChunk cached) {
            cir.setReturnValue(new RTFCellSamplerNode(cached));
        } else if (df instanceof NoiseFunction noise) {
            cir.setReturnValue(new RTFNoiseFunctionNode(noise));
        } else if (df instanceof ClampToNearestUnit clamp) {
            cir.setReturnValue(new RTFClampToNearestUnitNode(clamp));
        } else if (df instanceof LinearSplineFunction spline) {
            cir.setReturnValue(new RTFLinearSplineFunctionNode(spline));
        } else if (df instanceof ConditionalFlatCache cache) {
            cir.setReturnValue(McToAst.toAst(cache.function()));
        } else if (df instanceof ConditionalFlatCache.Cache cache) {
            cir.setReturnValue(new RTFConditionalFlatCacheNode(cache));
        }
    }
}
