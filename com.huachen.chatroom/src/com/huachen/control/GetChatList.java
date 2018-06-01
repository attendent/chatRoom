package com.huachen.control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.huachen.model.ChatContent;
import com.huachen.model.Page;
import com.huachen.model.User;
import com.huachen.service.ChatService;
import com.huachen.service.Impl.ChatServiceImpl;

@WebServlet("/GetChatList")
public class GetChatList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	String date = "";
	String beforeDate = "";
	public GetChatList() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String roomId = request.getParameter("roomId");
		User user = (User) request.getSession().getAttribute("user");
		Integer i = 0;

		ChatService chatservice = new ChatServiceImpl();
		List<ChatContent> contents = new ArrayList<>();

		contents = chatservice.getSevenContents(Integer.parseInt(roomId),user.getContentSign());

		List<Page> pages = new ArrayList<>();
		List<String> record = new ArrayList<>();		
		Integer begin = null;
		Integer end = null;
		String date2 = null;
		
		// 从数据库取出消息记录并实现分页
		for (ChatContent content : contents) {
			date = content.getDate().toString();
			StringTokenizer st = new StringTokenizer(date);
			date = st.nextToken();
			if(!date.equals(beforeDate)) {
				if(end != null) {
					Page page = new Page();
					page.setBegin(begin);
					page.setSize(end);
					page.setDate(date2);
					pages.add(page);
				}
				begin = i;
				date2 = date;
				beforeDate = date;
				record.add("-----------------------" + date + "-----------------------");
				i++;
			}
			record.add(content.getUserName() + "于 " + content.getDate() + "发出：" + content.getContent());
			end = i;
			i++;		
		}
		Page page = new Page();
		page.setBegin(begin);
		page.setSize(end);
		page.setDate(date2);
		pages.add(page);
		if(record.isEmpty()) {
			record.add("消息记录为空") ;
		}

		// 将当前聊天输入内容存储
		request.getSession().setAttribute("contents", record);
		request.getSession().setAttribute("pages", pages);

		request.getRequestDispatcher("Index.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

}
