package com.lb.chat.room.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.lb.chat.room.constant.ErrorCode;
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

@Service
public class MessageServiceImpl implements MessageService {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory
			.getLogger(MessageServiceImpl.class);

	@Autowired
	ClientSessionRepository clientSessionRepository;

	@Autowired
	MessageRepository messageRepository;

	@Autowired
	SimpMessagingTemplate messagingTemplate;

	@Override
	public FetchMessagesResponse fetchMessages(FetchMessagesDTO fetchMessages) {

		FetchMessagesResponse response = new FetchMessagesResponse();

		if (fetchMessages == null) {

			response.setOpCode("E0004");
			response.setOpMessage(ErrorCode.E0004);

			return response;

		}

		ClientSession clientSession = clientSessionRepository
				.findByUsername(fetchMessages.getUsername());

		if (clientSession != null) {

			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.HOUR_OF_DAY, -24);

			Date date = calendar.getTime();

			List<Message> allMessages = messageRepository
					.findByDateSentGreaterThan(date);

			response.setMessages(allMessages);

			try {

				messagingTemplate.convertAndSendToUser(
						clientSession.getLogin(), "/messages/receive",
						allMessages);

			} catch (Exception e) {

				e.printStackTrace();

				response.setOpCode("E0002");
				response.setOpMessage(ErrorCode.E0002);

				return response;

			}

		} else {

			response.setOpCode("E0001");
			response.setOpMessage(ErrorCode.E0001);

			return response;

		}

		return response;

	}

	@Override
	public SendMessageResponse sendMessage(NewMessageDTO inboundMessage) {

		SendMessageResponse response = new SendMessageResponse();

		if (inboundMessage == null) {

			response.setOpCode("E0007");
			response.setOpMessage(ErrorCode.E0007);

			return response;

		}

		if (Strings.isNullOrEmpty(inboundMessage.getMessage())) {

			response.setOpCode("E0005");
			response.setOpMessage(ErrorCode.E0005);

			return response;

		}

		ClientSession clientSession = clientSessionRepository
				.findByUsername(inboundMessage.getUsername());

		if (clientSession == null) {

			response.setOpCode("E0006");
			response.setOpMessage(ErrorCode.E0006);

			return response;

		}

		Message message = new Message();

		message.setClientSession(clientSession);
		message.setDateSent(new Date());
		message.setMessage(inboundMessage.getMessage());
		message.setUsername(inboundMessage.getUsername());

		Message savedMessage = null;
		try {

			savedMessage = messageRepository.save(message);

		} catch (Exception e) {

			response.setOpCode("E0003");
			response.setOpMessage(ErrorCode.E0003);

			return response;

		}

		try {

			messagingTemplate.convertAndSend("/message/receive", savedMessage);

		} catch (Exception e) {

			response.setOpCode("E0002");
			response.setOpMessage(ErrorCode.E0002);

			return response;

		}

		return response;

	}

	@Override
	public UpdateMessageSeenByResponse updateSeenBy(
			MessageSeenByDTO messageSeenBy) {

		UpdateMessageSeenByResponse response = new UpdateMessageSeenByResponse();

		if (messageSeenBy == null) {

			response.setOpCode("E0009");
			response.setOpMessage(ErrorCode.E0009);

			return response;

		}

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR_OF_DAY, -24);

		Date date = calendar.getTime();

		ClientSession seenByClient = clientSessionRepository
				.findByUsername(messageSeenBy.getUsername());

		if (seenByClient == null) {

			response.setOpCode("E0008");
			response.setOpMessage(ErrorCode.E0008);

			return response;

		}

		List<Message> latestMessages = messageRepository
				.findByDateSentGreaterThan(date);

		if (latestMessages.size() > 0) {

			Message message = latestMessages.get((latestMessages.size() - 1));

			String currentSeenBy = message.getSeenBy();

			if (currentSeenBy == null) {

				if (!messageSeenBy.getUsername().equals(message.getUsername())) {
					message.setSeenBy(messageSeenBy.getUsername());
				}

			} else {

				if (!currentSeenBy.contains(messageSeenBy.getUsername())
						&& !currentSeenBy.contains(messageSeenBy.getUsername()
								+ ",")
						&& !currentSeenBy.contains(", "
								+ messageSeenBy.getUsername())
						&& !messageSeenBy.getUsername().equals(
								message.getUsername())) {

					message.setSeenBy(currentSeenBy + ", "
							+ messageSeenBy.getUsername());

				}

			}

			messageRepository.save(message);

			try {

				messagingTemplate.convertAndSend("/message/new-seen-by",
						message);

			} catch (Exception e) {

				response.setOpCode("E0002");
				response.setOpMessage(ErrorCode.E0002);

				return response;

			}

		}

		return response;

	}

	@Override
	public void convertAndSend(String path, List<?> list) {

		messagingTemplate.convertAndSend(path, list);

	}

	@Override
	public void convertAndSend(String path, Object object) {

		messagingTemplate.convertAndSend(path, object);

	}

}