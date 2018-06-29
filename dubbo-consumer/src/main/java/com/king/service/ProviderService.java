package com.king.service;

/**
 * Created by ZHUYONGQIANG on 2018/6/29.
 * 这里的service接口要和dubbo的服务的提供者接口名称一致
 */
public interface ProviderService {
    String sayHello(String name);
}
