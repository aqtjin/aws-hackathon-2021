package com.hackathon.sales.service;

import com.hackathon.sales.error.BusinessException;
import com.hackathon.sales.service.model.UserModel;

public interface UserService {
    UserModel getUserById(Integer id);
    void register(UserModel userModel) throws BusinessException;

    /*
    telephone:用户注册手机
    password:用户加密后的密码
     */
    UserModel validateLogin(String telephone, String encryptPassword) throws BusinessException;
}
