package raccoonman.reterraforged.world.worldgen;


//Holds a reference to the current GeneratorContext for use across modules.
//GeneratorContext is world-global, there is only one active per world instance.
//Set from MixinRandomState when the context is first created,
//read by the C2ME OpenCL compat layer during buffer fill.
public class RTFWorldLookupHolder {
    private static volatile GeneratorContext context;

    public static void set(GeneratorContext ctx) {
        context = ctx;
    }

    public static GeneratorContext get() {
        return context;
    }

    public static void clear() {
        context = null;
    }
}
