 springbootDubbo

 项目启动步骤
# 1、首先application.properties文件中的dubbo的注册地址：address配置在linux上
#    如：linux上 /usr/local/app/zookeeper-3.4.12/bin 
#      启动命令是：./zkServer.sh start
#      查看zk的状态命令：./zkServer.sh status
#      关闭zk的命令：./zkServer.sh stop
#   备注：在Linux上安装并启动zookeeper
#         参考网址：https://blog.csdn.net/huangyuhuangyu/article/details/80418674 
#   dubbo所需要的zookeeper安装在192.168.199.239:2181机器上  
#   路径为/usr/local/app/dubbo-kafka/zookeeper-3.4.9 ，启动zookeeper后    Mode: standalone

这一次没有启动成功是因为spring.dubbo.scan 没有配置对路径，
#生产者里面配置service的地址  消费者配置在controller上

dubbo依赖于zookeeper，所以在linux上虚拟机上先安装上zookeeper，不必安装zookeeper集群，安装一个zookeeper即可，只需要修改zookeeper中的zoo.cfg中的日志路径

**总结（这里是dubbo服务的关键）**
生产者提供service（@Service(version="1.0.0",timeout = 3000)）的实现方式，里面有具体的处理逻辑，比如说在一个服务中调了该服务的一个方法
在消费者的服务中，通过使用@Reference注解引入消费者的Service，就可以用。
此处要注意了生产者和消费者的这两个注解都必须是alibaba的注解才可以

注意：dubbo相关的注解只需要在服务提供者的服务的pom文件即可

Provider中只需配置application.properties文件，文件内容如下：
# server.port=8011
#dubbo服务发布者
#spring.dubbo.application.name=provider
#spring.dubbo.registry.address=zookeeper://192.168.83.150:2181
# spring.dubbo.protocol.name=dubbo
#用dubbo协议在20881端口暴露服务
# spring.dubbo.protocol.port=20881
# spring.dubbo.scan=com.king
Consumer中配置application.properties文件，文件内容如下：
# server.port=8012

# spring.dubbo.application.name=consumer
# spring.dubbo.registry.address=zookeeper://192.168.83.150:2181
# spring.dubbo.scan=com.king
配置dubbo-admin，dubbo的管理后台
# 注意：在linux下的tomcat下运行dubbo-admin.war的时候，可能会提示jdk版本在jdk1.7，需卸载1.8，安装1.7版本。
# 之后输入地址栏：192.168.83.150:8080/dubbo-admin
# 可以查看dubbo服务的提供者和消费者
提供者和消费者的代码注意：
# 提供者暴露的接口的包的路径和类名---，在消费者端，必须要一致，否者是连接不同的，管理后台会提示“没有提供者”。
# 提供者写接口和接口的实现类；
# 消费者也要写同路径下的相同名的接口，不必写实现类。
# 提供者的接口需引入@Service（alibaba的包）；消费者的接口需引入@Reference(加版本号)。

# 如果从github上下载下来，出现类变成了橙色，启动类不能启动，原因可能是项目没有添加maven管理
# 选中pom.xml文件，右键Add to Maven,添加maven管理


Dubbo 使用的是 RPC 通信，而 Spring Cloud 使用的是 HTTP RESTFul 方式
Dubbo 默认使用 Netty 框架

--------------------------2021年4月15号----------------------------------
# 多版本
  当一个接口实现 出现不兼容升级时 可以用版本号过渡 版本号不同的服务相互间不引用
  可以按照以下的步骤进行版本迁移
  0、在低压力时间段 先升级一半提供者为新版本
  1、再将所有消费者升级为新版本
  2、然后将剩下的一半提供者升级为新版本
  
  具体做法是我们可以在服务提供方的@Service(version="1.0.0",timeout = 3000) 里面指定版本号
  而在我们的消费方的@Reference(version = "1.0.0",loadbalance="random",timeout=1000) 上指定版本号
  
  这样就可以实现灰度发布 根据版本号 一部分用户可以使用老的版本方法 一部分用户可以使用新的版本方法
  
# 本地存根 
   什么是本地存根？？
   远程服务后，客户端通常只剩下接口，而实现全在服务器端 但提供方有些时候想在客户端也执行部分逻辑，比如：做ThreadLocal缓存
   提前验证参数，调用失败后伪造容错数据等等 此时就需要在API中带上Stub 客户端生成Proxy实例
   会把Proxy通过构造函数传给Stub 然后把Stub暴露给用户 Stub可以决定要不要去调Proxy
   
   本地存根的意思是说 在调用远程暴露的服务的时候先做一些验证，如果验证失败了走我们定义的方法逻辑  如果验证通过了 调用远程服务
   
# Springboot与dubbo整合的三种方式
  1、导入dubbo-starter，在application.properties配置属性 使用@Service【暴露服务】 使用@Reference【引用服务】
  2、保留dubbo xml配置文件
     导入dubbo-starter，使用@ImportResource导入dubbo的配置文件
  3、使用注解API的方式
     将每一个组件手动创建到容器中 让dubbo来扫描其他的组件
     
# 高可用
  ## zookeeper宕机与dubbo直连
  现象是：zookeeper注册中心宕机，还可以消费dubbo暴露的服务
  原因： 1、监控中心宕机不影响使用 只是丢失部分采样数据
        2、数据库宕机后，注册中心仍能通过缓存提供服务列表查询，但不能注册新服务
        3、注册中心对等集群 任意一台宕机后 将自动切换到另一台
        4、注册中心全部宕机后 服务提供者和服务消费者仍能通过本地缓存通讯
        5、服务提供者无状态 任意一台宕机后 不影响使用
        6、服务提供者全部宕机后，服务消费者应用将无法使用 并无限次重连等待服务提供者恢复
  注：高可用 通过设计  减少系统不能提供服务的时间
  
  现在有这样一种场景：启动一个服务提供者 启动一个服务消费者 此时将zookeeper断开
  服务消费者依然能够消费，因为两者仍然能够通过本地缓存通讯
  ------当然zookeeper全部宕机后，服务消费方可以直连提供方在@Reference上指定地址  这样绕过注册中心，直连提供方依然可以 因为有本地缓存--------
  直连的方式  @Reference(url="127.0.0.1:20882"")

# 集群模式下 dubbo的负载均衡配置
Random LoadBalance 随机负载均衡机制  可以分为随机和基于权重的随机 weight=100 在大量请求情况下
RoundRobin LoadBalance 轮询负载均衡机制 可以分为轮询和基于权重的轮询 第一个调完 第二次就是第二个 依次轮询
LeastActive LoadBalance 最少活跃数均衡机制  在统计上一次的调用时间，它会找一个最少执行时间的，响应时间最快的
ConsistentHash LoadBalance 一致性hash 负载均衡机制  getUser?id=1 getUser?id=2 getUser?id=3 就是参数一样的话找的还是同一台机器

举个例子  比如启动了三个服务提供者  一个消费者
默认的是随机的负载均衡模式 random
1、可以在消费者处设置负载均衡算法
2、可以在提供方设置负载均衡算法
3、也可以在方法上设置负载均衡算法
4、     我们还可以在控制台上进行“倍权” “半权”进行设置 方便快捷

#服务降级
什么是服务降级
当服务器压力剧增的情况下，根据实际业务情况及流量 对一些服务和页面有策略的不处理或
换种简单的方式处理，从而释放服务器资源以保证核心交易正常运作或高效运作

可以通过服务降级功能临时屏蔽某个出错的非关键服务 并定义降级后的返回策略
向注册中心写入动态配置覆盖规则

可以在dubbo的后管页面进行手动的配置  比如在

mock=force:return+null 表示消费方对该服务的方法调用都直接返回null值，不发起远程调用。用来屏蔽不重要服务不可用时对调用方的影响。
不发起远程调用 直接返回为空

mock=fail:return+null 表示消费方对该服务的方法调用在失败后，再返回null值，不抛异常。用来容忍不重要服务不稳定时对调用方的影响。
在bubbo的消费者端 可以对服务进行“屏蔽”和“容错”的设置  设置“屏蔽”后就直接返回null值，不发起远程调用
设置“容错”后，当调用服务出现失败后如果设置了容错就返回null值，如果不设置容错就会抛出异常

# 集群容错
在集群调用失败时，Dubbo提供了多种容错方案 缺省为failover重试
集群容错模式
Failover Cluster
失败自动切换 当出现失败 重试其他服务器。通常用来读操作，但重试带来更长延迟 可通过 retry=“2” 来设置重试次数（不含第一次）
重试次数配置 可以在提供方 可以在消费方 也可以在方法上 失败重试 重试其他服务器，重试会有延迟

Failfast Cluster
快速失败 只发起一次调用 失败立即报错 通常用于非幂等性的写操作 比如新增记录。

# 整合hystrix
Hystrix 旨在通过控制那些访问远程系统 服务和第三方库的节点，从而对延迟和故障提供更强大的容错能力
Hystrix 具备拥有回退机制和断路器功能的线程和信号隔离，请求缓存和请求打包，以及监控和配置等功能

配置spring-cloud-starter-netflix-hystrix
springboot官方提供了对hystrix的集成 直接在pom.xml里加入依赖
然后在Application类上加上@EnableHystrix来启用hystrix starter

比如说一个场景  if(Math.random()>0.5) throw new RuntimeException()
不定期出现异常  那么在方法上 加上注解 @HystrixCommand (提供者暴露的方法上)

需要在提供方 消费方的pom文件中加上 hystrix依赖 同时在两者的启动类上开启断路器

在服务消费者方法上@HystrixCommand(fallbackMethod=“hello”)

再写一个hello方法 作为方法出错时时的备用执行方法
Arrays.asList(new UserAddress(“1”，“测试地址”))

# dubbo的原理
RPC的原理 远程过程调用

rpc call client stub（代理）序列化  socket  服务端反序列化 server stub代理 local call

1、服务消费方client调用以本地调用方式调用服务  2345678 9、服务消费方得到最终结果
RPC框架的目的就是要2-8这些步骤都封装起来 这些细节对用户来说是透明的 不可见的

Netty通信原理
netty是基于NIO的框架 它极大地简化了TCp和UDP套接字服务器等网络编程



--------------------------------------------------------------------------------------------------------