package com.cao.dao;

import com.cao.pojo.User;
/**
 * 
 * @author caoheshan
 *
 */
public interface IUserDao {

	/**通过id查询用户
	 * @author caoheshan
	 * @param userId
	 * @return
	 */
	public User findUserById(int userId);
	
	/**新增用户
	 * @author caoheshan
	 * @param u
	 * @return 
	 */
	public void addUser(User u);
	
	/**
	 * 更新用户信息
	 * @author caoheshan
	 * @param u
	 */
	public void updateUser(User u);
}
