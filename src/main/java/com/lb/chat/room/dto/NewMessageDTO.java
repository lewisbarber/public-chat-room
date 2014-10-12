package com.lb.chat.room.dto;

public class NewMessageDTO {

	private String message;
	private String username;

	public NewMessageDTO() {

	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}