package com.lb.chat.room.service;

import com.lb.chat.room.dto.FetchMessagesDTO;
import com.lb.chat.room.dto.MessageSeenByDTO;
import com.lb.chat.room.dto.NewMessageDTO;
import com.lb.chat.room.response.FetchMessagesResponse;
import com.lb.chat.room.response.SendMessageResponse;
import com.lb.chat.room.response.UpdateMessageSeenByResponse;

public interface MessageService {

	FetchMessagesResponse fetchMessages(FetchMessagesDTO fetchMessages);

	SendMessageResponse sendMessage(NewMessageDTO inboundMessage);

	UpdateMessageSeenByResponse updateSeenBy(MessageSeenByDTO messageSeenBy);

}