package com.mdiai.seckill.dao;

import com.mdiai.seckill.domain.SeckillUser;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author Kevin.Liu
 * @Date create in 2018/7/6  17:40
 * @Description
 */
@Mapper
public interface SeckillUserDAO {

    @Select("select * from seckill_user where id = #{id}")
    SeckillUser getByID(@Param("id") long id);

    @Insert("<script> insert into seckill_user(" +
             "id\n" +
            ",nickname\n" +
            ",password\n" +
            ",salt\n" +
            ",register_data\n" +
            ",last_login_data\n" +
            ",login_count) values"+
            " <foreach collection ='users' item='user' separator =','>\n" +
            "         (#{user.id}, #{user.nickname}, #{user.password},#{user.salt},#{user.registerData},#{user.lastLoginData},#{user.loginCount})\n" +
            " </foreach >"+
            "</script>")
    int batchInsert(@Param("users") List<SeckillUser> users);

    @Select("select * from seckill_user")
    List<SeckillUser> getSeckillUserList();

    @Update("update seckill_user set password=#{pwd} where id=#{id}")
    int updatePwd(@Param("id") long id, @Param("pwd") String pwd);
}
