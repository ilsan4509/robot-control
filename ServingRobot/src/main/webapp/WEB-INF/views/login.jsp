<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<style>
body {
	margin-left: auto;
	margin-right: auto;
	background:
		url('<%=request.getContextPath()%>/resources/img/background_main.png')
		no-repeat fixed;
	background-size: cover;
}

.loginpage {
	margin: 0 auto;
	width: 476px;
	padding-top: 100px;
}

.loginpage_box {
	margin-top: 0;
	margin-left: 0;
	padding: 34px 80px;
	background: #fff;
	box-shadow: 0 1px 3px rgba(0, 0, 0, .2);
	border-radius: 4px
}

.loginpage_form {
	display: block;
	margin-bottom: 20px
}

.loginpage_form_label {
	display: block;
	margin-bottom: 8px;
	font-size: 14px;
	font-weight: 400;
	color: #212121
}

.loginpage_form_input {
	display: block;
	width: 100%;
	box-sizing: border-box;
	padding: 6px 4px 7px;
	border: 1px solid #d6d6d6;
	font-size: 16px;
	line-height: 1.6;
	font-weight: 400;
	margin-bottom: 20px;
}

.loginpage_title {
	margin-top: 37px;
	font-size: 32px;
	font-weight: 400;
	margin-bottom: 24px;
	text-align: center;
}

.loginpage_form_submit {
	cursor: pointer;
	font-size: 16px;
	color: #fff;
	width: 184px;
	text-align: center;
	padding: 8px 0;
	border-radius: 4px;
	border: 0;
	background: linear-gradient(0deg, #264a80 0, #216f98);
	box-shadow: 0 4px 15px 3px rgb(55 63 122/ 23%);
}

.loginpage_form_row {
	text-align: center;
	margin-top: 30px;
	margin-bottom: 30px;
}

.loginpage_partner-title {
	margin-top: 30px;
	border-top: 1px solid #cdcdcd;
	padding-top: 30px;
	margin-bottom: 16px;
	text-align: center;
	display: block;
	font-size: 16px;
	letter-spacing: -1px;
}
</style>

<html>
<head>
<meta charset="UTF-8">
<title>매장 로그인</title>
</head>
<body>
	<div class="loginpage">
		<div class="loginpage_box">
			<div class="loginpage_form">
				<img src="<%=request.getContextPath()%>/resources/img/logo.png" width="100%" height="20%" alt="logo">
				<h2 class="loginpage_title">로그인</h2>
				<form action=" <%=request.getContextPath() %>/" method="post" id="loginForm">
					<span class="loginpage_form_label">아이디</span> 
					<input class="loginpage_form_input" type="text" name="user_id" id="userId" value="" placeholder="아이디를 입력해주세요"> 
					<span class="loginpage_form_label">비밀번호</span> 
					<input class="loginpage_form_input" type="password" name="user_pw" id="userPassword" value="" placeholder="비밀번호를 입력해주세요">
				</form>
				<div class="loginpage_form_row">
					<button type="submit" form="loginForm" type="button" class="loginpage_form_submit" id="btnLogin" >로그인</button>
				</div>
				<div class="loginpage_partner-title">
					고객문의 &nbsp;&nbsp; <strong>010-0000-0000</strong>
				</div>
			</div>
		</div>
	</div>
</body>
</html>