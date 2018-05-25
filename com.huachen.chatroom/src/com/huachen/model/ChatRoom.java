package com.huachen.model;

import java.io.Serializable;

public class ChatRoom implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String roomName;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	
	
}
