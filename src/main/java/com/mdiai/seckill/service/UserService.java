package com.mdiai.seckill.service;

import com.mdiai.seckill.domain.User;
import com.mdiai.seckill.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Kevin.Liu
 * @Date create in 2018/7/5  14:18
 * @Description
 */
@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;


    public User getUser(Integer id){
        return userDAO.getById(id);
    }

    @Transactional
    public boolean  tx() {
        User user = new User();
        user.setId(2);
        user.setName("222222");
        userDAO.insert(user);
        User user2 = new User();
        user2.setId(1);
        user2.setName("11111");
        userDAO.insert(user2);
        return true;
    }
}
