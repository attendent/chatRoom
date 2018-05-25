package com.huachen.listener;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSession;

import com.huachen.model.User;

public class OnlineListListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		
		Map<User, HttpSession> map = new HashMap<User, HttpSession>();
		ServletContext servletContext = sce.getServletContext();

		servletContext.setAttribute("map", map);
	}

}
