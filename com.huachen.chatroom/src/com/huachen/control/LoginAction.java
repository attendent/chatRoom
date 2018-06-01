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
import com.huachen.service.Verification;
import com.huachen.service.Impl.UserServiceImpl;
import com.huachen.service.Impl.VerificationImpl;
import com.huachen.util.DigertUtils;

@WebServlet("/login.do")
public class LoginAction extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public LoginAction() {
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
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		String inputCode = request.getParameter("inputCode");
		String code = (String) request.getSession().getAttribute("code");
		String msg = null;

		Verification ver = new VerificationImpl();
		UserService userservice = new UserServiceImpl();

		msg = ver.isCode(inputCode, code);
		//if (msg.equals("验证码正确")) {
			msg = ver.isLength(userName, password);
			if (msg.equals("操作成功")) {
				// 加密
				password = DigertUtils.md5(password);
				msg = userservice.login(userName, password);
				if (msg.equals("操作成功")) {
					// 登录成功
					User user = new User();
					user.setUserName(userName);
					user.setPassword(password);
					request.getSession().setAttribute("user", user);
					// 由用户名找到该用户的基本信息并存储在user中
					user = userservice.find(userName);
					// 销毁之前状态
					request.getSession().invalidate();

					@SuppressWarnings("unchecked")
					Map<User, HttpSession> map = (Map<User, HttpSession>) getServletContext().getAttribute("map");
					for (User hasLoginUser : map.keySet()) {
						if (hasLoginUser.getUserName() != null) {
							if (hasLoginUser.getUserName().equals(user.getUserName())) {
								// 此用户之前登陆过 --- 消灭Session
								HttpSession hasLoginSession = map.get(hasLoginUser);
								hasLoginSession.setAttribute("msg", "您的账号已经在另一处登录了,你被迫下线!");
								hasLoginSession.invalidate();// session 被摧毁，移除所有对象
								break;
							}
						}
					}
					request.getSession().setAttribute("user", user);
					response.sendRedirect("Index.jsp");
					return;
				}
			}
		//}
		request.setAttribute("msg", msg);
		request.getRequestDispatcher("Login.jsp").forward(request, response);
	}
}
