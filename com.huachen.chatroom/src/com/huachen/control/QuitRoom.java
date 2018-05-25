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

@WebServlet("/QuitRoom")
public class QuitRoom extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public QuitRoom() {
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
		String roomId = request.getParameter("roomId");
		User user = (User) request.getSession().getAttribute("user");
		String msg = null;

		UserService userservice = new UserServiceImpl();
		ChatService chatservice = new ChatServiceImpl();

		msg = userservice.quitUserRoom(user.getId(), Integer.parseInt(roomId));
		if (msg.equals("退出聊天群成功")) {
			// 更新聊天室列表；
			List<ChatRoom> chatRooms = new ArrayList<>();
			List<File> files = new ArrayList<>();
			chatRooms = userservice.getChatRooms(user.getId());
			request.getSession().setAttribute("chatRooms", chatRooms);

			// 当房间内没人时清除房间
			if (chatservice.getAllUsers(Integer.parseInt(roomId)).isEmpty()) {
				chatservice.delRoom(Integer.parseInt(roomId));
			}
			
			// 跳回聊天总群
			ChatRoom chatRoom = new ChatRoom();
			chatRoom.setId(1);
			chatRoom.setRoomName("聊天总群");
			files = chatservice.getAllFiles(Integer.parseInt(roomId));
			request.getSession().setAttribute("chatRoom", chatRoom);
			request.getSession().setAttribute("files", files);
		}
		request.setAttribute("msg", msg);
		request.getRequestDispatcher("Index.jsp").forward(request, response);
	}

}
