package com.lb.chat.room.service;

import com.lb.chat.room.response.RemoveClientSessionResponse;
import com.lb.chat.room.response.SaveNewClientSessionResponse;

public interface ClientSessionService {

	SaveNewClientSessionResponse saveNewClientSession(String login,
			String username, String sessionId);

	RemoveClientSessionResponse removeClientSession(String sessionId);

}