package com.huachen.control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.huachen.model.ChatRoom;
import com.huachen.model.File;
import com.huachen.model.User;
import com.huachen.service.ChatService;
import com.huachen.service.UserService;
import com.huachen.service.Impl.ChatServiceImpl;
import com.huachen.service.Impl.UserServiceImpl;

@WebServlet("/Whisper")
public class Whisper extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Whisper() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.sendRedirect("Index.jsp");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String friendId = request.getParameter("friendId");
		String friendName = request.getParameter("friendName");
		User user = (User) request.getSession().getAttribute("user");
		if(user.getId() == Integer.parseInt(friendId)) {
			request.setAttribute("msg", "不能私聊自己");
			request.getRequestDispatcher("Index.jsp").forward(request, response);
			return ;
		}
		
		UserService userservice = new UserServiceImpl();
		ChatService chatservice = new ChatServiceImpl();
		
		List<User> userlist = new ArrayList<>();
		ChatRoom chatRoom = new ChatRoom();
		
		String roomName = user.getNickName() + "与" + friendName + "的聊天窗";
		//以好友名字创建房间，并得到该房间的基本信息
		if(!chatservice.isExistRoom(roomName)) {
			chatservice.createRoom(roomName);		
		}
		chatRoom = chatservice.find(roomName);
		
		List<ChatRoom> chatRooms = new ArrayList<>();
		List<File> files = new ArrayList<>();
		
		//将自己和好友加入房间
		userservice.addUserRoom(user.getId(), chatRoom.getId());
		userservice.addUserRoom(Integer.parseInt(friendId), chatRoom.getId());
		
		//得到房间消息和成员
		userlist = chatservice.getAllUsers(chatRoom.getId());
		chatRooms = userservice.getChatRooms(user.getId());
		files = chatservice.getAllFiles(chatRoom.getId());
		
		request.getSession().setAttribute("chatRoom", chatRoom);
		request.getSession().setAttribute("userlist", userlist);
		request.getSession().setAttribute("chatRooms", chatRooms);
		String record = "";
		request.getSession().setAttribute("contents", record);
		request.getSession().setAttribute("files", files);

		request.getRequestDispatcher("Index.jsp").forward(request, response);
	}

}
