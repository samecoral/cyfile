package com.cao.rabbitmq.action;

import org.junit.Test;

public class TestRecive {

	/**
	 * 功能
	 *@date 2018年2月8日下午3:51:17
	 *@author caoheshan
	 *@param 
	 *@returnType void
	 *@return
	 */
	@Test
	public void testrecieveQueue(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				new Recieve().recieveQueue();
			}
		}).start();
		new Recieve().recieveQueue();
	}
	
	@Test
	public void testPublish(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				new Recieve().recievePublish(" publish_recieve 1");
			}
		}).start();
		
		new Recieve().recievePublish(" publish_recive 2");
	}
	
	
	@Test
	public void testRecieveRoutingDirect(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				new Recieve().recieveRoutingDirect("hello1");
			}
		}).start();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				new Recieve().recieveRoutingDirect("hello1");
			}
		}).start();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				new Recieve().recieveRoutingDirect("hello1");
			}
		}).start();
		
		new Recieve().recieveRoutingDirect("hello2");
	}
}
