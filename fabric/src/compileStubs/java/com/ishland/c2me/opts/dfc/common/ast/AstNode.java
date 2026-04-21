package com.ishland.c2me.opts.dfc.common.ast;

import com.ishland.c2me.opts.dfc.common.gen.jvm.BytecodeGen;
import com.ishland.c2me.opts.dfc.common.gen.opencl.OpenCLGen;
import org.objectweb.asm.commons.InstructionAdapter;

public interface AstNode {
    double evalSingle(int x, int y, int z, EvalType type);

    void evalMulti(double[] res, int[] x, int[] y, int[] z, EvalType type);

    AstNode[] getChildren();

    AstNode transform(AstTransformer transformer);

    void doBytecodeGenSingle(BytecodeGen.Context context, InstructionAdapter m,
            BytecodeGen.Context.LocalVarConsumer localVarConsumer);

    void doBytecodeGenMulti(BytecodeGen.Context context, InstructionAdapter m,
            BytecodeGen.Context.LocalVarConsumer localVarConsumer);

    String doCLGen(OpenCLGen.Context context);

    boolean relaxedEquals(AstNode o);

    int relaxedHashCode();
}

// ts requires a whole implementation of astnode