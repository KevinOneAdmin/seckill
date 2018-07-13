package com.mdiai.seckill.dao;

import com.mdiai.seckill.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author Kevin.Liu
 * @Date create in 2018/7/5  14:10
 * @Description
 */
@Mapper
public interface UserDAO {
    @Select("select * from user where id = #{id}")
    User getById(@Param("id") int id);

    @Insert("insert into user (id,name) values (#{id},#{name})")
    int insert(User user);
}
