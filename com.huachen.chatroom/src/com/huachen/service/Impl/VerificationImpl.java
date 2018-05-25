package com.huachen.service.Impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.huachen.service.Verification;

public class VerificationImpl implements Verification {

	@Override
	public String isLength(String username, String password) {
		if (!username.equals("") && !password.equals("")) {
			if (username.length() <= 16 && password.length() <= 16) {
				return "操作成功";
			}else{
				return "用户名或密码不能超过16位";
			}
		}else {
			return "用户名或密码不能为空";
		}
	}

	@Override
	public String isLength(String username, String password, String nextpassword) {
		if (!username.equals("") && !password.equals("")&& !nextpassword.equals("")) {
			if (username.length() <= 16
					&& password.length() <= 16 && nextpassword.length() <=16) {
				return "操作成功";
			}else {
				return "用户名或密码长度不能大于16";
			}
		}else {
			return "用户名或密码不能为空";
		}
	}

	@Override
	public String isCode(String checkcode, String inputCode) {
		if (!inputCode.equals("") && !checkcode.equals("")) {
			if (inputCode.equalsIgnoreCase(checkcode)) {
				return "验证码正确";
			}else {
				return "验证码错误";
			}
		}else {
			return "验证码不能为空";
		}
	}

	@Override
	public String mailFormat(String mail) {
		String tag = "操作成功";
		final String pattern1 = "^[a-z0-9]+([._\\\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$";
		final Pattern pattern = Pattern.compile(pattern1);
		final Matcher mat = pattern.matcher(mail);	
		if (!mat.find()) {
			tag = "邮箱格式错误";
		}
		return tag;
	}

	@Override
	public String isLength(String nickName) {
		if (!nickName.equals("")) {
			if (nickName.length() <= 16) {
				return "操作成功";
			}else{
				return "名称不能超过16位";
			}
		}else {
			return "名称不能为空";
		}
	}
}
