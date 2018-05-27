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

@WebServlet("/Remark")
public class Remark extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Remark() {
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
		String remark = request.getParameter("remark");
		
		if(remark.length() > 16) {
			request.setAttribute("msg", "备注名长度不能超过16");
			request.getRequestDispatcher("Index.jsp").forward(request, response);
		}
		UserService userservice = new UserServiceImpl();
		if(userservice.updateRemark(user.getId(),Integer.parseInt(friendId),remark) == true) {
			request.setAttribute("msg", "修改备注成功!");
			
			// 更新好友信息
			List<User> friends = new ArrayList<>();
			friends = userservice.getFriends(user.getId());
			request.getSession().setAttribute("friends", friends);
			
			request.getRequestDispatcher("Index.jsp").forward(request, response);
		}else {
			request.setAttribute("msg", "修改备注失败!");
			request.getRequestDispatcher("Index.jsp").forward(request, response);
		}
	}

}