package com.lb.chat.room.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;

import com.lb.chat.room.service.ClientSessionService;

public class PresenceChannelInterceptor extends ChannelInterceptorAdapter {

	private static final Logger logger = LoggerFactory
			.getLogger(PresenceChannelInterceptor.class);

	@Autowired
	ClientSessionService clientSessionService;

	@Override
	public void postSend(Message<?> message, MessageChannel channel,
			boolean sent) {

		StompHeaderAccessor sha = StompHeaderAccessor.wrap(message);

		if (sha.getCommand() == null) {
			return;
		}

		String login = sha.getLogin();
		String username = sha.getPasscode();
		String sessionId = sha.getSessionId();

		switch (sha.getCommand()) {

		case CONNECT:

			if (username != null) {
				clientSessionService.saveNewClientSession(login, username,
						sessionId);
			}

			logger.info("STOMP Connect [sessionId: " + sessionId + "]");

			break;

		case CONNECTED:

			if (username != null) {
				clientSessionService.saveNewClientSession(login, username,
						sessionId);
			}

			logger.info("STOMP Connected [sessionId: " + sessionId + "]");

			break;

		case DISCONNECT:

			clientSessionService.removeClientSession(sessionId);

			logger.info("STOMP Disconnect [sessionId: " + sessionId + "]");

			break;

		default:

			break;

		}
	}

}