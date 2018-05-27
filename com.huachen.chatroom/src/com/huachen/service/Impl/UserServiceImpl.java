package com.huachen.service.Impl;

import java.util.ArrayList;
import java.util.List;

import com.huachen.dao.ChatDao;
import com.huachen.dao.UserDao;
import com.huachen.dao.Impl.ChatDaoImpl;
import com.huachen.dao.Impl.UserDaoImpl;
import com.huachen.model.ChatRoom;
import com.huachen.model.User;
import com.huachen.service.UserService;

public class UserServiceImpl implements UserService {

	UserDao userdao = new UserDaoImpl();

	@Override
	public String login(String userName, String password) {
		if (userdao.isExist(userName, password) == true) {
			return "操作成功";
		} else {
			return "用户名或密码错误";
		}
	}

	@Override
	public String register(User user) {
		if (userdao.isExist(user.getUserName()) == false) {
			if (userdao.register(user) == true) {
				return "注册成功";
			} else {
				return "注册失败";
			}
		} else {
			return "该用户名已存在";
		}
	}

	@Override
	public String update(User user, String nextPassword) {
		if (userdao.isExist(user.getUserName(), user.getPassword()) == true) {
			if (userdao.update(user, nextPassword) == true) {
				return "修改密码成功，点击返回登录界面";
			} else {
				return "修改密码失败";
			}
		} else {
			return "用户名或者密码错误";
		}
	}

	@Override
	public boolean update(String userName, String nextPassword) {
		if (userdao.isExist(userName) == true) {
			User user = new User();
			user.setUserName(userName);
			if (userdao.update(user, nextPassword) == true) {
				return true;
			}
		}
		return false;
	}

	@Override
	public User find(Integer userId) {
		return null;
	}

	@Override
	public List<User> getListAll() {
		List<User> users = new ArrayList<>();
		users = userdao.getListAll();
		return users;
	}

	@Override
	public User find(String userName) {
		User user = new User();
		user = userdao.find(userName);
		return user;
	}

	@Override
	public String addFriend(Integer userId, Integer friendId) {
		if (!userId.equals(friendId)) {
			if (friendId != null) {
				if (userdao.isFriend(userId, friendId) == false) {
					if (userdao.isApplicant(userId, friendId) == false) {
						if (userdao.addFriend(userId, friendId) == true) {
							return "申请好友成功";
						} else {
							return "申请好友失败";
						}
					} else {
						return "正在申请好友";
					}
				} else {
					return "该用户已经是好友了";
				}
			} else {
				return "该用户不存在";
			}
		} else {
			return "不能添加自己为好友";
		}
	}

	@Override
	public List<User> getFriends(Integer userId) {
		if (userId != null) {
			return userdao.getFriends(userId);
		}
		return null;
	}

	@Override
	public String delFriend(Integer userId, Integer friendId) {
		if (userId != null) {
			if (friendId != null) {
				if (!userId.equals(friendId)) {
					if (userdao.isFriend(userId, friendId) == true) {
						if (userdao.delFriend(userId, friendId) == true) {
							return "删除好友成功";
						} else {
							return "删除好友失败";
						}
					} else {
						return "该用户不是你的好友";
					}
				} else {
					return "不能删除自己";
				}
			} else {
				return "该用户不存在";
			}
		} else {
			return "游客不能进行此项操作";
		}
	}

	@Override
	public List<ChatRoom> getChatRooms(Integer userId) {
		if (userId != null) {
			List<ChatRoom> chatRooms = new ArrayList<>();
			chatRooms = userdao.getChatRooms(userId);
			return chatRooms;
		}
		return null;
	}

	@Override
	public boolean updateRemark(Integer userId, Integer friendId, String remark) {
		if (userdao.updateRemark(userId, friendId, remark) == true) {
			return true;
		}
		return false;
	}

	@Override
	public String addUserRoom(Integer userId, Integer roomId) {
		if (userdao.isUserRoom(userId, roomId) == false) {
			if (userdao.addUserRoom(userId, roomId) == true) {
				return "操作成功";
			} else {
				return "操作失败";
			}
		} else {
			return "该用户已在该房间";
		}
	}

	@Override
	public String selectMail(String userName) {
		return userdao.selectMail(userName);
	}

	@Override
	public String quitUserRoom(Integer userId, Integer roomId) {
		if (roomId != 1) {
			ChatDao chatdao = new ChatDaoImpl();
			List<User> users = chatdao.getAllUsers(roomId);
			for (User user : users) {
				if (user.getId() == userId) {
					if (userdao.quitUserRoom(userId, roomId) == true) {
						return "退出聊天群成功";
					} else {
						return "退出聊天群失败";
					}
				}
			}
		} else {
			return "不能退出聊天总群";
		}
		return "你不在这个聊天群里";
	}

	@Override
	public String changeNickName(Integer userId, String nextNickName) {
		if (!nextNickName.equals("")) {
			if (nextNickName.length() <= 16) {
				if (userdao.changeNickName(userId, nextNickName) == true) {
					return "用户昵称修改成功";
				} else {
					return "用户昵称修改失败";
				}
			} else {
				return "用户昵称长度不能超过16";
			}
		} else {
			return "用户昵称不能为空";
		}
	}

	@Override
	public List<User> getApplicants(Integer userId) {
		return userdao.getApplicants(userId);
	}

	@Override
	public String agreedFriend(Integer userId, Integer applicantId) {
		if (userdao.agreedFriend(userId, applicantId) == true) {
			return "成功";
		} else {
			return "发生系统错误";
		}
	}

	@Override
	public String refuseFriend(Integer userId, Integer applicantId) {
		if (userdao.refuseFriend(userId, applicantId) == true) {
			return "被拒绝";
		} else {
			return "发生系统错误";
		}
	}

}
