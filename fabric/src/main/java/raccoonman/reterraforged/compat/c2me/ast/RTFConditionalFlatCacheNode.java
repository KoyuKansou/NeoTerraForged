package raccoonman.reterraforged.compat.c2me.ast;

import com.ishland.c2me.opts.dfc.common.ast.misc.DelegateNode;
import com.ishland.c2me.opts.dfc.common.gen.opencl.OpenCLGen;
import raccoonman.reterraforged.world.worldgen.densityfunction.ConditionalFlatCache;

public class RTFConditionalFlatCacheNode extends DelegateNode {

    public RTFConditionalFlatCacheNode(ConditionalFlatCache.Cache cache) {
        super(cache);
    }

    @Override
    public String doCLGen(OpenCLGen.Context context) {
        return "return 0.0;\n";
    }
}
