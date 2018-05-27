package com.huachen.control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.huachen.model.ChatContent;
import com.huachen.model.ChatRoom;
import com.huachen.model.User;
import com.huachen.service.ChatService;
import com.huachen.service.UserService;
import com.huachen.service.Impl.ChatServiceImpl;
import com.huachen.service.Impl.UserServiceImpl;
import com.huachen.util.WordUtil;

@WebServlet("/Export")
public class Export extends HttpServlet {
	private static final long serialVersionUID = 1L;

	String date = "";
	String beforeDate = "";

	public Export() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute("user");
 		String fileName = "消息记录.doc";

		ChatRoom chatRoom = (ChatRoom) request.getSession().getAttribute("chatRoom");

		UserService userservice = new UserServiceImpl();
		ChatService chatservice = new ChatServiceImpl();
		List<ChatContent> contents = new ArrayList<>();
		Integer contentSign = userservice.getLastContent(user.getId());

		contents = chatservice.getAllContents(chatRoom.getId(),contentSign);
		Map<String, Object> mapvalue = new HashMap<String, Object>();

		List<String> record = new ArrayList<>();

		// 从数据库中取出消息记录
		for (ChatContent content : contents) {
			date = content.getDate().toString();
			StringTokenizer st = new StringTokenizer(date);
			date = st.nextToken();
			if (!date.equals(beforeDate)) {
				beforeDate = date;
				record.add("======================" + date + "======================");
			}
			record.add(content.getUserName() + " 于 " + content.getDate() + " 发出： " + content.getContent());
		}
		mapvalue.put("contents", record);

		// 将消息记录导出到work文档
		String endCodeFileName = new String(fileName.getBytes("GBK"), "ISO8859-1");
		response.reset();
		response.setHeader("Content-Type", "application/octet-stream;charset=UTF-8");
		response.setHeader("Content-Disposition", "attachment;filename=" + endCodeFileName);
		response.setHeader("Connection", "close");

		WordUtil wUtil = new WordUtil();
		wUtil.createDoc(mapvalue, response.getOutputStream());
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

}
