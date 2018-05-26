package com.huachen.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.huachen.model.ChatRoom;
import com.huachen.model.File;
import com.huachen.model.User;
import com.huachen.service.ChatService;
import com.huachen.service.UserService;
import com.huachen.service.Impl.ChatServiceImpl;
import com.huachen.service.Impl.UserServiceImpl;

public class IndexFilter implements Filter{

	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("================Index过滤器被调用=====================");
		HttpServletRequest request = (HttpServletRequest) arg0;
		
		User user = (User) request.getSession().getAttribute("user");
		
		Integer roomId = 1;
		String roomName = "聊天总群";
		if (user == null) {
			String nickName = new String();
			int num = (int) (Math.random() * 10000);
			nickName = "游客" + num;
			user = new User();
			user.setNickName(nickName);
			request.getSession().setAttribute("user", user);
		}

		List<User> userlist = new ArrayList<>();
		ChatService chatservice = new ChatServiceImpl();
		ChatRoom chatRoom = new ChatRoom();
		
		chatRoom.setId(roomId);
        chatRoom.setRoomName(roomName);
		
		UserService userservice = new UserServiceImpl();
		List<User> friends = new ArrayList<>();
		List<ChatRoom> chatRooms = new ArrayList<>();
		List<File> files = new ArrayList<>();
		List<User> applicants = new ArrayList<>();
		
		// 得到该用户的所有,好友，聊天室
		friends = userservice.getFriends(user.getId());
		chatRooms = userservice.getChatRooms(user.getId());
		userlist = chatservice.getAllUsers(roomId);
		files = chatservice.getAllFiles(chatRoom.getId());
		applicants = userservice.getApplicants(user.getId());
		
		request.getSession().setAttribute("friends", friends);
		request.getSession().setAttribute("chatRooms", chatRooms);
		request.getSession().setAttribute("chatRoom",chatRoom);
		request.getSession().setAttribute("userlist",userlist);
		request.getSession().setAttribute("files", files);
		request.getSession().setAttribute("applicants", applicants);
		chain.doFilter(arg0, arg1);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
	}

}
