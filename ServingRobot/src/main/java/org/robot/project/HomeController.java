package org.robot.project;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.robot.project.account.AccountService;
import org.robot.project.account.AccountVO;
import org.robot.project.map.MapColumnsVO;
import org.robot.project.map.MapService;
import org.robot.project.map.MapSizeVO;
import org.robot.project.map.MapVO;
import org.robot.project.map.MapViewVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	@Resource(name = "AccountService")
	AccountService accountService;
	@Resource(name = "MapService")
	MapService mapService;
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	String parseTargetNode;
	String parseNodeNumberAndDirect;

	int inputRobotId;
	int inputCurrentNode;
	int inputFinalNode;
	String inputRequestion;

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ModelAndView login_check(HttpServletResponse response, AccountVO vo, ModelAndView mav, HttpSession session) throws IOException {
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();

		if (vo.getUser_id() == null || vo.getUser_id().equals("")) {
			out.println("<script>alert('아이디를 입력해주세요.'); history.go(-1);</script>");
			out.flush();
			mav.setViewName("login");
			return mav;
		} else if (vo.getUser_pw() == null || vo.getUser_pw().equals("")) {
			out.println("<script>alert('비밀번호를 입력해주세요.'); history.go(-1);</script>");
			out.flush();
			mav.setViewName("login");
			return mav;
		} 

		AccountVO account = accountService.getAccount(vo);

		// 아이디 있는지 유무
		if (account == null) {
			out.println("<script>alert('비밀번호 또는 아이디가 틀렸습니다.'); history.go(-1);</script>");
			out.flush();
			mav.setViewName("login");
			return mav;
		} else if (vo.getUser_pw().equals(account.getUser_pw())) {
			// 로그인 계정 세션 남기기
			session.setAttribute("loginAccount", account);
			ModelAndView mav1 = new ModelAndView("/parsing");
			return mav1;
		}
		out.println("<script>alert('아이디 또는 비밀번호가 틀렸습니다.'); history.go(-1);</script>");
		out.flush();
		mav.setViewName("login");
		return mav;

	}

	@SuppressWarnings("null")
	@RequestMapping(value = "/", method = { RequestMethod.GET })
	public ModelAndView hiEmbedded(AccountVO accountVo, HttpServletRequest req, MapVO mapVo, MapColumnsVO vo, Model model, Map<String, Object> map, ModelAndView mav, HttpSession session, HttpServletResponse response) throws IOException {
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();

		// 로그인 했는지 세션 확인
		AccountVO accountVO = (AccountVO) session.getAttribute("loginAccount");
		if (accountVO == null) {
			mav.setViewName("login");
			return mav;
		}

		if ((req.getParameter("robotId") == "") || (req.getParameter("currentNode") == "") || (req.getParameter("finalNode") == "")) {
			out.println("<script>alert('▽ 입력할 값 ▽에 값을 넣어주세요'); history.go(1);</script>");
			out.flush();
			mav.setViewName("parsing");
			return mav;
		}else if (!("path_plan".equals(req.getParameter("requestion"))) && !("arrival_assignment".equals(req.getParameter("requestion")))) {
			out.println("<script>alert('▽ requestion▽에 올바른 값을 넣어주세요'); history.go(1);</script>");
			out.flush();
			mav.setViewName("parsing");
			return mav;
		}
		
		// 인풋에 입력한 값 받는 변수
		int robotId = Integer.parseInt(req.getParameter("robotId"));
		int currentNode = Integer.parseInt(req.getParameter("currentNode")); //로봇의 현재 위치
		int finalNode = Integer.parseInt(req.getParameter("finalNode")); //로봇 행선지
		String requestion = req.getParameter("requestion"); //가는건지("path_plan"), 복귀하는 건지("arrival_assignment") 

		inputRobotId = robotId;
		inputCurrentNode = currentNode;
		inputFinalNode = finalNode;
		inputRequestion = requestion;

		List<MapVO> list = mapService.getMapGrid(mapVo);

		int[][] vertex = null;
		int[] path = null;
		int pathCount = 0;
		String[] edgeDirection = { "node_l", "node_r", "node_up", "node_down" };
		int targetNode = 0;
		String[] check = new String[7];
		int storeSeq = accountVO.getStore_seq();
		int storeNodeSize = 0;
		int storeNodeSizeFirst = 0;
		
		//map_controller 테이블에서 로그인한 매장의 내용만 받아 배열로 옯긴다.  
		for(int i = 0; i< list.size(); i++) {
			if(storeSeq == list.get(i).getStore_seq()) {
				if(storeNodeSize == 0) {
					storeNodeSizeFirst = i;
				}
				storeNodeSize = storeNodeSize+1;
			}
		}
		String[][] returnData = new String[storeNodeSize + 2][7];
		
		if ("path_plan".equals(requestion)) { //로봇이 행선지로 갈 때.
			parseNodeNumberAndDirect = "";

			List<MapColumnsVO> columnsList = new ArrayList<MapColumnsVO>();
			columnsList = mapService.getMapColumns(vo);

			for (int i = 0; i < columnsList.size() - 2; i++) { //컬럼 네임들 뽑아서 정렬.
				check[i] = columnsList.get(i).getColumn_name();
				returnData[1][i] = check[i];
			}
			if (list.size() > 0) { //returnData 배열에 해당 매장의 테이블 값만 불러온다.
				for (int i = 0; i < list.size(); i++) { // 첫번호와 끝번호를 알아야한다. 
					for (int j = 0; j < columnsList.size(); j++) {
						if(storeSeq == list.get(i).getStore_seq()) {
							if (j == 0) {
								String dataId = Integer.toString(list.get(i).getId());
								returnData[i - storeNodeSizeFirst + 2][j] = dataId;
							} else if (j == 1) {
								String dataStem = Integer.toString(list.get(i).getStem());
								returnData[i - storeNodeSizeFirst + 2][j] = dataStem;
							} else if (j == 2) {
								String dataExistence = Integer.toString(list.get(i).getExistence());
								returnData[i - storeNodeSizeFirst + 2][j] = dataExistence;
							} else if (j == 3) {
								String dataNode_l = Integer.toString(list.get(i).getNode_l());
								returnData[i - storeNodeSizeFirst + 2][j] = dataNode_l;
							} else if (j == 4) {
								String dataNode_r = Integer.toString(list.get(i).getNode_r());
								returnData[i - storeNodeSizeFirst + 2][j] = dataNode_r;
							} else if (j == 5) {
								String dataNode_up = Integer.toString(list.get(i).getNode_up());
								returnData[i - storeNodeSizeFirst + 2][j] = dataNode_up;
							} else if (j == 6) {
								String dataNode_down = Integer.toString(list.get(i).getNode_down());
								returnData[i - storeNodeSizeFirst + 2][j] = dataNode_down;
							}
						}
					}
				}
			}

			returnData[0][0] = "DB_table READ COMPLETE"; //테스트 하기위한 값
//			for(int i = 0; i < returnData.length; i++) {
//				for(int j = 0; j < returnData[i].length; j++) {
//					System.out.print(returnData[i][j]);
//				}
//				System.out.println();
//			}
			//배열인덱스와 노드id 맞추기
			vertex = new int[returnData.length - 1][columnsList.size()];

			for (int i = 2; i < returnData.length; i++) {
				String node_id = returnData[i][0].toString();
				if (node_id == "") {
					for (int j = 0; j < returnData[i].length; j++) {
						vertex[i][j] = (Integer) null;
					}
				} else {
					for (int j = 0; j < returnData[i].length; j++) {
						vertex[Integer.parseInt(node_id)][j] = Integer.parseInt(returnData[i][j]);
					}
				}
			}

			// info_visit_weight_connect_init
			String[] visit = new String[vertex.length];
			for (int i = 1; i < vertex.length; i++) {
				if (vertex[i][0] == 0) {
					visit[i] = "empty";
				} else {
					visit[i] = "false";
				}
			}
			// 노드id별 가중치
			int[][] edge = new int[visit.length][visit.length];

			for (int f_node = 1; f_node < visit.length; f_node++) {
				for (int e_node = 1; e_node < visit.length; e_node++) {
					if (f_node == e_node) {
						edge[f_node][e_node] = 0;
					} else {
						edge[f_node][e_node] = 10000;
					}
				}
			}

			for (int f_node = 1; f_node < visit.length; f_node++) {
				for (int i = 3; i <= 6; i++) {
					if (vertex[f_node][i] != 0) {
						int e_node = vertex[f_node][i];
						edge[f_node][e_node] = 1;
					} else {

					}
				}
			}

			int[] dist = new int[edge.length];

			for (int i = 1; i < edge.length; i++) {
				dist[i] = edge[currentNode][i];
			}

			visit[currentNode] = "true";

			path = new int[visit.length];

			for (int i = 1; i < visit.length; i++) {
				path[i] = currentNode;
			}

			String[] dir = new String[visit.length];

			for (int i = 1; i < visit.length - 1; i++) {
//						System.out.println();
				int min = 1000000;
				int index = 0;
				for (int j = 1; j < visit.length; j++) {
					if (dist[j] < min && visit[j] == "false") {
						min = dist[j];
						index = j;
					}
				}
				int current = index;
				visit[current] = "true";
				for (int j = 1; j < visit.length; j++) {
					if (visit[j] == "false") {
						if (dist[current] + edge[current][j] < dist[j]) {
							dist[j] = dist[current] + edge[current][j];
							path[j] = current;
						}
					}
				}
			}
			path[currentNode] = 0;
			// 위 정보를 토대로 방향성을 부여 
			// path배열은 인덱스가 로봇이 도착하는 블럭이며 담겨있는 내용은 다음 블럭으로 가기 위한 전 노드의 정보가 담겨있다. 
			// 전 노드의 번호를 사용해 노드별 연결 구조가 저장된 vertex배열에 대입하여 전 노드의 앞뒤좌우 값을 비교하여 dir배열에 대입 
			for (int j = 1; j < visit.length; j++) {
				int tmp = path[j]; //path는 해당 인덱스번호가 현재찍힐 노드, 이전노드에 대한 정보를 담음, 이것을 tmp에 저장
				for (int k = 3; k <= 6; k++) { //앞뒤좌우 값 확인
					if (tmp == 0) {//전 노드값이 0이면 그 노드는 시작점.
						dir[j] = "none";
						break;
					} else if (vertex[tmp][k] == j) {// 해당노드에서 연결된곳이 어딘지 확인
						dir[j] = edgeDirection[k - 3];
					}
				}
			}
			pathCount = 0;

			int[] nodeNumber = new int[visit.length];
			String[] direction = new String[visit.length];
			//초기화 작업
			for (int i = 1; i < visit.length; i++) {
				nodeNumber[i] = 0;
				direction[i] = "";
			}
			nodeNumber[0] = path[finalNode];
			direction[0] = dir[finalNode];
			pathCount++;

			if (nodeNumber[0] != 1) {
				for (int i = 1; i < visit.length; i++) {
					nodeNumber[i] = path[nodeNumber[i - 1]];
					direction[i] = dir[nodeNumber[i - 1]];
					pathCount++;
					if (nodeNumber[i] == currentNode) {
						break;
					}
				}
			}

//			System.out.println();
//			for (int j = 0; j < nodeNumber.length; j++) {
//				System.out.print("[" + j + "]=>" + nodeNumber[j] + " ");
//			}
//			System.out.println();
//			for (int j = 0; j < direction.length; j++) {
//				System.out.print("[" + j + "]=>" + direction[j] + " ");
//			}

			for (int i = 0; i < pathCount / 2; i++) {
				int temporary_box1 = nodeNumber[i];
				nodeNumber[i] = nodeNumber[(pathCount - 1) - i];
				nodeNumber[(pathCount - 1) - i] = temporary_box1;
				String temporary_box2 = direction[i];
				direction[i] = direction[(pathCount - 1) - i];
				direction[(pathCount - 1) - i] = temporary_box2;
			}

//			System.out.println();
//			for (int i = 0; i < pathCount; i++) {
//				System.out.println(nodeNumber[i]);
//				System.out.println(direction[i]);
//			}

			/*
			 * Trim_Path&Path_Assignment.php
			 */

			/* function Creat_Node_array */
			int[] path_to_go_through = new int[pathCount + 1];
			for (int i = 0; i < pathCount; i++) {
				path_to_go_through[i] = nodeNumber[i];
			}
			path_to_go_through[pathCount] = finalNode;

//			System.out.println("returnData[][]=");
//			for (int i = 0; i < returnData.length; i++) {
//				for (int j = 0; j < returnData[i].length; j++) {
//					System.out.print(returnData[i][j] + " ");
//				}
//				System.out.println();
//			}

			// 지나가는 경로에 로봇이 있는지, 확인하고 있으면 경로지우기(경로가 중첩되서 병목현상일어남)
			// if 많이 겹친거 나중에 정리
//			System.out.println("11path_to_go_through.length=" + path_to_go_through.length);
			for (int i = 2; i < returnData.length; i++) {
				for (int j = 0; j < path_to_go_through.length; j++) {
//							System.out.println("returnData[i][0]="+(returnData[i][0]));
//							System.out.println("path_to_go_through[j]="+(path_to_go_through[j]));
					if (Integer.parseInt(returnData[i][0]) == path_to_go_through[j]) {
//								System.out.println("if 에 들어왔는지"+(path_to_go_through[j]));
						if (Integer.parseInt(returnData[i][0]) != 0) {
							if (Integer.parseInt(returnData[i][2]) != robotId && Integer.parseInt(returnData[i][2]) != 0) {
								int temporary_value = nodeNumber[0];
								nodeNumber = new int[visit.length];
								direction = new String[visit.length];
								path_to_go_through = new int[1];
								path_to_go_through[0] = currentNode;
								nodeNumber[0] = temporary_value;
//								System.out.println("22path_to_go_through.length=" + path_to_go_through.length);
//								for (int k = 0; k < nodeNumber.length; k++) {
//									System.out.print("nodeNumber[" + k + "]" + nodeNumber[k] + " ");
//								}
							}
						}
					}
				}
			}
//			System.out.println("정리전 nodeNumber[]=");
//			for (int i = 0; i < nodeNumber.length; i++) {
//				System.out.print(nodeNumber[i] + " ");
//			}

			for (int i = 0; i < nodeNumber.length; i++) {
				if (nodeNumber[i] == 0) {
					nodeNumber = Arrays.copyOf(nodeNumber, i);
				}
			}
//			System.out.println();
//			System.out.println("정리후 nodeNumber[]=");
//			for (int i = 0; i < nodeNumber.length; i++) {
//				System.out.print(nodeNumber[i] + " ");
//			}
//
//			System.out.println();
//			System.out.println("정리전 direction[]=" + direction.length);
//			for (int i = 0; i < direction.length; i++) {
//				System.out.print(direction[i] + " ");
//			}

			for (int i = 0; i < direction.length; i++) {
				if (direction[i] == "" || direction[i] == null) {
					direction = Arrays.copyOf(direction, i);
				}
			}
//			System.out.println();
//			System.out.println("정리후 direction[]=" + direction.length);
//			for (int i = 0; i < direction.length; i++) {
//				System.out.print(direction[i] + " ");
//			}

			int temporary_value = path_to_go_through.length;
			//만들어진 경로의 처음노드부터 시작해서 최대한 갈수있는 노드 까지 다듬기
//			System.out.println();
			for (int i = 1; i < temporary_value; i++) {
				for (int j = 2; j < returnData.length; j++) {
//							for (int k = 0; k < path_to_go_through.length; k++) {
//								System.out.print(path_to_go_through[k] + " ");
//							}
					if (path_to_go_through[i] == Integer.parseInt(returnData[j][0])) {
						if (Integer.parseInt(returnData[j][2]) != 0 && Integer.parseInt(returnData[j][2]) != robotId) {
							for (int k = i; k < temporary_value; k++) {
								path_to_go_through[k] = 0;
								nodeNumber[k - 1] = 0;
								direction[k - 1] = "";
							}
						}
					}
				}
			}

			/* function path_Assignment */
			// 경로 할당
//			System.out.println("path_to_go_through.length[]=" + path_to_go_through.length);
//			for (int i = 0; i < path_to_go_through.length; i++) {
//				System.out.print(path_to_go_through[i] + " ");
//			}
//			System.out.println("경로 할당");
			for (int i = 0; i < path_to_go_through.length; i++) {
				// MySQL작업1
				int path_to_go_i = path_to_go_through[i];
//				System.out.println("UPDATE map_controller SET existence = " + robotId + " WHERE id = " + path_to_go_i);

				MapVO mapVO = new MapVO();
				mapVO.setExistence(robotId);
				mapVO.setId(path_to_go_i);
				mapVO.setStore_seq(storeSeq);
				mapService.modiMap1(mapVO);
			}

//			System.out.println("전 노드 해제");
			//현재 가려는경로 이외의 경로는 전부 해제
			for (int i = 2; i < returnData.length; i++) {
				if (Integer.parseInt(returnData[i][2]) == robotId) {
					int cancel = 1;
					//경로 할당해제 할지 말지, 1은 해제
					for (int j = 0; j < path_to_go_through.length; j++) {
						if (path_to_go_through[j] == Integer.parseInt(returnData[i][0])) {
							cancel = 0;
						}
					}
					if (cancel == 1) { 
						int k = Integer.parseInt(returnData[i][0]);
						int reset_zero = 0;
						// MySQL작업2
//						System.out.println("UPDATE map_controller SET existence = " + reset_zero + " WHERE id = " + k);
						MapVO mapVO = new MapVO();
						mapVO.setExistence(reset_zero);
						mapVO.setId(k);
						mapVO.setStore_seq(storeSeq);
						System.out.println(mapVO);
						mapService.modiMap2(mapVO);
					}
				}
			}

			// 목표노드의 줄기 경로할당
//			System.out.println("줄기 경로 할당");
			int lastNode = path_to_go_through[path_to_go_through.length - 1];
//					System.out.println("lastNode="+lastNode);
			for (int i = 2; i < returnData.length; i++) {
//						System.out.println("returnData[i][0]=lastNode= "+returnData[i][0]+" = "+lastNode);
				if (Integer.parseInt(returnData[i][0]) == lastNode) {
//							System.out.println("returnData[i][0])==lastNode");
					if (Integer.parseInt(returnData[i][1]) != 0) {
						int k = Integer.parseInt(returnData[i][1]);
						// MySQL작업3
//						System.out.println("UPDATE map_controller SET existence = " + robotId + " WHERE stem = " + k);
//						System.out.println("줄기할당="+k);
						MapVO mapVO = new MapVO();
						mapVO.setExistence(robotId);
						mapVO.setStem(k);
						mapVO.setStore_seq(storeSeq);
						mapService.modiMap3(mapVO);
					}
				}
			}

//			System.out.println("nodeNumber[i]");
//			for (int i = 0; i < nodeNumber.length; i++) {
//				System.out.print(nodeNumber[i] + " ");
//			}
//			System.out.println("direction[i]");
//			for (int i = 0; i < direction.length; i++) {
//				System.out.print(direction[i] + " ");
//			}
			int targetNodeCount = (path_to_go_through.length - 1);
			targetNode = path_to_go_through[targetNodeCount];

//			System.out.println("path_to_go_through[i]");
//			for (int i = 0; i < path_to_go_through.length; i++) {
//				System.out.print("[" + i + "] = " + path_to_go_through[i] + " ");
//			}
//			System.out.println();
//			System.out.println("path_to_go_through[targetNodeCount] = " + path_to_go_through[targetNodeCount]);
//			for (int i = 0; i < nodeNumber.length; i++) {
//				System.out.println("nodeNumber[" + i + "] = " + nodeNumber[i]);
//			}

//			System.out.println("여기서부터 로봇이 읽을 브라우저 내용");
//			System.out.print(targetNode);
//			System.out.print("!");
			// 여기서부터 로봇이 파싱할 내용을 브라우저 내용을
			parseTargetNode = (Integer.toString(targetNode)) + "!";
			session.setAttribute("targetNode", Integer.toString(targetNode));
			
			if (direction.length != 0 && nodeNumber.length != 0 && nodeNumber[0] != 0) {
				for (int i = 0; i < path_to_go_through.length - 1; i++) {
					System.out.print(nodeNumber[i]);
					System.out.print(",");
					System.out.print(direction[i]);
					System.out.print("/");
					if (nodeNumber[i] != 0 || direction[i] != null) {
						parseNodeNumberAndDirect = parseNodeNumberAndDirect.concat(Integer.toString(nodeNumber[i]) + "," + (direction[i]) + "/");
					}
				}
			} else {
				out.println("<script>alert('해당경로에 이미 이동주인 로봇이 있어 이동을 못합니다.'); history.go(1);</script>");
				out.flush();
			}
			if (parseTargetNode != null) {
				model.addAttribute("parse_1", parseTargetNode);
			}
			if (parseNodeNumberAndDirect != null) {
				model.addAttribute("parse_2", parseNodeNumberAndDirect);
			}
			mav.setViewName("parsing");
			return mav;
		} else if ("arrival_assignment".equals(requestion)) {//복귀, 로봇의 디스플레이 또는 뒙등 디바이스에서 복귀를 눌렀을 때
			List<MapColumnsVO> columnsList = new ArrayList<MapColumnsVO>();
			columnsList = mapService.getMapColumns(vo);

			for (int i = 0; i < columnsList.size()-2; i++) {
				check[i] = columnsList.get(i).getColumn_name();
				returnData[1][i] = check[i];

			}
//			for(int i = 0; i < check.length; i++) {
//				  System.out.println("check["+i+"]= "+check[i]);
//				  System.out.println("returnData[1]["+i+"]= "+returnData[1][i]);
//			}
			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					for (int j = 0; j < columnsList.size(); j++) {
						if(storeSeq == list.get(i).getStore_seq()) {
							if (j == 0) {
								String dataId = Integer.toString(list.get(i).getId());
								returnData[i - storeNodeSizeFirst + 2][j] = dataId;
							} else if (j == 1) {
								String dataStem = Integer.toString(list.get(i).getStem());
								returnData[i - storeNodeSizeFirst + 2][j] = dataStem;
							} else if (j == 2) {
								String dataExistence = Integer.toString(list.get(i).getExistence());
								returnData[i - storeNodeSizeFirst + 2][j] = dataExistence;
							} else if (j == 3) {
								String dataNode_l = Integer.toString(list.get(i).getNode_l());
								returnData[i - storeNodeSizeFirst + 2][j] = dataNode_l;
							} else if (j == 4) {
								String dataNode_r = Integer.toString(list.get(i).getNode_r());
								returnData[i - storeNodeSizeFirst + 2][j] = dataNode_r;
							} else if (j == 5) {
								String dataNode_up = Integer.toString(list.get(i).getNode_up());
								returnData[i - storeNodeSizeFirst + 2][j] = dataNode_up;
							} else if (j == 6) {
								String dataNode_down = Integer.toString(list.get(i).getNode_down());
								returnData[i - storeNodeSizeFirst + 2][j] = dataNode_down;
							}
						}
					}
				}
			}

			for(int i = 0; i < returnData.length; i++) {
				for(int j = 0; j < returnData[i].length; j++) {
					System.out.print(returnData[i][j]+"$ ");
				}
				System.out.println();
			}

			returnData[0][0] = "DB_table READ COMPLETE";

//			System.out.println("!!!도착시 줄기이외 노드의 경로해제!!!");
			//현재 어떤 줄기인지
			int nowStem = 0;
			for (int i = 2; i < returnData.length; i++) {
				if (Integer.parseInt(returnData[i][0]) == currentNode) {
					nowStem = Integer.parseInt(returnData[i][1]);
				}
			}
			// MySQL작업4
//			System.out.println(nowStem + "번 줄기 경로 할당!!!");
//			System.out.println("UPDATE map_controller SET existence = " + robotId + " WHERE stem = " + nowStem);
			
			MapVO mapVO = new MapVO();
			mapVO.setExistence(robotId);
			mapVO.setStem(nowStem);
			mapVO.setStore_seq(storeSeq);
			// 갈 줄기 경로 할당
			mapService.modiMap4(mapVO);
			System.out.println(mapVO);
			for (int i = 2; i < returnData.length; i++) {
				if (Integer.parseInt(returnData[i][1]) != robotId && Integer.parseInt(returnData[i][2]) == robotId) {
					int nowStem2 = Integer.parseInt(returnData[i][0]);
					// MySQL작업5
					int reset_zero = 0;
//					System.out.println("UPDATE map_controller SET existence = " + reset_zero + " WHERE id = " + nowStem2);
//					System.out.println(nowStem2 + "번 노드 경로 해제!");
					MapVO mapVO5 = new MapVO();
					mapVO5.setExistence(reset_zero);
					mapVO5.setId(nowStem2);
					mapVO5.setStore_seq(storeSeq);
					// 전에 노드 경로 짜여진 것 해제!
					mapService.modiMap5(mapVO5);
				}
			}
			if (parseTargetNode != null) {
				model.addAttribute("parse_1", parseTargetNode);
			}
			if (parseNodeNumberAndDirect != null) {
				model.addAttribute("parse_2", parseNodeNumberAndDirect);
			}
			mav.setViewName("parsing");
			return mav;
		}
		mav.setViewName("login");
		return mav;
	}

	@RequestMapping(value = "/parsing", method = RequestMethod.GET)
	public String parsingManage(AccountVO accountVo, HttpSession session, HttpServletResponse response, Locale locale, MapColumnsVO vo, HttpServletRequest req, ModelAndView mav, Model model, MapVO mapVo) throws IOException {
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		String formattedDate = dateFormat.format(date);

		model.addAttribute("serverTime", formattedDate);

		List<MapVO> location = mapService.getMapGrid(mapVo);
		model.addAttribute("location", location);
		
		//계정 세션에 있는 store_seq를 불러와서 맵나타내기
		//일단 최대값 최소값 불러내어 테이블 사이즈 잡기
		AccountVO accountVO = (AccountVO) session.getAttribute("loginAccount");
//		System.out.println("accountVO.getStore_seq() = "+accountVO.getStore_seq());
		int storeSeq = accountVO.getStore_seq();
		int maxX;
		int maxY;
		
		List<MapViewVO> mapView = mapService.getMapView(storeSeq);
		maxX = mapService.getMaxNodeX(storeSeq);
		maxY = mapService.getMaxNodeY(storeSeq);
		
		int nodeSize = mapView.size();
		session.setAttribute("nodeSize", nodeSize);
		
		for(int i = 1; i <= mapView.size(); i++) {
			session.setAttribute("mapView"+i, mapView.get(i-1));
		}
		
//		System.out.println("mapView.get(0)= "+ mapView.get(0));
//		System.out.println("mapView.size()= "+ mapView.size());
//		System.out.println("max_mode_x = "+ maxX);
//		System.out.println("max_mode_y = "+ maxY);
		
		MapSizeVO mapSize = new MapSizeVO();
		mapSize.setMaxX(maxX);
		mapSize.setMaxY(maxY);
		session.setAttribute("mapSize", mapSize);
		session.setAttribute("moveRobotId", inputRobotId);
		
		model.addAttribute("inputRobotId", inputRobotId);
		model.addAttribute("inputCurrentNode", inputCurrentNode);
		model.addAttribute("inputFinalNode", inputFinalNode);
		model.addAttribute("inputRequestion", inputRequestion);

		return "home";
	}
}
