package com.jiangfan.topic;

import com.jiangfan.util.ConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Auther: 江帆
 * @Date: 2020/7/12
 * @Description: com.jiangfan.simpleQueue
 * @version: 1.0
 */
public class Consumer2 {
    public static void main(String[] args) throws IOException, TimeoutException {
        //获取连接
        Connection connection = ConnectionUtils.getConnection();
        //创建渠道
        Channel channel = connection.createChannel();
        //声明交换机
   //     channel.exchangeDeclare(Producer.exchange1, BuiltinExchangeType.TOPIC);
         //声明队列
      //  channel.queueDeclare(Producer.QUEUE_NAME2, true, false, false, null);


        //队列绑定交换机  //参数1  routing key   参数2  交换机  参数3    绑定的routing key
        //创建消费者
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            /**
             *  功能描述:
             * @Auther: 江帆
             * @Date:
             * @Description:
             *consumerTag 消息者标签，channel.basicConsume时可以指定
             *envelope  消息包内容，可以获取消息ID，消息routingKey，交换机，消息冲转标识（收到消息失败后是否需要重新发送）
             *properties 消息属性
             * body  消息体
             *
             */

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                System.out.println("------start------消费者2-------------------");
                String routingKey = envelope.getRoutingKey(); //路由key
                String exchange = envelope.getExchange();//交换机
                long deliveryTag = envelope.getDeliveryTag(); //消息ID
                System.out.println("路由key===" + routingKey);
                System.out.println("交换机========" + exchange);
                System.out.println("消息id" + deliveryTag);
                System.out.println("消息内容=====" + new String(body));
                System.out.println("------end-----消费者2-------------------");


            }
        };
        //监听队列
        //参数1 队列名
        //参数2 是否自动确认  true 表示消息接收到自动向mq回复接收到了 ,mq将消息从队列删除，如果设置为false则需要手动确认
        //参数3 消费者对象
        channel.basicConsume(Producer.QUEUE_NAME2, true, defaultConsumer);

        //需要持续监听队列消息接收消息，所以不需要关闭资源

    }
}
