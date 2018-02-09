package com.cao.rabbitmq.action;

import org.junit.Test;

public class TestSend {

	@Test
	public void testSendQueue(){
		new Send().sendQueque();
	}
	
	@Test
	public void testSendPublish(){
		new Send().sendPublish();
	}
	
	@Test
	public void testSendRoutingDirect(){
		new Send().sendRouting("hello1");
		new Send().sendRouting("hello2");
		new Send().sendRouting("hello3");
	}
}
