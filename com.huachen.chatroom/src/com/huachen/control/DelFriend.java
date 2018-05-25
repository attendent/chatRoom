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

@WebServlet("/DelFriend")
public class DelFriend extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public DelFriend() {
        super();
    }

    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("Index.jsp");
		
	}

    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String friendId = request.getParameter("friendId");
		User user = (User) request.getSession().getAttribute("user");
		String msg = null;
		UserService userservice = new UserServiceImpl();
		
		msg = userservice.delFriend(user.getId(),Integer.parseInt(friendId));
		if(msg.equals("删除好友成功")) {
			//更新好友列表
			List<User> friends = new ArrayList<>();
			friends = userservice.getFriends(user.getId());
			request.getSession().setAttribute("friends", friends);
		}
		request.setAttribute("msg", msg);
		request.getRequestDispatcher("Index.jsp").forward(request, response);
	}

}
