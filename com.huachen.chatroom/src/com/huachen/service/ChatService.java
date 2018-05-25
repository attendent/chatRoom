package com.huachen.service;

import java.util.List;

import com.huachen.model.ChatContent;
import com.huachen.model.ChatRoom;
import com.huachen.model.File;
import com.huachen.model.User;

public interface ChatService {
	// 创建新聊天室
	public String createRoom(String roomName);

	// 由房间名找到该房间的基本信息
	public ChatRoom find(String roomName);

	// 得到所有房间
	public List<ChatRoom> getAllChatRooms();

	// 得到某房间七天内消息记录
	public List<ChatContent> getSevenContents(Integer roomId);
	
	// 得到某房间的全部消息纪录
	public List<ChatContent> getAllContents(Integer roomId);

	// 将某条消息记录加入数据库
	public boolean addContent(ChatContent chatContent);

	// 得到房间内所有用户
	public List<User> getAllUsers(Integer roomId);
	
	// 添加文件
	public boolean addFile(String nickName,Integer roomId,String file);
	
	// 得到该房间所有文件
	public List<File> getAllFiles(Integer roomId);
	
	// 删除该房间
	public boolean delRoom(Integer roomId);

	public boolean isExistRoom(String roomName);
}
