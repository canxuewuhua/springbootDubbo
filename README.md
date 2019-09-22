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
