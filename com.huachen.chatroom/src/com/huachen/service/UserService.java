package com.huachen.service;

import java.util.List;

import com.huachen.model.ChatRoom;
import com.huachen.model.User;

public interface UserService {
	// 用户登录
	public String login(String userName, String password);

	// 用户注册
	public String register(User user);

	// 用户更新
	public String update(User user, String nextPassword);

	// 查找用户
	public User find(String userName);

	public User find(Integer userId);

	// 得到所有用户
	public List<User> getListAll();

	// 添加好友
	public String addFriend(Integer userId, Integer friendId);

	// 删除好友
	public String delFriend(Integer userId, Integer friendId);

	// 修改好友备注
	public boolean updateRemark(Integer userId, Integer friendId, String remark);

	// 邀请好友进入聊天室
	public String addUserRoom(Integer userId, Integer roomId);

	// 让用户退出聊天室
	public String quitUserRoom(Integer userId, Integer roomId);

	// 得到用户所有好友
	public List<User> getFriends(Integer userId);

	// 得到用户所有聊天室
	public List<ChatRoom> getChatRooms(Integer userId);

	// 得到用户的邮箱账号
	public String selectMail(String userName);

	// 修改密码
	public boolean update(String userName, String nextPassword);
	
	public String changeNickName(Integer userId,String nextNickName);
	
	public List<User> getApplicants(Integer userId);
	
	// 同意好友申请
	public String agreedFriend(Integer userId,Integer applicantId);
	
	// 拒绝好友申请
	public String refuseFriend(Integer userId,Integer applicantId);
	
	public Integer getLastContent(Integer userId);

	public void addContentSign(Integer userId,Integer contentSign);
}
