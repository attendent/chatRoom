package com.huachen.dao;

import java.util.List;

import com.huachen.model.ChatRoom;
import com.huachen.model.User;

public interface UserDao {
	public boolean isExist(String userName);

	public boolean isExist(Integer userId);

	public boolean isExist(String userName, String password);

	// 判断该用户是否是好友
	public boolean isFriend(Integer userId, Integer friendId);

	// 判断该用户是否已在聊天室内
	public boolean isUserRoom(Integer userId, Integer roomId);

	// 查找用户
	public User find(Integer userId);

	public User find(String userName);

	// 注册用户
	public boolean register(User user);

	// 修改用户密码
	public boolean update(User user, String nextPassword);

	// 添加好友
	public boolean addFriend(Integer userId, Integer friendId);

	// 删除好友
	public boolean delFriend(Integer userId, Integer friendId);

	// 修改好友备注
	public boolean updateRemark(Integer userId, Integer friendId, String remark);

	// 得到所有用户
	public List<User> getListAll();

	// 得到该用户所有好友
	public List<User> getFriends(Integer userId);

	// 得到用户所有的聊天室
	public List<ChatRoom> getChatRooms(Integer userId);

	// 将用户加入聊天室
	public boolean addUserRoom(Integer userId, Integer roomId);

	// 让用户退出聊天室
	public boolean quitUserRoom(Integer userId, Integer roomId);

	// 得到用户邮箱
	public String selectMail(String userName);
	
	public boolean changeNickName(Integer userId, String nextNickName);

}
