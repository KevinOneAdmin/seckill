package com.mdiai.seckill.common.codemsg;

/**
 * @author Kevin.Liu
 * @Date create in 2018/7/5  14:33
 * @Description
 */
public class CodeMsg {

    private int code;

    private String msg;

    public CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    //通用错误码
    public static CodeMsg SUCCESS = new CodeMsg(0,"success");
    public static CodeMsg SERVICE_ERROR = new CodeMsg(500100,"服务端异常");
    public static CodeMsg BIND_ERROR = new CodeMsg(500101,"参数校验异常:%s");
    public static CodeMsg USER_NOT_LOGIN = new CodeMsg(500102,"用户尚未登录");
    public static CodeMsg REQUEST_ILLEGAL = new CodeMsg(500103,"请求非法");
    public static CodeMsg ACCESS_LIMIT_REACHED = new CodeMsg(500104,"访问太频繁");
    //登录模块 5002xx
    public static CodeMsg SESSION_ERROR = new CodeMsg(500201,"服务端异常");
    public static CodeMsg PASSSWORD_EMPTY = new CodeMsg(500202,"密码不能为空");
    public static CodeMsg MOBILE_EMPTY = new CodeMsg(500203,"手机号不能为空");
    public static CodeMsg MOBILE_ERROR = new CodeMsg(500204,"手机号格式错误");
    public static CodeMsg MOBILE_NOT_EXIST = new CodeMsg(500205,"手机号不存在");
    public static CodeMsg PASSSWORD_ERROR = new CodeMsg(500206,"密码错误");
    public static CodeMsg PASSSWORD_UPDATE_FAIL = new CodeMsg(500208,"更新密码失败");
   //秒杀模块 5003xx
   public static CodeMsg SECKILL_OVER = new CodeMsg(500301,"商品已经秒杀完毕");
   public static CodeMsg REPEATE_SECKILL = new CodeMsg(500302,"请勿重复秒杀");
   public static CodeMsg INVENTORY_REDUCTION_FAIL= new CodeMsg(500303,"减库存失败");
   public static CodeMsg SECKILL_FAIL= new CodeMsg(500304,"秒杀失败");



    public CodeMsg fillArgs(Object ... args){
       int code = this.code;
        String msg = String.format(this.msg, args);
        return new CodeMsg(code,msg);
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

    @Override
    public String toString() {
        return "codemsg{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
