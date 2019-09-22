package com.king.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.king.service.ProviderService;

import java.util.Date;

/**
 * Created by ZHUYONGQIANG on 20190922
 * 配上版本号1.0.0说明向zookeeper注册的是版本为1.0.0的TestService接口，超时时长为3000ms等信息
 */
@Service(version="1.0.0",timeout = 3000)
public class ProviderServiceImpl implements ProviderService{
    @Override
    public String sayHello(String name) {
        return new Date()+"Hello，我是生产者过来的，消费者说"+name;
    }
}
