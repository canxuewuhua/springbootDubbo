package com.king.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.king.service.ProviderService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ZHUYONGQIANG on 2018/6/29.
 */
@RestController
@RequestMapping("/project")
public class ConsumerController {
    @Reference(version = "1.0.0")
    public ProviderService consumerService;
    @RequestMapping("/say")
    public String sayHello(String name){
        String str = consumerService.sayHello(name);
        return str;
    }

    @RequestMapping("/info")
    public String sayInfo(String name){
        String str = "Hello";
        return str;
    }
}
