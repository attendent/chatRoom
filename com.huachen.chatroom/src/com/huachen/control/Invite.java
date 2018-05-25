package com.huachen.control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.huachen.model.User;
import com.huachen.service.ChatService;
import com.huachen.service.UserService;
import com.huachen.service.Impl.ChatServiceImpl;
import com.huachen.service.Impl.UserServiceImpl;

@WebServlet("/Invite")
public class Invite extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Invite() {
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
		String roomId = request.getParameter("roomId");
		String msg = null;
		ChatService chatservice = new ChatServiceImpl();
		UserService userservice = new UserServiceImpl();

		msg = userservice.addUserRoom(Integer.parseInt(friendId), Integer.parseInt(roomId));

		if (msg.equals("操作成功")) {
			// 更新房间中成员列表
			List<User> userlist = new ArrayList<>();
			userlist = chatservice.getAllUsers(Integer.parseInt(roomId));
			request.getSession().setAttribute("userlist", userlist);

			request.setAttribute("msg", "邀请好友成功");
			request.getRequestDispatcher("Index.jsp").forward(request, response);
		} else {
			request.setAttribute("msg", msg);
			request.getRequestDispatcher("Index.jsp").forward(request, response);
		}
	}
}
