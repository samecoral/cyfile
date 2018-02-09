package com.cao.rabbitmq.action;

import java.io.IOException;

import com.cao.util.rabbitmq.RabbitMqUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;

public class Send {

	
	/**
	 * 功能 入门测试
	 *@date 2018年2月7日下午4:59:07
	 *@author caoheshan
	 *@param 
	 *@returnType void
	 *@return
	 */
	public void testSendHelloWorld(){
		final String QUEUE_NAME = "helloworld";
		try {
			Connection connection = RabbitMqUtil.getConnection();
			Channel channel = connection.createChannel();
			channel.queueDeclare(QUEUE_NAME, false, false, false, null);
			String message  = "hello world";
			channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
			System.out.println(" Send message :"+message);
			channel.close();
			connection.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 功能 队列发送
	 *@date 2018年2月7日下午5:08:14
	 *@author caoheshan
	 *@param 
	 *@returnType void
	 *@return
	 */
	public void sendQueque(){
		final String QUEUE_NAME = "workqueue-durable";
		try {
			Connection connection = RabbitMqUtil.getConnection();
			Channel channel = connection.createChannel();
			boolean durable = true;
			
			channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
			
			for (int i = 0; i < 10; i++) {
				String message = " hello :"+i;
				channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
				System.out.println("发送的信息："+ message);
			}
			 channel.close();  
		      connection.close();  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 功能 发布订阅
	 *@date 2018年2月8日下午3:58:40
	 *@author caoheshan
	 *@param 
	 *@returnType void
	 *@return
	 */
	public void sendPublish(){
		
		try {
			Connection connection = RabbitMqUtil.getConnection();
			Channel channel = connection.createChannel();
			channel.exchangeDeclare("test_fanout", "fanout");
			for (int i = 0; i < 10; i++) {
				String message = " hello :"+i;
				channel.basicPublish("test_fanout", "", null, message.getBytes());
				System.out.println("发送的信息："+ message);
			}
			channel.close();
			connection.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 功能  routing  用direct 方式进行发布
	 *@date 2018年2月9日下午3:11:36
	 *@author caoheshan
	 *@param 
	 *@returnType void
	 *@return
	 */
	public void sendRouting(String routingKey){
	
		final  String EXCHANGE_NAME = "rabbit_direct";
		try {
			Connection connection = RabbitMqUtil.getConnection();
			Channel channel = connection.createChannel();
			channel.exchangeDeclare(EXCHANGE_NAME, "direct");
			for(int i=0;i<10;i++){
				String message = " rabbit_direct send "+i;
				channel.basicPublish(EXCHANGE_NAME, routingKey, null,message.getBytes());
				System.out.println(routingKey+" 发送的消息："+message);
			}
			channel.close();
			connection.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
}
