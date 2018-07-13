package com.mdiai.seckill.exception;

import com.mdiai.seckill.common.codemsg.CodeMsg;

/**
 * @author Kevin.Liu
 * @Date create in 2018/7/9  9:57
 * @Description 全局业务异常
 */
public class GlodleException extends RuntimeException {

    private static final long serialVersionUID = -2923175252810143645L;

    private CodeMsg codeMsg;

    public GlodleException(CodeMsg codeMsg) {
        super(codeMsg.toString());
        this.codeMsg = codeMsg;
    }

    public CodeMsg getCodeMsg() {
        return codeMsg;
    }
}
