package com.mdiai.seckill.common.result;

import com.mdiai.seckill.common.codemsg.CodeMsg;

/**
 * @author Kevin.Liu
 * @Date create in 2018/7/5  14:23
 * @Description
 */
public class Result<T> {

    private int code;

    private String msg;

    private T data;

    private Result(T data) {
        this.code = 0;
        this.msg = "success";
        this.data = data;
    }

    private Result(CodeMsg cmg) {
        if (null == cmg) {
            return;
        }
        this.code = cmg.getCode();
        this.msg = cmg.getMsg();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    public static <T> Result<T> success(T data) {
        return new Result<T>(data);
    }

    public static <T> Result<T> eorror(CodeMsg cmg) {
        return new Result<T>(cmg);
    }
}
