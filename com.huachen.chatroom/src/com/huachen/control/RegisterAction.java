package com.huachen.control;

import java.io.*;
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

@WebServlet("/register.do")
public class RegisterAction extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet (HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		response.sendRedirect("Index.jsp");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		String nickName = request.getParameter("nickName");
		String mail = request.getParameter("mail");
		String inputCode = request.getParameter("inputCode");
		String code = (String) request.getSession().getAttribute("code");

		request.setAttribute("userName", userName);
		request.setAttribute("password", password);
		request.setAttribute("nickName", nickName);
		request.setAttribute("mail", mail);
		String msg = null;

		Verification ver = new VerificationImpl();
		// 判断验证码
		msg = ver.isCode(inputCode, code);
		if (msg.equals("验证码正确")) {
			msg = ver.isLength(userName, password);
			if (msg.equals("操作成功")) {
				msg = ver.mailFormat(mail);
				if (msg.equals("操作成功")) {
					msg = ver.isLength(nickName);
					if (msg.equals("操作成功")) {
						password = DigertUtils.md5(password);

						User user = new User();
						user.setUserName(userName);
						user.setPassword(password);
						user.setNickName(nickName);
						user.setMail(mail);

						UserService userservice = new UserServiceImpl();
						msg = userservice.register(user);
						if (msg.equals("注册成功")) {
							Integer roomId = 1;
							user = userservice.find(userName);
							userservice.addUserRoom(user.getId(), roomId);
						}
					}
				}
			}
		}
		request.setAttribute("msg", msg);
		request.getRequestDispatcher("Register.jsp").forward(request, response);
	}
}
