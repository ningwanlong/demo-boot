package com.boot.demo.core.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@CrossOrigin
@RequestMapping("/web")
public class WebPageController {
	
	@RequestMapping(value = {"/index",""})
	public String index1(String param) {
		
		System.out.println(param);
		
		return "index";
	}
	
	
	//用户注册协议
	@RequestMapping("/agreement/userRegister")
	public String userRegisterAgreement() {
		
		
		return "agreement/userRegister";
	}
	
	
}
