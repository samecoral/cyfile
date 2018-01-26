package com.cao;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cao.dao.IUserDao;
import com.cao.pojo.User;
/**
 * 
 * @author caoheshan
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class UserTest {
	@Autowired
	IUserDao userDao;
	@Test
	public void testFindAuthorById() {

		User u = userDao.findUserById(4);
		System.out.println(u.getUserName());

	}
	
	@Test
	public void testInsertUser(){
		User u = new User();
		u.setUserName("caoheshan");
		u.setGroupId(3);
		u.setLocked(0);
		u.setRegDate(new Date());
		u.setUserPwd("16a995df59c545d7b5a1bb6c467335af");
		u.setUserSalt("b0abe376c5429e0c07c65d6ea16a4143");
		userDao.addUser(u);
	}
}
