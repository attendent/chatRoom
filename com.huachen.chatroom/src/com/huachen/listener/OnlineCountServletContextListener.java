package com.huachen.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class OnlineCountServletContextListener implements
		ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("==================在线人数归0===========================");
		ServletContext context = sce.getServletContext();
		context.setAttribute("onlinenum", 0);
	}

}
