package com.huachen.service.Impl;

import java.util.List;

import com.huachen.dao.ChatDao;
import com.huachen.dao.Impl.ChatDaoImpl;
import com.huachen.model.ChatContent;
import com.huachen.model.ChatRoom;
import com.huachen.model.File;
import com.huachen.model.User;
import com.huachen.service.ChatService;

public class ChatServiceImpl implements ChatService {

	ChatDao chatdao = new ChatDaoImpl();

	@Override
	public String createRoom(String roomName) {
		if (!roomName.equals("")) {
			if (chatdao.createRoom(roomName) == true) {
				return "创建房间成功";
			} else {
				return "创建房间失败";
			}
		} else {
			return "房间名不能为空";
		}
	}

	@Override
	public List<ChatRoom> getAllChatRooms() {
		return chatdao.getAllChatRooms();
	}

	@Override
	public List<ChatContent> getSevenContents(Integer roomId,Integer contentSign) {
		return chatdao.getSevenContents(roomId,contentSign);
	}

	@Override
	public boolean addContent(ChatContent chatContent) {
		return chatdao.addContent(chatContent);
	}

	@Override
	public ChatRoom find(String roomName) {
		return chatdao.find(roomName);
	}

	@Override
	public List<User> getAllUsers(Integer roomId) {
		return chatdao.getAllUsers(roomId);
	}

	@Override
	public boolean addFile(String nickName, Integer roomId, String file) {
		if (chatdao.addFile(nickName, roomId, file) == true) {
			return true;
		}
		return false;
	}

	@Override
	public List<File> getAllFiles(Integer roomId) {
		return chatdao.getAllFiles(roomId);
	}

	@Override
	public boolean delRoom(Integer roomId) {
		return chatdao.delRoom(roomId);
	}

	@Override
	public List<ChatContent> getAllContents(Integer roomId,Integer contentSign) {
		return chatdao.getAllContents(roomId,contentSign);
	}

	@Override
	public boolean isExistRoom(String roomName) {
		return chatdao.isExistRoom(roomName);
	}

}
