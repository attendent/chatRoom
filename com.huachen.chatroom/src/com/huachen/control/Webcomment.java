package com.huachen.control;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.text.StringEscapeUtils;

import com.huachen.model.ChatContent;
import com.huachen.service.ChatService;
import com.huachen.service.Impl.ChatServiceImpl;

@ServerEndpoint(value = "/newwebsocket/{nickName}/{roomId}/{roomName}")
public class Webcomment {
	private static int onlineCount = 0;

	// concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
	// private static CopyOnWriteArraySet< Webcomment> webSocketSet = new
	// CopyOnWriteArraySet<Webcomment>();
	private static Map<String, List<Webcomment>> webSocketMap = new HashMap<>();
	//private static List<Webcomment> webSocketList = new ArrayList<>();

	// 与某个客户端的连接会话，需要通过它来给客户端发送数据
	private Session session;

	@OnOpen
	public void onOpen(@PathParam("nickName") String nickName, @PathParam("roomId") String roomId,
			@PathParam("roomName") String roomName, Session session) {

		this.session = session;
		if(webSocketMap.isEmpty()) {
			List<Webcomment> webSocketList = new ArrayList<>();
			webSocketList.add(this);
			webSocketMap.put(roomId, webSocketList);
		}else {
			if(webSocketMap.isEmpty()) {
				List<Webcomment> webSocketList = new ArrayList<>();
				webSocketList.add(this);
				webSocketMap.put(roomId, webSocketList);
			}else {
				for(String Id : webSocketMap.keySet()) {
					if(Id.equals(roomId)) {
						webSocketMap.get(Id).add(this);			
					}else {
						
					}
				}
			}
		}
		
		//webSocketList.add(this);
		//webSocketMap.put(roomId, ); // 加入set中
		addOnlineCount(); // 在线数加1

		String message = String.format("[%s,%s]<br>", nickName + "加入了聊天室", "当前在线人数为" + getOnlineCount());
		
		for (String Id : webSocketMap.keySet()) {
			try {
				if(Id.equals(roomId)) {
					List<Webcomment> list = webSocketMap.get(Id);
					for(Webcomment item : list) {
						System.out.println(item);
						item.sendMessage(message);
					}	
				}						
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
		}
	}

	@OnClose
	public void onClose(@PathParam("nickName") String nickName, @PathParam("roomId") String roomId,
			@PathParam("roomName") String roomName, Session session) {
		this.session = session;
		for(String Id : webSocketMap.keySet()) {
			if(Id.equals(roomId)) {
				webSocketMap.get(Id).remove(this);			
			}
		}
		
		//webSocketMap.remove(roomId, this); // 从set中删除
		subOnlineCount(); // 在线数减1
		String message = String.format("[%s,%s]<br>", nickName + "退出了聊天室", "当前在线人数为" + getOnlineCount());
		// 群发消息
		for (List<Webcomment> list : webSocketMap.values()) {
			try {
				for(Webcomment item : list) {
					item.sendMessage(message);
				}			
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
		}
	}

	@OnMessage
	public void onMessage(String message, @PathParam("nickName") String nickName, @PathParam("roomId") String roomId,
			@PathParam("roomName") String roomName, Session session) {

		String myMessage = null;
		myMessage = StringEscapeUtils.escapeHtml4(message);
		ChatContent chatContent = new ChatContent();

		chatContent.setContent(myMessage);
		chatContent.setUserName(nickName);
		chatContent.setRoomId(Integer.parseInt(roomId));
		chatContent.setRoomName(roomName);
		chatContent.setDate(new Timestamp(System.currentTimeMillis()));

		ChatService chatservice = new ChatServiceImpl();

		// 将本次得到的信息写入数据库
		chatservice.addContent(chatContent);

		List<String> records = new ArrayList<>();
		records.add(nickName + " 于 " + new Timestamp(System.currentTimeMillis()) + " 说： " + myMessage + "<br>");

		for (List<Webcomment> list : webSocketMap.values()) {
			try {
				for(Webcomment item : list) {
					for(String record : records) {
						item.sendMessage(record);
					}		
				}			
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
		}
	}

	@OnError
	public void onError(Session session, Throwable error) {
		System.out.println("发生错误");
		error.printStackTrace();
	}

	public void sendMessage(String message) throws IOException {
		this.session.getBasicRemote().sendText(message);
	}

	public static synchronized int getOnlineCount() {
		return onlineCount;
	}

	public static synchronized void addOnlineCount() {
		Webcomment.onlineCount++;
	}

	public static synchronized void subOnlineCount() {
		Webcomment.onlineCount--;
	}
}