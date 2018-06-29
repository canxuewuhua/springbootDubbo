package com.king.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.king.service.ProviderService;

import java.util.Date;

/**
 * Created by ZHUYONGQIANG on 2018/6/29.
 */
@Service(version="1.0.0")
public class ProviderServiceImpl implements ProviderService{
    @Override
    public String sayHello(String name) {
        return new Date()+"Hello"+name;
    }
}
