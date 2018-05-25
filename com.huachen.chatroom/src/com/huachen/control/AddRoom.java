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
import com.huachen.model.User;
import com.huachen.service.ChatService;
import com.huachen.service.UserService;
import com.huachen.service.Impl.ChatServiceImpl;
import com.huachen.service.Impl.UserServiceImpl;

@WebServlet("/AddRoom")
public class AddRoom extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public AddRoom() {
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
		String roomName = request.getParameter("roomName");
		User user = (User) request.getSession().getAttribute("user");
		String msg = null;
		ChatService chatservice = new ChatServiceImpl();
		UserService userservice = new UserServiceImpl();
		List<User> userlist = new ArrayList<>();
		List<ChatRoom> chatRooms = new ArrayList<>();
		if(roomName.length()<=16) {
			msg = chatservice.createRoom(roomName);
		}else {
			msg = "房间名长度不能大于16";
		}
		

		if(msg.equals("创建房间成功")) {
			//更新房间信息，房间成员，用户所有房间
			ChatRoom chatRoom = chatservice.find(roomName);
			userservice.addUserRoom(user.getId(), chatRoom.getId());
			userlist = chatservice.getAllUsers(chatRoom.getId());
			chatRooms = chatservice.getAllChatRooms();
			
			request.getSession().setAttribute("chatRoom", chatRoom);
			request.getSession().setAttribute("userlist", userlist);
			request.getSession().setAttribute("chatRooms", chatRooms);
		}
		
		request.setAttribute("msg", msg);
		request.getRequestDispatcher("Index.jsp").forward(request, response);
		return;
	}

}
