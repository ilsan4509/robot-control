package org.robot.project;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.robot.project.account.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/account")
public class Account {
	//로그인 / 로그아웃 페이지
	@Resource(name = "AccountService")
	AccountService accountService;
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ModelAndView login( ModelAndView mav, HttpSession session, HttpServletResponse response ) {
		mav.setViewName("login");
		return mav;
	}
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public ModelAndView logout(HttpSession session, ModelAndView mav) {
		session.invalidate();
		mav.setViewName("login");
		return mav;
	}
}
