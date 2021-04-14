package com.hackathon.sales.service;

import com.hackathon.sales.error.BusinessException;
import com.hackathon.sales.service.model.UserModel;

public interface UserService {
    UserModel getUserById(Integer id);
    void register(UserModel userModel) throws BusinessException;
}
