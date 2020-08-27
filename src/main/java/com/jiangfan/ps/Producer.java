package com.jiangfan.ps;

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
 * 发布订阅模式
 */
public class Producer {
    public final static String exchange1 = "exchange1";

    static final String QUEUE_NAME1 = "ps-queue1";  //队列名
    static final String QUEUE_NAME2 = "ps-queue2";  //队列名

    public static void main(String[] args) throws IOException, TimeoutException {
        //1创建连接工厂

        //2 创建连接
        Connection connection = ConnectionUtils.getConnection();
        //3创建渠道  一个连接可以有多个渠道
        Channel channel = connection.createChannel();

        //声明交换机   参数1  交换机名称 参数2  交换机类型
        channel.exchangeDeclare(exchange1, BuiltinExchangeType.FANOUT);
        //4 声明队列
        // 参数1 队列名
        // 参数2  是否持久化队列  rabbitMQ服务停掉了 true的话就会持久化这个队列，false则不会
        // 参数3 设置是否排他 （是否这个渠道独自占用本次连接）   一个连接可以有多个渠道
        // 参数4  设置是否自动删除队列 至少一个消费者连接到这个队列时 之后所有消费者都断开这个对接时，则队列自动删除
        // 参数5  有带有其它参数
        channel.queueDeclare(QUEUE_NAME1, true, false, false, null);
        channel.queueDeclare(QUEUE_NAME2, true, false, false, null);

        //队列绑定交换机
        channel.queueBind(QUEUE_NAME1, exchange1, "");
        channel.queueBind(QUEUE_NAME2, exchange1, "");

        //5 发送消息
        //参数1 交换机名称 如果为""  则默认交换机
        //参数2  路口key  简单队列模式可以使用队列名称
        //参数3  消息其它属性
        //参数4  消息内容
        for (int i = 0; i < 10; i++) {    //发送30条消息
            String msg = "hello大哥" + i;
            channel.basicPublish(exchange1, "", null, msg.getBytes());
            System.out.println("已发送消息===" + msg);
        }
        //6 关闭资源
        channel.close();
        connection.close();
    }


}
