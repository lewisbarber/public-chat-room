package com.lb.chat.room.response;

import java.util.List;

import com.lb.chat.room.model.Message;

public class FetchMessagesResponse extends OperationResponse {

	private List<Message> messages;

	public FetchMessagesResponse() {

	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

}