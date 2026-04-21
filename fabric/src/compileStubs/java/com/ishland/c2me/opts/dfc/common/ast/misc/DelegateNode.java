package com.ishland.c2me.opts.dfc.common.ast.misc;

import com.ishland.c2me.opts.dfc.common.ast.AstNode;
import com.ishland.c2me.opts.dfc.common.ast.AstTransformer;
import com.ishland.c2me.opts.dfc.common.ast.EvalType;
import com.ishland.c2me.opts.dfc.common.gen.jvm.BytecodeGen;
import com.ishland.c2me.opts.dfc.common.gen.opencl.OpenCLGen;
import net.minecraft.world.level.levelgen.DensityFunction;
import org.objectweb.asm.commons.InstructionAdapter;

public class DelegateNode implements AstNode {
    public DelegateNode(DensityFunction densityFunction) {}

    @Override
    public double evalSingle(int x, int y, int z, EvalType type) { return 0; }

    @Override
    public void evalMulti(double[] res, int[] x, int[] y, int[] z, EvalType type) {}

    @Override
    public AstNode[] getChildren() { return new AstNode[0]; }

    @Override
    public AstNode transform(AstTransformer transformer) { return null; }

    @Override
    public void doBytecodeGenSingle(BytecodeGen.Context context, InstructionAdapter m, BytecodeGen.Context.LocalVarConsumer localVarConsumer) {}

    @Override
    public void doBytecodeGenMulti(BytecodeGen.Context context, InstructionAdapter m, BytecodeGen.Context.LocalVarConsumer localVarConsumer) {}

    @Override
    public String doCLGen(OpenCLGen.Context context) { return null; }

    @Override
    public boolean relaxedEquals(AstNode o) { return false; }

    @Override
    public int relaxedHashCode() { return 0; }
}
