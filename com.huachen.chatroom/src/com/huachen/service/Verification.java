package com.huachen.service;

public interface Verification {

	public String isLength(String nickName);
	
	public String isLength(String username, String password);

	public String isLength(String username, String password, String nextpassword);

	// 判断验证码是否正确
	public String isCode(String checkcode, String inputCode);

	public String mailFormat(String mail); 
}
