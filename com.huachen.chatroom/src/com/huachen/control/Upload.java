package com.huachen.control;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;
import org.apache.tomcat.util.http.fileupload.util.Streams;

import com.huachen.model.ChatContent;
import com.huachen.model.ChatRoom;
import com.huachen.model.User;
import com.huachen.service.ChatService;
import com.huachen.service.Impl.ChatServiceImpl;

@WebServlet("/Upload")
public class Upload extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("Index.jsp");
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		User user = (User) request.getSession().getAttribute("user");
		ChatRoom chatRoom = (ChatRoom) request.getSession().getAttribute("chatRoom");
		String myfile = request.getParameter("myfile");
		System.out.println("name"+myfile);
		request.setCharacterEncoding("UTF-8");
		Date date = new Date();
		SimpleDateFormat sdfFileName = new SimpleDateFormat("yyyyMMdd");
		String newfileName = sdfFileName.format(date);
		// 真实文件路径
		String fileRealPath = "";
		
		// 保存路径
		String savePath = this.getServletConfig().getServletContext().getRealPath("/") + "uploads\\" + newfileName
				+ "\\";
		File file = new File(savePath);
		if (!file.isDirectory()) {
			file.mkdirs();
		}

		DiskFileItemFactory fac = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(fac);
		upload.setHeaderEncoding("UTF-8");

		// 获取文件列表
		List<FileItem> fileList = null;
		try {
			fileList = upload.parseRequest(new ServletRequestContext(request));
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
		// 遍历文件arrayList
		Iterator<FileItem> it = fileList.iterator();
		while (it.hasNext()) {
			Object obit = it.next();
			if (obit instanceof DiskFileItem) {
				DiskFileItem item = (DiskFileItem) obit;

				// 获取文件名
				String fileName = item.getName();
				if (fileName != null) {
					if(fileName == "") {
						request.setAttribute("msg", "上传文件不能为空");
						request.getRequestDispatcher("Index.jsp").forward(request, response);
						return ;
					}
					// 获取文件名
					String formatName = fileName.substring(fileName.lastIndexOf("."));
					// 保存的真实文件路径
					fileRealPath = savePath + fileName;
					/*if(fileName == null) {
						response.setCharacterEncoding("Index.jsp");
						return ;
					}*/
					System.out.println("fileName"+fileName);
					System.out.println("formatName" + formatName);

					BufferedInputStream in = new BufferedInputStream(item.getInputStream());// 读取文件流
					BufferedOutputStream outStream = new BufferedOutputStream(
							new FileOutputStream(new File(fileRealPath)));//
					Streams.copy(in, outStream, true);// 文件流复制
					// 写入文件
					if (new File(fileRealPath).exists()) {
						// 用户发送文件的信息
						String str = "<a href='uploads/" + newfileName + "/" + fileName + "'>" + fileName
								+ "</a>";

						ChatService chatservice = new ChatServiceImpl();
						
						// 将信息写入数据库
						ChatContent chatContent = new ChatContent();
						chatContent.setContent(str);
						chatContent.setUserName(user.getNickName());
						chatContent.setRoomId(chatRoom.getId());
						chatContent.setRoomName(chatRoom.getRoomName());
						chatContent.setDate(new Timestamp(System.currentTimeMillis()));
						chatservice.addContent(chatContent);
						String chatRecord = user.getNickName() + "于" + chatContent.getDate()
								+ "发送了文件:" + str + "\n";
						
						 chatservice.addFile(user.getNickName(), chatRoom.getId(), str);
						
						List<com.huachen.model.File> f = new ArrayList<>();
						f = chatservice.getAllFiles(chatRoom.getId());
						request.getSession().setAttribute("files", f);
						request.getSession().setAttribute("contents", chatRecord);
					}
				}
			}
		}
		request.setAttribute("msg", "上传文件成功");
		request.getRequestDispatcher("Index.jsp").forward(request, response);
	}
}
