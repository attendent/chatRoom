<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<script type="text/javascript" src="JS/JS.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>修改密码</title>
<link href="css/login.css" rel="stylesheet" type="text/css" />
</head>
<body>
<c:if test="${!empty(msg) }">
<script>
alert("${msg }");
</script>
</c:if>
	<div id="tab">
		<div class="tab_box">
			<div>
				<div class="stu_error_box"></div>
				<form action="SendMail?action=ReSetPwd" method="post" name="form1"
					onsubmit="return test() ">
					<h1>修改密码界面</h1>
					<div id="password">
						<label>修&nbsp;改&nbsp;为(0-16)：</label> <input type="password"
							name="nextpassword" size="20"/>
					</div>
					<div id="code">
					<label>检&nbsp;验&nbsp;码</label><input type="text"
						name="input_code" size="20"/>
					</div>
					<input type="hidden" name="userName" value="${userName }" />
					<input type="hidden" name="time" value="${time }"/>
					<div id="login">
						<button type="submit">&nbsp;&lt;确定&gt;</button>
					</div>
				</form>
			</div>
		</div>
	</div>
</body>
</html>