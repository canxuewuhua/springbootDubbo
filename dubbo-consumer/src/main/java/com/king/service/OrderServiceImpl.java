package com.king.service;

import java.util.Arrays;
import java.util.List;

import com.king.dto.UserAddress;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;

/**
 * 1、将服务提供者注册到注册中心（暴露服务）
 * 		1）、导入dubbo依赖（2.6.2）\操作zookeeper的客户端(curator)
 * 		2）、配置服务提供者
 * 
 * 2、让服务消费者去注册中心订阅服务提供者的服务地址
 * @author lfy
 *
 */
@Service
public class OrderServiceImpl implements OrderService {

	/**
	 * 此处timeout为方法上的 优先 再使用配置文件中的超时时间  精确优先
	 * 消费者的超时时间设置优先 提供者的超时时间次之
	 * loadbalance random roundrobin leastactive
	 */
	//@Autowired
	@Reference(version = "1.0.0",loadbalance="random",timeout=3000,retries = 3, stub = "com.king.service.UserServiceStub")
//	@Reference(version = "1.0.0",timeout=1000)
	//@Reference(url="127.0.0.1:20882") //dubbo直连 服务提供方的地址 绕过zookeeper
	UserService userService;
	
	@HystrixCommand(fallbackMethod="hello")
	@Override
	public List<UserAddress> initOrder(String userId) {
		// TODO Auto-generated method stub
		System.out.println("用户id："+userId);
		//1、查询用户的收货地址
		List<UserAddress> addressList = userService.getUserAddressList(userId);
		return addressList;
	}
	
	
	public List<UserAddress> hello(String userId) {
		// TODO Auto-generated method stub
	
		return Arrays.asList(new UserAddress(10, "测试地址", "1", "测试", "测试", "Y"));
	}
	

}
