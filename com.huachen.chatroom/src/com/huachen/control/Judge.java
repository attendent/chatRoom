package com.huachen.control;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.huachen.model.User;
import com.huachen.service.UserService;
import com.huachen.service.Impl.UserServiceImpl;

@WebServlet("/Judge")
public class Judge extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Judge() {
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
		String action = request.getParameter("action");
		User user = (User) request.getSession().getAttribute("user");
		String applicantId = request.getParameter("applicantId");
		String msg = null;
		@SuppressWarnings("unchecked")
		Map<User, HttpSession> map = (Map<User, HttpSession>) getServletContext().getAttribute("map");

		UserService userservice = new UserServiceImpl();
		if(action == null) {
			response.sendRedirect("Index.jsp");
			return ;
		}
		if (action.equals("Yes")) {
			msg = userservice.agreedFriend(user.getId(), Integer.parseInt(applicantId));
		} else if (action.equals("No")) {
			msg = userservice.refuseFriend(user.getId(), Integer.parseInt(applicantId));
		}

		for (User hasLoginUser : map.keySet()) {
			if (hasLoginUser.getId().equals(Integer.parseInt(applicantId))) {
				// 此用户之前登陆过 --- 消灭Session
				HttpSession hasLoginSession = map.get(hasLoginUser);
				hasLoginSession.setAttribute("msg", "您添加" + user.getNickName() + msg);
				break;
			}
		}
		response.sendRedirect("Index.jsp");
	}

}
