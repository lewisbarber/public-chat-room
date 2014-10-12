package com.lb.chat.room.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.lb.chat.room.dto.FetchMessagesDTO;
import com.lb.chat.room.dto.MessageSeenByDTO;
import com.lb.chat.room.dto.NewMessageDTO;
import com.lb.chat.room.service.MessageService;

@Controller
public class MessageController {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory
			.getLogger(MessageController.class);

	@Autowired
	MessageService messageService;

	@MessageMapping("/message/send")
	public void staff(NewMessageDTO inboundMessage) throws Exception {

		messageService.sendMessage(inboundMessage);

	}

	@MessageMapping("/messages/fetch")
	public void staff(FetchMessagesDTO fetchMessages) throws Exception {

		messageService.fetchMessages(fetchMessages);

	}

	@MessageMapping("/message/update-seen-by")
	public void staff(MessageSeenByDTO messageSeenBy) throws Exception {

		messageService.updateSeenBy(messageSeenBy);

	}

}