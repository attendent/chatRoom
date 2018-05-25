	package com.huachen.control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.huachen.model.ChatRoom;
import com.huachen.model.File;
import com.huachen.model.User;
import com.huachen.service.ChatService;
import com.huachen.service.UserService;
import com.huachen.service.Impl.ChatServiceImpl;
import com.huachen.service.Impl.UserServiceImpl;

@WebServlet("/QuitUser")
public class QuitUser extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public QuitUser() {
        super();
    }

    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
        if(session == null){  
        	request.setAttribute("msg", "退出登录失败");
        	request.getRequestDispatcher("Index.jsp").forward(request, response);
            return;  
        }
        request.getSession().invalidate();
        request.setAttribute("msg", "退出登录成功");
        
        // 设置游客身份
        User user = new User();
        Integer roomId = 1;
		String roomName = "聊天总群";
		String nickName = new String();
		int num = (int) (Math.random() * 10000);
		nickName = "游客" + num;
		user = new User();
		user.setNickName(nickName);
		request.getSession().setAttribute("user", user);
		
		List<User> userlist = new ArrayList<>();
		ChatService chatservice = new ChatServiceImpl();
		UserService userservice = new UserServiceImpl();
		ChatRoom chatRoom = new ChatRoom();
		List<ChatRoom> chatRooms = new ArrayList<>();
		List<File> files = new ArrayList<>();
		
		chatRoom.setId(roomId);
        chatRoom.setRoomName(roomName);			      
        
        // 使游客进入聊天总群
		chatRooms = userservice.getChatRooms(user.getId());
		userlist = chatservice.getAllUsers(roomId);
		files = chatservice.getAllFiles(chatRoom.getId());
		
		request.getSession().setAttribute("chatRooms", chatRooms);
		request.getSession().setAttribute("chatRoom",chatRoom);
		request.getSession().setAttribute("userlist",userlist);
		request.getSession().setAttribute("files", files);
        
        request.getRequestDispatcher("Index.jsp").forward(request, response);
	}

    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
