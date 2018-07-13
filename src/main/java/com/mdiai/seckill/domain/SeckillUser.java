package com.mdiai.seckill.domain;

import java.util.Date;

/**
 * @author Kevin.Liu
 * @Date create in 2018/7/6  17:37
 * @Description 秒杀用户
 */
public class SeckillUser {

    private Long id;
    private String nickname;
    private String password;
    private String salt;
    private String head;
    private Date registerData;
    private Date lastLoginData;
    private Integer loginCount=0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public Date getRegisterData() {
        return registerData;
    }

    public void setRegisterData(Date registerData) {
        this.registerData = registerData;
    }

    public Date getLastLoginData() {
        return lastLoginData;
    }

    public void setLastLoginData(Date lastLoginData) {
        this.lastLoginData = lastLoginData;
    }

    public Integer getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SeckillUser{");
        sb.append("id=").append(id);
        sb.append(", nickname='").append(nickname).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", salt='").append(salt).append('\'');
        sb.append(", head='").append(head).append('\'');
        sb.append(", registerData=").append(registerData);
        sb.append(", lastLoginData=").append(lastLoginData);
        sb.append(", loginCount=").append(loginCount);
        sb.append('}');
        return sb.toString();
    }
}
