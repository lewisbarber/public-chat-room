package com.lb.chat.room.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/")
public class IndexController {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory
			.getLogger(IndexController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String root() {
		return "index";
	}

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index() {
		return "index";
	}

}