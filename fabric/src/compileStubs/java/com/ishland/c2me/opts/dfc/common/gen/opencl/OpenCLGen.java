package com.ishland.c2me.opts.dfc.common.gen.opencl;

import com.ishland.c2me.opts.dfc.common.ast.AstNode;
import com.ishland.c2me.opts.dfc.common.gen.meta.ValuesMethodDefD;

public class OpenCLGen {
    public static class Context {
        public ValuesMethodDefD newMethod(AstNode node) { return null; }
        public String callDelegate(ValuesMethodDefD method) { return null; }
        public int allocGlobalDynamicData(Object data) { return 0; }
        public int getGlobalDynamicDataOffset(Object data) { return 0; }
    }
}
