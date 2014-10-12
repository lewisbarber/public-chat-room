package com.lb.chat.room.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.lb.chat.room.constant.ErrorCode;
import com.lb.chat.room.model.ClientSession;
import com.lb.chat.room.model.Message;
import com.lb.chat.room.repository.ClientSessionRepository;
import com.lb.chat.room.repository.MessageRepository;
import com.lb.chat.room.response.RemoveClientSessionResponse;
import com.lb.chat.room.response.SaveNewClientSessionResponse;

@Service
public class ClientSessionServiceImpl implements ClientSessionService {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory
			.getLogger(ClientSessionServiceImpl.class);

	@Autowired
	ClientSessionRepository clientSessionRepository;

	@Autowired
	MessageRepository messageRepository;

	@Autowired
	SimpMessagingTemplate messagingTemplate;

	@Override
	public SaveNewClientSessionResponse saveNewClientSession(String login,
			String username, String sessionId) {

		SaveNewClientSessionResponse response = new SaveNewClientSessionResponse();

		if (Strings.isNullOrEmpty(login) || Strings.isNullOrEmpty(username)
				|| Strings.isNullOrEmpty(sessionId)) {

			response.setOpCode("E0010");
			response.setOpMessage(ErrorCode.E0010);

			return response;

		}

		ClientSession session = clientSessionRepository
				.findBySessionId(sessionId);

		ClientSession checkUsername = clientSessionRepository
				.findByUsername(username);

		if (checkUsername != null) {
			clientSessionRepository.delete(checkUsername);
		}

		if (session == null) {

			ClientSession newClientSession = new ClientSession();

			newClientSession.setSessionId(sessionId);
			newClientSession.setLogin(login);
			newClientSession.setUsername(username);

			clientSessionRepository.save(newClientSession);

		}

		return response;

	}

	@Override
	public RemoveClientSessionResponse removeClientSession(String sessionId) {

		RemoveClientSessionResponse response = new RemoveClientSessionResponse();

		ClientSession session = clientSessionRepository
				.findBySessionId(sessionId);

		if (session == null) {

			response.setOpCode("E0011");
			response.setOpMessage(ErrorCode.E0011);

			return response;

		}

		List<Message> clientMessages = messageRepository
				.findByClientSession(session);

		for (Message message : clientMessages) {
			message.setClientSession(null);
		}

		if (session != null) {

			messageRepository.save(clientMessages);
			clientSessionRepository.delete(session);

		}

		return response;

	}

}