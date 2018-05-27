package com.huachen.control;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.huachen.model.User;
import com.huachen.service.UserService;
import com.huachen.service.Impl.UserServiceImpl;
import com.huachen.util.DigertUtils;
import com.huachen.util.MailUtils;

@WebServlet("/SendMail")
public class SendMail extends HttpServlet implements Servlet {

	private static final long serialVersionUID = 1L;
	private static final int validtime = 1000 * 60 * 10; // 10 minutes

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.sendRedirect("Index.jsp");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("action");

		if (action.equals("sendmail")) {
			String userName = request.getParameter("userName");
			String userMail = null;
			
			// 判断用户名是否正确
			if (userName.equals("") || userName.length() > 16) {
				request.setAttribute("msg", "用户名长度请符合规范");
				request.getRequestDispatcher("FindPassword.jsp").forward(request, response);
				return;
			}
			UserService userservice = new UserServiceImpl();
			User user = new User();
			user = userservice.find(userName);
			if (user.getId() == null) {
				request.setAttribute("msg", "该用户不存在");
				request.getRequestDispatcher("FindPassword.jsp").forward(request, response);
				return;
			}
			userMail = user.getMail();
			int code = (int) (Math.random() * 10000);		

			String mailContent = "<div>" + "<div>亲爱的聊天室用户</div>" + "<div>您已经在本聊天室申请了找回密码，请点击下面链接，重新设置您的密码：</div>"
					+ "<div><a href=\"" + "\">" + "</a></div>" + "<div>您的检验码为 '" + code + "'</div>"
					+ "<div>此信是由聊天室系统发出，系统不接受回信，请勿直接回复。</div>" + "<div>致礼！</div>" + "</div>";
			MailUtils.sendEmail("1213018820@qq.com", "ppabylzakledhjbi", new String[] { userMail }, "请重置您的聊天室账号的密码",
					mailContent, null, "text/html", "UTF8");
			request.setAttribute("msg", "短信已发送，请注意查收");
			request.getSession().setAttribute("code", code);
			request.setAttribute("time", System.currentTimeMillis());
			request.setAttribute("userName", userName);
			request.getRequestDispatcher("ReSet.jsp").forward(request, response);
			return;
		} else if (action.equals("ReSetPwd")) {
			String nextPassword = request.getParameter("nextpassword");
			String time = request.getParameter("time");
			String userName = request.getParameter("userName");
			Integer code = (Integer) request.getSession().getAttribute("code");
			String input_code = request.getParameter("input_code");

			// 判断验证码
			if (input_code.equals("")) {
				request.setAttribute("msg", "检验码不能为空");
				request.getRequestDispatcher("ReSet.jsp").forward(request, response);
				return;
			}
			if (input_code.length() > 5) {
				request.setAttribute("msg", "检验码长度过长");
				request.getRequestDispatcher("ReSet.jsp").forward(request, response);
				return;
			}
			if (code.equals(Integer.parseInt(input_code))) {
				long currTimeMillis = System.currentTimeMillis();
				long prevTimeMillis = Long.parseLong(time);
				if (currTimeMillis - prevTimeMillis > validtime) {
					request.setAttribute("msg", "检验码已过期，请重新发送邮件");
					request.getRequestDispatcher("FindPassword").forward(request, response);
					return;
				}

				UserService userservice = new UserServiceImpl();
				if (!nextPassword.equals("")) {
					if (nextPassword.length() <= 16) {
						nextPassword = DigertUtils.md5(nextPassword);

						if (true == userservice.update(userName, nextPassword)) {
							request.setAttribute("msg", "修改密码成功");
							request.getRequestDispatcher("Login.jsp").forward(request, response);
							return;
						} else {
							request.setAttribute("msg", "修改密码失败");
							request.getRequestDispatcher("ReSet.jsp").forward(request, response);
							return;
						}
					} else {
						request.setAttribute("msg", "密码长度错误");
						request.getRequestDispatcher("ReSet.jsp").forward(request, response);
						return;
					}
				} else {
					request.setAttribute("msg", "密码不能为空");
					request.getRequestDispatcher("ReSet.jsp").forward(request, response);
					return;
				}
			} else {
				request.setAttribute("msg", "验证码错误");
				request.getRequestDispatcher("ReSet.jsp").forward(request, response);
				return;
			}
		}

	}
}