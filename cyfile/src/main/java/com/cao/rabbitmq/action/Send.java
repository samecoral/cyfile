package com.cao.rabbitmq.action;

import java.io.IOException;
import java.security.MessageDigest;import org.aspectj.weaver.GeneratedReferenceTypeDelegate;

import com.cao.util.rabbitmq.RabbitMqUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.AMQP.BasicProperties;

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
	
	/**
	 * 功能 topic 形式的发送
	 *@date 2018年2月9日下午5:24:30
	 *@author caoheshan
	 *@param 
	 *@returnType void
	 *@return
	 */
	public void sendTopic(String routingKey){
		String EXCHANGE_NAME = "test_topic";
		try {
			Connection connection = RabbitMqUtil.getConnection();
			Channel channel = connection.createChannel();
			
			channel.exchangeDeclare(EXCHANGE_NAME, "topic");
			
			for (int i = 0; i < 5; i++) {
				String message = "hello"+i;
				channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
				System.out.println(routingKey+"  发送了消息 ：" +message );
			}
			channel.close();
			connection.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 功能  rpc 的服务端
	 *@date 2018年2月12日下午3:58:44
	 *@author caoheshan
	 *@param 
	 *@returnType void
	 *@return
	 */
	public void sendRpcServer(){
		final String RPC_QUEUE_NAME="";
		Connection connection = RabbitMqUtil.getConnection();
		try {
			Channel channel = connection.createChannel();
			channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);
			channel.basicQos(1);
			
			QueueingConsumer consumer = new QueueingConsumer(channel);
			System.out.println(" wating rpc quests");
			while(true){
				QueueingConsumer.Delivery delivery = consumer.nextDelivery();
				BasicProperties basicProperties = delivery.getProperties();
				BasicProperties replyProps = new BasicProperties.Builder().correlationId(basicProperties.getCorrelationId()).build();
				String message = new String( delivery.getBody());
				System.out.println(" getMd5String( "+message+" )");
				String response = getMd5String(message);
				//返回处理结果队列
				channel.basicPublish("",basicProperties.getReplyTo() , replyProps, response.getBytes());
				//发送应答
				channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
				
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	 // 模拟RPC方法 获取MD5字符串  
    public static String getMd5String(String str) {  
        MessageDigest md5 = null;  
        try {  
            md5 = MessageDigest.getInstance("MD5");  
        } catch (Exception e) {  
            System.out.println(e.toString());  
            e.printStackTrace();  
            return "";  
        }  
        char[] charArray = str.toCharArray();  
        byte[] byteArray = new byte[charArray.length];  
  
        for (int i = 0; i < charArray.length; i++)  
            byteArray[i] = (byte) charArray[i];  
        byte[] md5Bytes = md5.digest(byteArray);  
        StringBuffer hexValue = new StringBuffer();  
        for (int i = 0; i < md5Bytes.length; i++) {  
            int val = ((int) md5Bytes[i]) & 0xff;  
            if (val < 16)  
                hexValue.append("0");  
            hexValue.append(Integer.toHexString(val));  
        }  
        return hexValue.toString();  
    }  
	
}
