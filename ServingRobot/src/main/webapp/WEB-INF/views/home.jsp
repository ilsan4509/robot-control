<%@page import="org.robot.project.account.*"%>
<%@page import="org.robot.project.map.*"%>
<%@page import="org.robot.project.HomeController.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/jquery.ui.touch-punch.min.js"></script>
<script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
<script src="http://code.jquery.com/jquery.min.js"></script>
<script src="http://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

<style>
body {
	text-align: center;
	background: white;
}

.viewMap {
	text-align: center;
	margin-left: auto;
	margin-right: auto;
}

.drawMap {
	text-align: center;
	margin-left: auto;
	margin-right: auto;
}

.gridMap {
	text-align: center;
	margin-left: auto;
	margin-right: auto;
}

.changeMap {
	text-align: center;
	margin-left: auto;
	margin-right: auto;
}

.input {
	text-align: center;
	margin-left: auto;
	margin-right: auto;
}

.inText{
	position: absolute;
	font-weight: bold;
}

.userInfo{
	font-weight: bold;
}

.node {
	width: 100%;
	padding-top: 1%;
	padding-bottom: 1%;
}

</style>
<html>
<head>
<title>로봇이동</title>
</head>
<body>
	<% AccountVO accountVO = (AccountVO) session.getAttribute("loginAccount");%>
	<% MapSizeVO mapSize = (MapSizeVO) session.getAttribute("mapSize");%>
	<% int moveRobotId = Integer.parseInt(session.getAttribute("moveRobotId").toString());%>
	<% 
	int targetNode = 0;
	if(session.getAttribute("targetNode") != null){
		targetNode = Integer.parseInt(session.getAttribute("targetNode").toString());
	}
	%>
	<% int nodeSize = Integer.parseInt(session.getAttribute("nodeSize").toString());%>
	
	<img src="<%=request.getContextPath()%>/resources/img/logo.png" width="35%" height="5%" alt="logo">
	<br>
	<a class = "userInfo">
	[ <% out.println(accountVO.getStore_name());%>] 안녕하세요 <% out.println(accountVO.getUser_name());%>님
	</a>
	<a href="<%=request.getContextPath()%>/account/logout" target="_parent" onclick="javascript:btn()">로그아웃</a>
	<P>${serverTime}</P>
	<hr width = "80%">
	(requestion: 경로생성 및 할당 = "path_plan", 복귀 = "arrival_assignment")<br>
	<a>▽ 입력할 값 ▽</a><br>
	<form action=" <%=request.getContextPath()%>/" method="get" target="_parent">
		<table class="input" height="50" width="600">
			<thead>
				<tr>
					<td>robotId</td>
					<td>currentNode</td>
					<td>finalNode</td>
					<td>requestion</td>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td><input type="text" placeholder="robotId" name="robotId" style="width: 90px"></td>
					<td><input type="text" placeholder="currentNode" name="currentNode" style="width: 90px"></td>
					<td><input type="text" placeholder="finalNode" name="finalNode" style="width: 90px"></td>
					<td><input type="text" placeholder="requestion" name="requestion" style="width: 90px" value="path_plan"></td>
				</tr>
			</tbody>
		</table>
		<br>
		<input type="submit" value="입 력">
	</form>
	<hr width = "80%">
	<br> ▽ 입력한 값 ▽
	<table class="input" height="50" width="600">
		<thead>
			<tr>
				<td>RobotId</td>
				<td>CurrentNode</td>
				<td>FinalNode</td>
				<td>Requestion</td>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td>${inputRobotId}</td>
				<td>${inputCurrentNode}</td>
				<td>${inputFinalNode}</td>
				<td>${inputRequestion}</td>
			</tr>
		</tbody>
	</table>
	<hr width = "80%">
	<h3>map_view[ <% out.println(accountVO.getStore_name());%>]</h3>
	<table class="drawMap" height="100" width="700">
	<%  for(int i = 1; i <= mapSize.getMaxY(); i++){ %>
		<tr>
		<%	for(int j = 1; j <= mapSize.getMaxX(); j++){%>
			<%	for(int k = 1; k <=  nodeSize; k++ ){
					MapViewVO mapView = (MapViewVO) session.getAttribute("mapView"+k);
				 	if(mapView.getNode_x() == j && mapView.getNode_y() == i){
				 		if(k == 1){%>
				 			<td><span class="inText" value="<% out.println(k);%>"><% out.println(k);%></span><img src="<%=request.getContextPath()%>/resources/img/block_nodeParking1.png" id="activationNode" class="node"></td>
				 		<%	break;
				 		}else if(k == 3){%>
				 			<td><span class="inText" value="<% out.println(k);%>"><% out.println(k);%></span><img src="<%=request.getContextPath()%>/resources/img/block_nodeParking2.png" id="activationNode" class="node"></td>
				 		<%	break;
				 		}else{
				 			if(k == targetNode && moveRobotId == 1){ %>
				 				<td><span class="inText" value="<% out.println(k);%>"><% out.println(k);%></span><img src="<%=request.getContextPath()%>/resources/img/block_robot1.png" id="activationNode" class="node"></td>
				 			<%	break;
				 			}else if(k == targetNode && moveRobotId == 2){%>
				 				<td><span class="inText" value="<% out.println(k);%>"><% out.println(k);%></span><img src="<%=request.getContextPath()%>/resources/img/block_robot2.png" id="activationNode" class="node"></td>
				 			<%	break;
				 			}else{
				 		%>
								<td><span class="inText" value="<% out.println(k);%>"><% out.println(k);%></span><img src="<%=request.getContextPath()%>/resources/img/block_node.png" id="activationNode" class="node"></td>
						<%		break;
				 			}
				 		}
					}else if(k == nodeSize){%>
					<td><img src="<%=request.getContextPath()%>/resources/img/block.png" id="disabledNode" class="node"> </td>
				<%	}
				}
			}%>
		</tr>
	<%	}%>
	</table>
	<hr width = "80%">
	<h3>map_controller[전체]</h3>
	<table class="gridMap" height="100" width="700">
		<thead>
			<tr>
				<th style="width: 7%; height: 10%">store_seq</th>
				<th style="width: 7%; height: 10%">id</th>
				<th style="width: 7%; height: 10%">stem</th>
				<th style="width: 7%; height: 10%">existence</th>
				<th style="width: 7%; height: 10%">node_l</th>
				<th style="width: 7%; height: 10%">node_r</th>
				<th style="width: 7%; height: 10%">node_up</th>
				<th style="width: 7%; height: 10%">node_down</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="mapLocation" items="${location}">
				<tr>
					<td>${mapLocation.store_seq}</td>
					<td>${mapLocation.id}</td>
					<td>${mapLocation.stem}</td>
					<td>${mapLocation.existence}</td>
					<td>${mapLocation.node_l}</td>
					<td>${mapLocation.node_r}</td>
					<td>${mapLocation.node_up}</td>
					<td>${mapLocation.node_down}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<script>
		function btn() {
			alert('로그아웃 되었습니다.');
		};
	</script>
</body>
</html>
