package com.huachen.control;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.huachen.model.User;
import com.huachen.service.UserService;
import com.huachen.service.Impl.UserServiceImpl;

@WebServlet("/ClearContents")
public class ClearContents extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ClearContents() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute("user");
		Integer contentSign;
		UserService userservice = new UserServiceImpl();
		contentSign = userservice.getLastContent(user.getId());
		System.out.println(contentSign);

		user.setContentSign(contentSign);
		request.getSession().setAttribute("user", user);
		userservice.addContentSign(user.getId(), contentSign);
		response.sendRedirect("Index.jsp");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
