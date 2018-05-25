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
import com.huachen.service.UserService;
import com.huachen.service.Verification;
import com.huachen.service.Impl.UserServiceImpl;
import com.huachen.service.Impl.VerificationImpl;

@WebServlet("/AddFriend")
public class AddFriend extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public AddFriend() {
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
		User user = (User) request.getSession().getAttribute("user");
		String friendName = request.getParameter("friendName");
		String msg = null;
		User friend = new User();
		UserService userservice = new UserServiceImpl();

		Verification ver = new VerificationImpl();
		msg = ver.isLength(friendName);
		if (msg.equals("操作成功")) {
			// 由好友名得到好友的基本信息
			friend = userservice.find(friendName);
			List<User> friends = new ArrayList<>();
			msg = userservice.addFriend(user.getId(), friend.getId());
			if (msg.equals("添加好友成功")) {
				// 更新主页好友列表
				friends = userservice.getFriends(user.getId());
				request.getSession().setAttribute("friends", friends);
			}
		}
		request.setAttribute("msg", msg);
		request.getRequestDispatcher("Index.jsp").forward(request, response);
	}
}
