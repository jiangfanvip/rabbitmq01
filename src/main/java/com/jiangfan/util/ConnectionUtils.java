package com.jiangfan.util;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Auther: 江帆
 * @Date: 2020/7/12
 * @Description: com.jiangfan.util
 * @version: 1.0
 */
public class ConnectionUtils {
    public static Connection getConnection() throws IOException, TimeoutException {
        //1创建连接工厂    默认    主机 localhost    端口  5672        虚拟主机 默认/ 户名 默认guest  密码 guest
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("106.52.248.12");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/test01");
        connectionFactory.setUsername("jiangfan");
        connectionFactory.setPassword("123456");
        //2 创建连接
        Connection connection = connectionFactory.newConnection();
        return connection;
    }

    ;
}
