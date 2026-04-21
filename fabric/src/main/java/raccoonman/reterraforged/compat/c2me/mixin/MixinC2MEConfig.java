package raccoonman.reterraforged.compat.c2me.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "com.ishland.c2me.opts.accel.opencl.common.Config", remap = false)
public class MixinC2MEConfig {

    @Mutable
    @Shadow
    public static boolean allowIncompatibilityFallback;

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void onClInit(CallbackInfo ci) {
        // this basically dead code since c2me ocl already fully implemented

        allowIncompatibilityFallback = true;
    }
}
