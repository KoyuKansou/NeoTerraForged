package raccoonman.reterraforged.compat.c2me.ast;

import raccoonman.reterraforged.world.worldgen.densityfunction.NoiseFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RTFNoiseFunctionRegistry {

    private static final List<NoiseFunction> registry = new ArrayList<>();

    public static void register(int ordinal, NoiseFunction func) {
        while (registry.size() <= ordinal) {
            registry.add(null);
        }
        registry.set(ordinal, func);
    }

    public static List<NoiseFunction> getAll() {
        return Collections.unmodifiableList(registry);
    }

    public static void reset() {
        registry.clear();
        RTFOpenCLMarkers.resetNoiseFunctionCount();
    }
}
