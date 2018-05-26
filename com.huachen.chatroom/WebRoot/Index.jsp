<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="css/main.css">
<script type="text/javascript" src="js/JS.js"></script>
<script type="text/javascript">
	document.getElementById("message").innerHTML = "<s:property escape="false" value=${contents }/> ";
</script>
<title>网上聊天室</title>
</head>

<body id="head">
	<form action="">
		<table width="100%" height="69" border="0" cellpadding="0"
			cellspacing="0" align=center>
			<tr>
				<td bgcolor="F9A859" valign="top">
					<table width="100%" height="40" border="0" align="center"
						cellpadding="0" cellspacing="0" bgcolor="FBEAD0">
						<tr>
							<td align="center" style="font-size: 30px;">网上聊天室</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td bgcolor="F9A859" valign="top">
					<table width="100%" border="0" align="center" cellpadding="0"
						cellspacing="0">
						<tr>
							<td align="center" style="font-size: 20px" valign="middle">欢迎
								<c:if test="${!empty(user.nickName) }">
              						${user.nickName }
             					</c:if> 访问! 当前在线人数为 <c:if test="${empty(onlinenum) }">
                     				0
                     			</c:if> <c:if test="${!empty(onlinenum) }">
                   					${onlinenum }
                     			</c:if>人
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</form>
</body>


<div id="left" onclick="printf()">
	<div class="menu">
		<div style="height: 160px; overflow: auto">
			<h3>我的好友</h3>
			<p>
				<c:forEach items="${friends }" var="friend">
  					${friend.nickName }
					<form action="DelFriend" method=post>
						<input type="text" style="display: none" name="friendId"
							value="${friend.id }">
						<button type="submit" value="删除好友">删除好友</button>
					</form>

					<form action="Remark" id="remark" method=post style="display: none">
						<input type="hidden" name="friendId" value="${friend.id }">
						<label>修改备注:</label><input type="text" name="remark" value="楼下小黑">
						<button type="submit" value="确定">确定</button>
					</form>

					<form action="Whisper" method=post>
						<input type="hidden" name="friendId" value="${friend.id }">
						<input type="hidden" name="friendName" value="${friend.nickName }">
						<button type="submit">私聊好友</button>
					</form>

					<form action="Invite" method=post>
						<input type="hidden" name="friendId" value="${friend.id }">
						<input type="hidden" name="roomId" value="${chatRoom.id }">
						<button type="submit">邀请好友</button>
					</form>
				</c:forEach>
			</p>
		</div>

		<div style="height: 160px; overflow: auto">
			<h3>聊天室成员</h3>
			<p>
				<c:forEach items="${userlist }" var="user2">
  					${user2.nickName }
  					<br>
  					<c:if test="${!empty(user.id) }">
					<form action="AddFriend" method="post">
						<input type="hidden" name="friendName" value="${user2.userName }" />
						<button type="submit">添加好友</button>
					</form>
					</c:if>
				</c:forEach>
			</p>
		</div>

		<div style="height: 160px; overflow: auto">
			<h3>聊天室列表</h3>
			<p>
				<c:forEach items="${chatRooms }" var="rooms">
					${rooms.roomName }
					
					<form action="EnterRoom" method="post" name="enterRoom">
						<input type="hidden" name="roomId" value="${rooms.id }"> <input
							type="hidden" name="roomName" value="${rooms.roomName }">
						<button type="submit">进入该群</button>
					</form>

					<form action="QuitRoom" method="post" name="quitRoom">
						<input type="hidden" name="roomId" value="${rooms.id }">
						<button type="submit">退出该群</button>
					</form>

					<br>
				</c:forEach>
			</p>
		</div>
	</div>

	<c:if test="${!empty(user.id) }">
		<form action="AddFriend" method="post" name="form1"
			onsubmit="return test() ">
			<label>用&nbsp;户&nbsp;名：</label> <input type="text" name="friendName"
				id="friendName" size="20" />
			<button type="submit">&nbsp;&lt;添加好友&gt;</button>
		</form>

		<form action="AddRoom" method="post" name="form1"
			onsubmit="return test() ">
			<label>群&nbsp;名(0-16)：</label> <input type="text" name="roomName"
				id="roomName" size="20" />
			<button type="submit">&nbsp;&lt;创建新群&gt;</button>
		</form>
	</c:if>
</div>

<c:if test="${!empty(msg) }">
	<script>
		alert("${msg }");
	</script>
</c:if>

<body>
	<div id="main">
		<h1>
			<c:if test="${!empty(chatRoom) }">${chatRoom.roomName }</c:if>
		</h1>
		<%-- <textarea id="message" style="resize: none" cols="100" rows="24"
			readonly="readonly" name="show_textarea">${contents }
     
		</textarea> --%>
		<div id="message"
			style="contentEditable: true; height: 400px; width: 750px; overflow: auto; borden: 1px">
			<h4>友情提醒:消息记录仅存储最近七天的消息，完整消息请导出消息记录</h4>
			<br> <input type="hidden" name="first">
			<c:forEach items="${contents }" var="content" begin="${param.first }"
				end="${param.last }">
		${content }<br>
			</c:forEach>
			<c:forEach items="${pages }" var="page">
				<a href="Index.jsp?first=${page.begin }&last=${page.size }">${page.date }</a>
			</c:forEach>
		</div>
		<textarea id="text" style="resize: none" cols="100" rows="5"
			name="input_textarea"></textarea>

		<table>
			<tr>
				<th><c:if test="${!empty(user) }">
						<button onclick="send()" onclick="clear()">发送 消息</button>
					</c:if></th>
				<c:if test="${!empty(user.id) }">
					<th><a href="GetChatList?roomId=${chatRoom.id }"><button>消息
								记录</button></a></th>
					<th><a href="Export"><button>导出记录</button></a></th>
					<th>
						<form action="Upload" method="post" enctype="multipart/form-data">
							<input type="hidden" name="roomId" value="${chatRoom.id}" /> <input
								type="hidden" name="roomName" value="${chatRoom.roomName }" />
							<input id="myfile" name="myfile" type="file" /> <input
								type="submit" value="上传" />
						</form>
					</th>
					<th><a href="QuitUser"><button>退出登录</button></a></th>
					<th>
						<button type="button" onclick="change2()">修改昵称</button>
					</th>
					<th>
						<form action="ChangeNickName" id="changeNickName" method=post
							style="display: none">
							<label>修改昵称:</label><input type="text" name="nextNickName"
								value="楼下小黑">
							<button type="submit" value="确定">确定</button>
						</form>
					</th>
				</c:if>
			</tr>
		</table>
		<a href="Register.jsp">前往注册</a> <a href="Login.jsp">前往登录</a>
	</div>
</body>

<body topmargin="0" rightmargin="0">

<div style="float: right">
	<div
		style="height: 170px; width: 280px; overflow: auto; border: 1px; border-style: solid;">
		<h3>聊天室总在线用户</h3>
		<p>
			<c:forEach items="${map }" var="entry">
		${entry.key.nickName }<br>
			</c:forEach>
		</p>
	</div>
	<div
		style="height: 170px; width: 280px; border: 1px; border-style: solid; overflow: auto">
		<h3>本群存储文件</h3>
		<p>
			<c:forEach items="${files }" var="file">
		${file.nickName} 上传了:${file.file }<br>
			</c:forEach>
		</p>
	</div>
		
	<div
		style="height: 170px; width: 280px; overflow: auto; border: 1px; border-style: solid;">
		<h3>好友申请列表</h3>
		<p>
			<c:forEach items="${applicants }" var="applicant">
		${applicant.nickName}
		<form action="Judge?action=Yes" method="post">
			<input type="hidden" name="applicantId" value="${applicant.id }"/>
			<button type="submit">同意</button>
		</form>
		<form action="Judge?action=No" method="post">
			<input type="hidden" name="applicantId" value="${applicant.id }"/>
			<button type="submit">拒绝</button>
		</form>  
			</c:forEach>
		</p>
	</div>
</div>
</body>


<script>
	var websocket = null;
	// 判断当前浏览器是否支持WebSocket
	if ('WebSocket' in window) {
		//alert('当前浏览器支持 websocket');
		websocket = new WebSocket(
				'ws://localhost:8080/com.huachen.chatroom/newwebsocket/'
						+ "${user.nickName }" + '//' + "${chatRoom.id}" + '//'
						+ "${chatRoom.roomName}");
	} else {
		alert('当前浏览器 Not support websocket')
	}
	// 连接发生错误的回调方法
	websocket.onerror = function() {
		setMessageInnerHTML("WebSocket连接发生错误");
		reconnect();
	};
	// 连接成功建立的回调方法
	websocket.onopen = function() {
		//setMessageInnerHTML("WebSocket连接成功");
		heartCheck.start();
	}
	// 接收到消息的回调方法
	websocket.onmessage = function(event) {
		setMessageInnerHTML(event.data);
		heartCheck.reset();
	}
	// 连接关闭的回调方法
	websocket.onclose = function() {
		setMessageInnerHTML("WebSocket连接关闭");
		reconnect();
	}
	// 监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
	window.onbeforeunload = function() {
		closeWebSocket();
	}
	// 将消息显示在网页上
	function setMessageInnerHTML(innerHTML) {
		document.getElementById('message').innerHTML += innerHTML;
	}
	// 关闭WebSocket连接
	function closeWebSocket() {
		websocket.close();
	}
	// 发送消息
	function send() {
		var message = document.getElementById('text').value;
		websocket.send(message);
	}

	//使文本输出框光标在最后一行
	var t = document.getElementById("message");
	setInterval(function() {
		t.scrollTop = t.scrollHeight;
	}, 1000);
</script>
</html>