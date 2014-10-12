package com.lb.chat.room.service;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.lb.chat.room.model.ClientSession;
import com.lb.chat.room.model.Message;
import com.lb.chat.room.repository.ClientSessionRepository;
import com.lb.chat.room.repository.MessageRepository;
import com.lb.chat.room.response.RemoveClientSessionResponse;
import com.lb.chat.room.response.SaveNewClientSessionResponse;

public class ClientSessionServiceTest {

	@InjectMocks
	ClientSessionServiceImpl clientSessionService;

	@Mock
	ClientSessionRepository clientSessionRepository;

	@Mock
	MessageRepository messageRepository;

	@Before
	public void setup() {

		MockitoAnnotations.initMocks(this);

	}

	@Test
	public void cannotSaveNewClientSessionWithEmptyLogin() {

		String login = null;
		String username = "john.smith";
		String sessionId = "_as72hda";

		try {

			SaveNewClientSessionResponse response = clientSessionService
					.saveNewClientSession(login, username, sessionId);

			if (!response.getOpCode().contains("E0010")) {
				fail();
			}

		} catch (Exception e) {

		}

	}

	@Test
	public void cannotSaveNewClientSessionWithEmptyUsername() {

		String login = "7d1abc50-5246-11e4-916c-0800200c9a66";
		String username = null;
		String sessionId = "_as72hda";

		try {

			SaveNewClientSessionResponse response = clientSessionService
					.saveNewClientSession(login, username, sessionId);

			if (!response.getOpCode().contains("E0010")) {
				fail();
			}

		} catch (Exception e) {

		}

	}

	@Test
	public void cannotSaveNewClientSessionWithEmptySessionId() {

		String login = "7d1abc50-5246-11e4-916c-0800200c9a66";
		String username = "john.smith";
		String sessionId = null;

		try {

			SaveNewClientSessionResponse response = clientSessionService
					.saveNewClientSession(login, username, sessionId);

			if (!response.getOpCode().contains("E0010")) {
				fail();
			}

		} catch (Exception e) {

		}

	}

	@Test
	public void canSaveNewClientSessionWithPopulatedFields() {

		String login = "7d1abc50-5246-11e4-916c-0800200c9a66";
		String username = "john.smith";
		String sessionId = "_as72hda";

		when(clientSessionRepository.findBySessionId(sessionId)).thenReturn(
				new ClientSession());

		when(clientSessionRepository.findByUsername(username)).thenReturn(
				new ClientSession());

		try {

			SaveNewClientSessionResponse response = clientSessionService
					.saveNewClientSession(login, username, sessionId);

			if (!response.getOpCode().contains("S0000")) {
				fail();
			}

		} catch (Exception e) {

			e.printStackTrace();
			fail();

		}

	}

	@Test
	public void cannotRemoveClientSessionWithInvalidSessionId() {

		String sessionId = null;

		ClientSession clientSession = null;

		when(clientSessionRepository.findBySessionId(sessionId)).thenReturn(
				clientSession);

		try {

			RemoveClientSessionResponse response = clientSessionService
					.removeClientSession(sessionId);

			if (!response.getOpCode().contains("E0011")) {
				fail();
			}

		} catch (Exception e) {

			e.printStackTrace();
			fail();

		}

	}

	@Test
	public void canRemoveClientSessionWithValidSessionId() {

		String sessionId = "_as72hda";

		List<Message> allMessages = new ArrayList<Message>();

		ClientSession clientSession = new ClientSession();

		when(clientSessionRepository.findBySessionId(sessionId)).thenReturn(
				clientSession);

		when(messageRepository.findByClientSession(clientSession)).thenReturn(
				allMessages);

		try {

			RemoveClientSessionResponse response = clientSessionService
					.removeClientSession(sessionId);

			if (!response.getOpCode().contains("S0000")) {
				fail();
			}

		} catch (Exception e) {

			e.printStackTrace();
			fail();

		}

	}

}