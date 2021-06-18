<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<style>
body {
	text-align: center;
	margin-left: auto;
	margin-right: auto;
	background: url('<%=request.getContextPath()%>/resources/img/background_main.png') no-repeat fixed;
	background-size: cover;
}
.par {
	color: blue;
	font-style: italic;
}

</style>
<html>
<head>
<meta charset="UTF-8">
<title>로봇이동</title>
</head>
<body>
	<a class="par"> ${parse_1}${parse_2} </a><br>
		<iframe id="controller" width="800" height="3300" src="<%=request.getContextPath()%>/parsing"></iframe>
</body>
</html> 