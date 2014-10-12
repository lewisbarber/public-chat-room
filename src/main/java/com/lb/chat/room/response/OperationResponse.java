package com.lb.chat.room.response;

import com.lb.chat.room.constant.SuccessCode;

public class OperationResponse {

	private String opCode = "S0000";
	private String opMessage = SuccessCode.S0000;

	public OperationResponse() {

	}

	public String getOpCode() {
		return opCode;
	}

	public void setOpCode(String opCode) {
		this.opCode = opCode;
	}

	public String getOpMessage() {
		return opMessage;
	}

	public void setOpMessage(String opMessage) {
		this.opMessage = opMessage;
	}

}