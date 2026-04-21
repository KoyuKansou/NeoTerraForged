package raccoonman.reterraforged.compat.c2me.ast;

import com.ishland.c2me.opts.dfc.common.ast.AstNode;
import com.ishland.c2me.opts.dfc.common.ast.McToAst;
import com.ishland.c2me.opts.dfc.common.ast.misc.DelegateNode;
import com.ishland.c2me.opts.dfc.common.gen.meta.ValuesMethodDefD;
import com.ishland.c2me.opts.dfc.common.gen.opencl.OpenCLGen;
import net.minecraft.world.level.levelgen.DensityFunction;
import raccoonman.reterraforged.world.worldgen.densityfunction.ClampToNearestUnit;

public class RTFClampToNearestUnitNode extends DelegateNode {
    private final AstNode delegateAst;
    private final int resolution;

    public RTFClampToNearestUnitNode(ClampToNearestUnit densityFunction) {
        super(densityFunction);
        this.delegateAst = McToAst.toAst(densityFunction.function());
        this.resolution = densityFunction.resolution();
    }

    @Override
    public String doCLGen(OpenCLGen.Context context) {
        ValuesMethodDefD method = context.newMethod(this.delegateAst);
        return "double input = " + context.callDelegate(method) + ";\n" +
               "float scaled = (int)(input * " + this.resolution + ") + 1;\n" +
               "return ((double)scaled) / " + this.resolution + ";\n";
    }
}
