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

		UserService userservice = new UserServiceImpl();
		if(action == null) {
			response.sendRedirect("Index.jsp");
			return ;
		}
		if (action.equals("Yes")) {
			msg = userservice.agreedFriend(user.getId(), Integer.parseInt(applicantId));
			
			// 更新好友列表
			List<User> friends = new ArrayList<>();
			friends = userservice.getFriends(user.getId());
			
			request.setAttribute("friends", friends);
			
		} else if (action.equals("No")) {
			msg = userservice.refuseFriend(user.getId(), Integer.parseInt(applicantId));
		}
		
		// 更新申请列表
		List<User> applicants = new ArrayList<>();
		applicants = userservice.getApplicants(user.getId());
		request.setAttribute("applicants", applicants);
		
		request.setAttribute("msg", msg);
		request.getRequestDispatcher("Index.jsp").forward(request, response);
	}

}
