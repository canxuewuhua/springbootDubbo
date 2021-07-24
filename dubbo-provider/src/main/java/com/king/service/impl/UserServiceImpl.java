package com.king.service.impl;

import java.util.Arrays;
import java.util.List;

import com.king.dto.UserAddress;
import com.king.service.UserService;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
/**
 * Created by ZHUYONGQIANG on 20190922
 * 配上版本号1.0.0说明向zookeeper注册的是版本为1.0.0的TestService接口，超时时长为3000ms等信息
 */

@Service(version="1.0.0",timeout = 3000)//暴露服务
@Component
public class UserServiceImpl implements UserService {

	@HystrixCommand
	@Override
	public List<UserAddress> getUserAddressList(String userId) {
		// TODO Auto-generated method stub
		System.out.println("UserServiceImpl..3.....");
		UserAddress address1 = new UserAddress(1, "北京市昌平区宏福科技园综合楼3层", "1", "李老师", "010-56253825", "Y");
		UserAddress address2 = new UserAddress(2, "深圳市宝安区西部硅谷大厦B座3层（深圳分校）", "1", "王老师", "010-56253825", "N");
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		if(Math.random()>0.5) {
//			throw new RuntimeException();
//		}
		return Arrays.asList(address1,address2);
	}

}
