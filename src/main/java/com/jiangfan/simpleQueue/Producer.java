package com.jiangfan.simpleQueue;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Auther: 江帆
 * @Date: 2020/7/12
 * @Description: com.jiangfan.simpleQueue
 * @version: 1.0
 * 生产者
 */
public class Producer {
    static final String QUEUE_NAME = "simple_name";  //队列名

    //在设置连接工厂的时候，如果没有指定连接参数则会有默认值。一般我们不同业务的队列会在不同虚拟主机中
    public static void main(String[] args) throws IOException, TimeoutException {
        //1创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("106.52.248.12"); //默认主机 localhost
        connectionFactory.setPort(5672);  //默认端口  5672 是通信端口 15672 是管理端端口
        connectionFactory.setVirtualHost("/test01"); //虚拟主机 默认/
        connectionFactory.setConnectionTimeout(5000); //超时时间
        connectionFactory.setUsername("jiangfan");   //用户户名 默认guest  密码 guest
        connectionFactory.setPassword("123456");
        //2 创建连接
        Connection connection = connectionFactory.newConnection();
        //3创建渠道  一个连接可以有多个渠道
        Channel channel = connection.createChannel();
        //4 声明队列
        // 参数1 队列名
        // 参数2  是否持久化队列  rabbitMQ服务停掉了 true的话就会持久化这个队列，false则不会
        // 参数3 设置是否排他 （是否这个渠道独自占用本次连接）   一个连接可以有多个渠道
        // 参数4  设置是否自动删除队列 至少一个消费者连接到这个队列时 之后所有消费者都断开这个对接时，则队列自动删除
        // 参数5  有带有其它参数
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        //5 发送消息
        //参数1 交换机名称 如果为""  则默认交换机
        //参数2  路口key  简单队列模式可以使用队列名称
        //参数3  消息其它属性
        //参数4  消息内容
        channel.basicPublish("", QUEUE_NAME, null, "hello小兔子22222".getBytes());
        System.out.println("已发送消息");
        //6 关闭资源
        channel.close();
        connectionFactory.clone();


    }
}
