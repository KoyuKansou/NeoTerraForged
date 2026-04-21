package raccoonman.reterraforged.compat.c2me.ast;

import com.ishland.c2me.opts.dfc.common.ast.AstNode;
import com.ishland.c2me.opts.dfc.common.ast.McToAst;
import com.ishland.c2me.opts.dfc.common.ast.misc.DelegateNode;
import com.ishland.c2me.opts.dfc.common.gen.meta.ValuesMethodDefD;
import com.ishland.c2me.opts.dfc.common.gen.opencl.OpenCLGen;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.level.levelgen.DensityFunction;
import raccoonman.reterraforged.world.worldgen.densityfunction.LinearSplineFunction;

import java.util.ArrayList;
import java.util.List;

public class RTFLinearSplineFunctionNode extends DelegateNode {
    private final AstNode inputNode;
    private final List<Double> thresholds;
    private final List<AstNode> valueNodes;

    public RTFLinearSplineFunctionNode(LinearSplineFunction function) {
        super(function);
        this.inputNode = McToAst.toAst(function.input());
        this.thresholds = new ArrayList<>();
        this.valueNodes = new ArrayList<>();
        
        for (Pair<Double, DensityFunction> point : function.points()) {
            this.thresholds.add(point.getFirst());
            this.valueNodes.add(McToAst.toAst(point.getSecond()));
        }
    }

    @Override
    public String doCLGen(OpenCLGen.Context context) {
        ValuesMethodDefD inputMethod = context.newMethod(this.inputNode);
        
        StringBuilder sb = new StringBuilder();
        sb.append("double input = ").append(context.callDelegate(inputMethod)).append(";\n");
        
        int n = this.thresholds.size();
        if (n == 0) {
            return "return 0.0;\n";
        }
        
        // if input <= first, return first value
        ValuesMethodDefD firstValMethod = context.newMethod(this.valueNodes.get(0));
        sb.append("if (input <= ").append(this.thresholds.get(0)).append(") return ").append(context.callDelegate(firstValMethod)).append(";\n");
        
        // if input >= last, return last value
        ValuesMethodDefD lastValMethod = context.newMethod(this.valueNodes.get(n - 1));
        sb.append("if (input >= ").append(this.thresholds.get(n - 1)).append(") return ").append(context.callDelegate(lastValMethod)).append(";\n");
        
        for (int i = 0; i < n - 1; i++) {
            double min = this.thresholds.get(i);
            double max = this.thresholds.get(i + 1);
            ValuesMethodDefD fromMethod = context.newMethod(this.valueNodes.get(i));
            ValuesMethodDefD toMethod = context.newMethod(this.valueNodes.get(i + 1));
            
            sb.append("if (input < ").append(max).append(") {\n");
            sb.append("    double from = ").append(context.callDelegate(fromMethod)).append(";\n");
            sb.append("    double to = ").append(context.callDelegate(toMethod)).append(";\n");
            
            //what the fuck is this claude ?
            sb.append("    double lerp = (input - ").append(min).append(") / (").append(max).append(" - ").append(min).append(");\n");
            sb.append("    if (lerp < 0.0) lerp = 0.0;\n");
            sb.append("    if (lerp > 1.0) lerp = 1.0;\n");

            sb.append("    return from + lerp * (to - from);\n");
            sb.append("}\n");
        }
        
        sb.append("return 0.0;\n");
        return sb.toString();
    }
}
