package com.huachen.listener;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class OnlineCountHttpSessionListener implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		System.out.println("==================在线人数+1===========================");
		HttpSession session = se.getSession();
		se.getSession().setMaxInactiveInterval(30 * 60);//30分钟
		ServletContext context = session.getServletContext();

		int onlinenum = (Integer) context.getAttribute("onlinenum");
		context.setAttribute("onlinenum", onlinenum + 1);
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		System.out.println("==================在线人数-1===========================");
		HttpSession session = se.getSession();
		ServletContext context = session.getServletContext();

		int onlinenum = (Integer) context.getAttribute("onlinenum");
		context.setAttribute("onlinenum", onlinenum - 1);

	}

}
