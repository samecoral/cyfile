package com.cao.service;

import com.cao.pojo.User;
/**
 * 
 * @author caoheshan
 *
 */
public interface IUserService {

	/**通过id查找用户信息
	 * @author caoheshan
	 * @param userId
	 * @return
	 */
	public User getUserById(int userId);
}
