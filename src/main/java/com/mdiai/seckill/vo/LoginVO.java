package com.mdiai.seckill.vo;

import com.mdiai.seckill.common.validator.IsMobile;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author Kevin.Liu
 * @Date create in 2018/7/6  16:22
 * @Description
 */
public class LoginVO {

    @NotBlank
    @IsMobile
    private String mobile;

    @NotBlank
    @Length(min = 32,max = 32)
    private String password;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginVO{" +
                "mobile='" + mobile + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
