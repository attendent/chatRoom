package com.huachen.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.huachen.model.User;

public class Private implements Filter{

	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException{
		HttpServletRequest httpservletrequest = (HttpServletRequest) request;
		User user = (User) httpservletrequest.getSession().getAttribute("user");
		if(user.getId() == null) {
			request.setAttribute("msg", "请登录后再进行该操作!");
			httpservletrequest.getRequestDispatcher("Index.jsp").forward(request, response);
			return ;
		}
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
	}
	

}
