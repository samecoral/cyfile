package com.cao.rabbitmq.action;

import java.io.IOException;

import com.cao.util.rabbitmq.RabbitMqUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class Recieve {
	
	
	
	/**
	 * 功能 入门发送
	 *@date 2018年2月7日下午5:10:51
	 *@author caoheshan
	 *@param 
	 *@returnType void
	 *@return
	 */
	public void recieveHelloWorld(){
		final  String QUEUE_NAME = "helloworld";
		try {
			Connection connection = RabbitMqUtil.getConnection();
			Channel channel = connection.createChannel();
			channel.queueDeclare(QUEUE_NAME, false, false, false, null);
			
			QueueingConsumer consumer = new QueueingConsumer(channel);
			channel.basicConsume(QUEUE_NAME,true, consumer);
			while(true){
				QueueingConsumer.Delivery delivery = consumer.nextDelivery();
				String message = new String(delivery.getBody());
				System.out.println("  接收的消息 ："+message);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 功能  从队列接收
	 *@date 2018年2月7日下午5:18:55
	 *@author caoheshan
	 *@param 
	 *@returnType void
	 *@return
	 */
	public void recieveQueue(){
		final String QUEUE_NAME = "workqueue-durable";
		String threadName = Thread.currentThread().getName();  

		try {
			Connection connection = RabbitMqUtil.getConnection();
			Channel channel = connection.createChannel();
			
			int prefetchCount = 1;
			channel.basicQos(prefetchCount);
			boolean durable = true;
			channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
			
			//消费者
			QueueingConsumer consumer = new QueueingConsumer(channel);
			//是否自动确认
			boolean ack = false;
			
			channel.basicConsume(QUEUE_NAME, ack,consumer);
			while(true){
				QueueingConsumer.Delivery delivery = consumer.nextDelivery();
				String message = new String(delivery.getBody());
				Thread.sleep(1000);
				System.out.println(" consumer "+threadName+"  消息" +message);
				//确认消息
				channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);//下一个消息  
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 功能  发布订阅的消费
	 *@date 2018年2月8日下午4:07:10
	 *@author caoheshan
	 *@param 
	 *@returnType void
	 *@return
	 */
	public void recievePublish(String queueName){
		
		try {
			Connection connection = RabbitMqUtil.getConnection();
			Channel channel = connection.createChannel();
			channel.exchangeDeclare("test_fanout", "fanout");
			channel.queueDeclare(queueName, false, false, false, null);
			channel.queueBind(queueName, "test_fanout", "");
			QueueingConsumer consumer = new QueueingConsumer(channel);
			channel.basicConsume(queueName, true,consumer);
			
			while(true){
				QueueingConsumer.Delivery delivery = consumer.nextDelivery();
				String message = new String(delivery.getBody());
				System.out.println(queueName+"接受的广播消息 "+message);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 * 功能 从routing进行接收   direct方式
	 *@date 2018年2月9日下午3:29:57
	 *@author caoheshan
	 *@param 
	 *@returnType void
	 *@return
	 */
	public void recieveRoutingDirect(String routingKey){
		final  String EXCHANGE_NAME = "rabbit_direct";
		try {
			Connection connection = RabbitMqUtil.getConnection();
			Channel channel = connection.createChannel();
			channel.exchangeDeclare(EXCHANGE_NAME, "direct");
			String queueName = channel.queueDeclare().getQueue();
			channel.queueBind(queueName, EXCHANGE_NAME, routingKey);
			
			QueueingConsumer consumer = new QueueingConsumer(channel);
			
			channel.basicConsume(queueName, true,consumer);
			while(true){
				QueueingConsumer.Delivery delivery = consumer.nextDelivery();
				String message = new String(delivery.getBody());
				System.out.println(Thread.currentThread().getName()+" ---- "+  routingKey+" 接收到的消息  "+message);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
