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

@WebServlet("/ChangeNickName")
public class ChangeNickName extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public ChangeNickName() {
        super();
    }
    
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("Index.jsp");
	}

    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute("user");
		String nextNickName = request.getParameter("nextNickName");
		String msg = null;
		UserService userservice = new UserServiceImpl();
		
		
		msg = userservice.changeNickName(user.getId(),nextNickName);
		if(msg.equals("用户昵称修改成功")) {
			// 修改user
			user = userservice.find(user.getUserName());
			request.getSession().setAttribute("user", user);
		}
		request.setAttribute("msg", msg);
		request.getRequestDispatcher("Index.jsp").forward(request, response);
	}

}
