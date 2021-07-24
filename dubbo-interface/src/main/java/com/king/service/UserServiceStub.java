package com.king.service;

import com.king.dto.UserAddress;
import org.springframework.util.StringUtils;

import java.util.List;

public class UserServiceStub implements UserService{

    private final UserService userService;

    public UserServiceStub(UserService userService){
        this.userService = userService;
    }


    @Override
    public List<UserAddress> getUserAddressList(String userId) {
        System.out.println("此处走了本地存根，根据验证判断，再是否调远程服务");
        if (!StringUtils.isEmpty(userId)){
            return userService.getUserAddressList(userId);
        }
        return null;
    }
}
