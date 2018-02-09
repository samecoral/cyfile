package com.cao.util.rabbitmq;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMqUtil {

	
	public static Connection getConnection(){
		ConnectionFactory factory = new ConnectionFactory();  
        //设置MabbitMQ所在主机ip或者主机名  
        factory.setHost("200.200.202.154");  
        //指定用户 密码
        factory.setUsername("admin");
        factory.setPassword("admin");
        //指定端口
        factory.setPort(AMQP.PROTOCOL.PORT);
        //创建一个连接  
        try {
			Connection connection =  factory.newConnection();
			return connection;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return null;
	}
	
}
