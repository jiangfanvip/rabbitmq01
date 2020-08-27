package com.jiangfan.routing;

import com.jiangfan.util.ConnectionUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Auther: 江帆
 * @Date: 2020/7/16
 * @Description: com.jiangfan.ps
 * @version: 1.0
 * 路由模式
 */
public class Producer {
    public final static String DIRECT_EXCHANGE = "direct_exchange";  //交换机名称

    static final String QUEUE_INSERT = "routing-queue1";  //队列名insert
    static final String QUEUE_UPDATE = "routing-queue2";  //队列名update

    public static void main(String[] args) throws IOException, TimeoutException {
        //1创建连接工厂

        //2 创建连接
        Connection connection = ConnectionUtils.getConnection();
        //3创建渠道  一个连接可以有多个渠道
        Channel channel = connection.createChannel();

        //声明交换机   参数1  交换机名称 参数2  交换机类型
        channel.exchangeDeclare(DIRECT_EXCHANGE, BuiltinExchangeType.DIRECT);
        //4 声明队列
        // 参数1 队列名
        // 参数2  是否持久化队列  rabbitMQ服务停掉了 true的话就会持久化这个队列，false则不会
        // 参数3 设置是否排他 （是否这个渠道独自占用本次连接）   一个连接可以有多个渠道
        // 参数4  设置是否自动删除队列 至少一个消费者连接到这个队列时 之后所有消费者都断开这个对接时，则队列自动删除
        // 参数5  有带有其它参数
        channel.queueDeclare(QUEUE_INSERT, true, false, false, null);
        channel.queueDeclare(QUEUE_UPDATE, true, false, false, null);

        //队列绑定交换机  //参数1  绑定队列名   参数2 绑定交换机名  参数3  所绑定的routing key
        //QUEUE_INSERT队列绑定到DIRECT_EXCHANG 交换机上，并指定routing key为insert
        channel.queueBind(QUEUE_INSERT, DIRECT_EXCHANGE, "insert");
        channel.queueBind(QUEUE_UPDATE, DIRECT_EXCHANGE, "update");

        //5 发送消息
        //参数1 交换机名称 如果为""  则默认交换机
        //参数2  routing key  路由key
        //参数3  消息其它属性
        //参数4  消息内容
        String msg = "新增加消息路由key模式...路由key====insert";
        //发消息时候指定exchange和routing key ,然后就会在DIRECT_EXCHANGE交换机下根据insert路由键去找对应的队列，再把消息发给这个队列
        channel.basicPublish(DIRECT_EXCHANGE, "insert", null, msg.getBytes());
        System.out.println("已发送消息===" + msg);


        msg = "新增加消息路由key模式...路由key====update";
        channel.basicPublish(DIRECT_EXCHANGE, "update", null, msg.getBytes());
        System.out.println("已发送消息===" + msg);

        //6 关闭资源
        channel.close();
        connection.close();
    }


}
