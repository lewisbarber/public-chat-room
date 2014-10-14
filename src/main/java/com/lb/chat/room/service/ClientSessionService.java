package com.lb.chat.room.service;

import com.lb.chat.room.response.CheckUsernameResponse;
import com.lb.chat.room.response.FetchClientSessionsResponse;
import com.lb.chat.room.response.RemoveClientSessionResponse;
import com.lb.chat.room.response.SaveNewClientSessionResponse;

public interface ClientSessionService {

	SaveNewClientSessionResponse saveNewClientSession(String login,
			String username, String sessionId);

	RemoveClientSessionResponse removeClientSession(String sessionId);

	CheckUsernameResponse checkUsername(String username);

	FetchClientSessionsResponse fetchClientSessions();

}