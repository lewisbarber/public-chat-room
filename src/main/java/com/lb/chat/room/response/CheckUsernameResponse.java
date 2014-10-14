package com.lb.chat.room.response;

public class CheckUsernameResponse extends OperationResponse {

	private Boolean usernameInUse = false;

	public CheckUsernameResponse() {

	}

	public Boolean getUsernameInUse() {
		return usernameInUse;
	}

	public void setUsernameInUse(Boolean usernameInUse) {
		this.usernameInUse = usernameInUse;
	}

}