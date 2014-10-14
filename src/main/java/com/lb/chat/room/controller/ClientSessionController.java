package com.lb.chat.room.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lb.chat.room.response.OperationResponse;
import com.lb.chat.room.service.ClientSessionService;

@Controller
@RequestMapping(value = "/client-session")
public class ClientSessionController {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory
			.getLogger(ClientSessionController.class);

	@Autowired
	ClientSessionService clientSessionService;

	@RequestMapping(value = "/check-username", method = RequestMethod.POST)
	@ResponseBody
	public OperationResponse checkUsername(
			@RequestParam("username") String username) {

		return clientSessionService.checkUsername(username);

	}

	@MessageMapping("/client-sessions/fetch")
	public void staff() throws Exception {

		clientSessionService.fetchClientSessions();

	}

}