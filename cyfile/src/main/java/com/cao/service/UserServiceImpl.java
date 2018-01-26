package com.cao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cao.dao.IUserDao;
import com.cao.pojo.User;
/**
 * 
 * @author caoheshan
 *
 */
@Service
public class UserServiceImpl implements IUserService {

	@Autowired
	private IUserDao userDao;
	@Override
	public User getUserById(int userId) {
		return userDao.findUserById(userId);
	}

}
