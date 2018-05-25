package com.huachen.control;

import java.io.IOException;
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
import com.huachen.util.DigertUtils;

@WebServlet("/update.do")
public class UpdateAction extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("Index.jsp");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset= UTF-8  ");
		response.setCharacterEncoding("UTF-8");

		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String nextpassword = request.getParameter("nextpassword");
		String msg = null;
		request.getSession().setAttribute(username, username);
		
		Verification ver = new VerificationImpl();
		msg = ver.isLength(username, password, nextpassword);
		if (msg.equals("操作成功")) {
			password = DigertUtils.md5(password);
			nextpassword = DigertUtils.md5(nextpassword);

			User user = new User();
			user.setUserName(username);
			user.setPassword(password);

			UserService userservice = new UserServiceImpl();
			msg = userservice.update(user, nextpassword);
			if (msg.equals("修改密码成功，点击返回登录界面")) {
				request.setAttribute("msg", msg);
				request.getRequestDispatcher("Login.jsp").forward(request, response);
				return ;
			} 
		}
		request.setAttribute("msg", msg);
		request.getRequestDispatcher("Update.jsp").forward(request, response);
		return ;
	}	
}