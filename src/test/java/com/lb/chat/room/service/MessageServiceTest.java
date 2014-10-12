package com.lb.chat.room.service;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.lb.chat.room.dto.FetchMessagesDTO;
import com.lb.chat.room.dto.MessageSeenByDTO;
import com.lb.chat.room.dto.NewMessageDTO;
import com.lb.chat.room.model.ClientSession;
import com.lb.chat.room.model.Message;
import com.lb.chat.room.repository.ClientSessionRepository;
import com.lb.chat.room.repository.MessageRepository;
import com.lb.chat.room.response.FetchMessagesResponse;
import com.lb.chat.room.response.SendMessageResponse;
import com.lb.chat.room.response.UpdateMessageSeenByResponse;

public class MessageServiceTest {

	@InjectMocks
	MessageServiceImpl messageService;

	@Mock
	ClientSessionRepository clientSessionRepository;

	@Mock
	MessageRepository messageRepository;

	@Mock
	SimpMessagingTemplate messagingTemplate;

	@Before
	public void setup() {

		MockitoAnnotations.initMocks(this);

	}

	@Test
	public void cannotFetchMessagesWithInvalidUsername() {

		String username = null;

		FetchMessagesDTO fetchMessages = new FetchMessagesDTO();

		fetchMessages.setUsername(username);

		when(clientSessionRepository.findByUsername(username)).thenReturn(null);

		try {

			FetchMessagesResponse response = messageService
					.fetchMessages(fetchMessages);

			if (response.getOpCode().contains("S")
					|| response.getMessages() != null) {
				fail();
			}

		} catch (Exception e) {

			e.printStackTrace();
			fail();

		}

	}

	@Test
	public void cannotFetchMessagesWithInvalidFetchMessageDTO() {

		FetchMessagesDTO fetchMessages = null;

		try {

			FetchMessagesResponse response = messageService
					.fetchMessages(fetchMessages);

			if (!response.getOpCode().contains("E0004")) {
				fail();
			}

		} catch (Exception e) {

			e.printStackTrace();
			fail();

		}

	}

	@Test
	public void canFetchMessagesWithValidUsername() {

		String username = "john.smith";

		FetchMessagesDTO fetchMessages = new FetchMessagesDTO();

		fetchMessages.setUsername(username);

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR_OF_DAY, -24);

		Date date = calendar.getTime();

		ClientSession clientSession = new ClientSession();
		List<Message> allMessages = new ArrayList<Message>();

		when(clientSessionRepository.findByUsername(username)).thenReturn(
				clientSession);
		when(messageRepository.findByDateSentGreaterThan(date)).thenReturn(
				allMessages);

		Mockito.doNothing()
				.when(messagingTemplate)
				.convertAndSendToUser(clientSession.getLogin(),
						"/messages/receive", allMessages);

		try {

			FetchMessagesResponse response = messageService
					.fetchMessages(fetchMessages);

			if (!response.getOpCode().contains("S")
					|| response.getMessages() == null) {
				fail();
			}

		} catch (Exception e) {

			e.printStackTrace();
			fail();

		}

	}

	@Test
	public void cannotSendMessageWithInvalidMessage() {

		NewMessageDTO inboundMessage = new NewMessageDTO();

		inboundMessage.setMessage(null);
		inboundMessage.setUsername("john.smith");

		try {

			SendMessageResponse response = messageService
					.sendMessage(inboundMessage);

			if (!response.getOpCode().contains("E0005")) {
				fail();
			}

		} catch (Exception e) {

			e.printStackTrace();
			fail();

		}

	}

	@Test
	public void cannotSendMessageWithInvalidUsername() {

		NewMessageDTO inboundMessage = new NewMessageDTO();

		inboundMessage.setMessage("Hello World!");
		inboundMessage.setUsername(null);

		try {

			SendMessageResponse response = messageService
					.sendMessage(inboundMessage);

			if (!response.getOpCode().contains("E0006")) {
				fail();
			}

		} catch (Exception e) {

			e.printStackTrace();
			fail();

		}

	}

	@Test
	public void cannotSendMessageWithInvalidNewMessageDTO() {

		NewMessageDTO inboundMessage = null;

		try {

			SendMessageResponse response = messageService
					.sendMessage(inboundMessage);

			if (!response.getOpCode().contains("E0007")) {
				fail();
			}

		} catch (Exception e) {

			e.printStackTrace();
			fail();

		}

	}

	@Test
	public void canSendMessageWithValidMessageAndUsername() {

		NewMessageDTO inboundMessage = new NewMessageDTO();

		inboundMessage.setMessage("Hello World!");
		inboundMessage.setUsername("john.smith");

		ClientSession clientSession = new ClientSession();

		when(
				clientSessionRepository.findByUsername(inboundMessage
						.getUsername())).thenReturn(clientSession);

		Mockito.doNothing().when(messagingTemplate)
				.convertAndSend("/message/receive", new Message());

		try {

			SendMessageResponse response = messageService
					.sendMessage(inboundMessage);

			if (!response.getOpCode().contains("S0000")) {
				fail();
			}

		} catch (Exception e) {

			e.printStackTrace();
			fail();

		}

	}

	@Test
	public void cannotUpdateSeenByWithInvalidUsername() {

		MessageSeenByDTO messageSeenBy = new MessageSeenByDTO();

		messageSeenBy.setUsername(null);

		try {

			UpdateMessageSeenByResponse response = messageService
					.updateSeenBy(messageSeenBy);

			if (!response.getOpCode().contains("E0008")) {
				fail();
			}

		} catch (Exception e) {

			e.printStackTrace();
			fail();

		}

	}

	@Test
	public void cannotUpdateSeenByWithInvalidMessageSeenByDTO() {

		MessageSeenByDTO messageSeenBy = null;

		try {

			UpdateMessageSeenByResponse response = messageService
					.updateSeenBy(messageSeenBy);

			if (!response.getOpCode().contains("E0009")) {
				fail();
			}

		} catch (Exception e) {

			e.printStackTrace();
			fail();

		}

	}

	@Test
	public void canUpdateSeenByWithValidUsername() {

		MessageSeenByDTO messageSeenBy = new MessageSeenByDTO();

		messageSeenBy.setUsername("john.smith");

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR_OF_DAY, -24);

		Date date = calendar.getTime();

		ClientSession clientSession = new ClientSession();
		List<Message> allMessages = new ArrayList<Message>();

		when(
				clientSessionRepository.findByUsername(messageSeenBy
						.getUsername())).thenReturn(clientSession);

		when(messageRepository.findByDateSentGreaterThan(date)).thenReturn(
				allMessages);

		Mockito.doNothing().when(messagingTemplate)
				.convertAndSend("/message/new-seen-by", new Message());

		try {

			UpdateMessageSeenByResponse response = messageService
					.updateSeenBy(messageSeenBy);

			if (!response.getOpCode().contains("S0000")) {
				fail();
			}

		} catch (Exception e) {

			e.printStackTrace();
			fail();

		}

	}

}