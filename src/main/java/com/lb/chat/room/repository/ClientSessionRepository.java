package com.lb.chat.room.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lb.chat.room.model.ClientSession;

public interface ClientSessionRepository extends
		JpaRepository<ClientSession, String> {

	ClientSession findBySessionId(String sessionId);

	ClientSession findByUsername(String username);

}