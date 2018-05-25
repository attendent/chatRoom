package com.huachen.model;

import java.io.Serializable;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

public class User implements Serializable, HttpSessionBindingListener {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String userName;
	private String password;
	private String nickName;
	private String mail;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	@Override
	public void valueBound(HttpSessionBindingEvent event) {
		// 将新建立Session 和 用户 保存ServletContext 的Map中
		HttpSession session = event.getSession();
		ServletContext servletContext = session.getServletContext();

		@SuppressWarnings("unchecked")
		Map<User, HttpSession> map = (Map<User, HttpSession>) servletContext.getAttribute("map");

		// 将新用户加入map
		map.put(this, session);
	}

	@Override
	public void valueUnbound(HttpSessionBindingEvent event) {
		// 根据user对象，从Map中移除Session
		HttpSession session = event.getSession();
		ServletContext servletContext = session.getServletContext();

		@SuppressWarnings("unchecked")
		Map<User, HttpSession> map = (Map<User, HttpSession>) servletContext.getAttribute("map");

		// 从map移除
		map.remove(this);
	}
}
