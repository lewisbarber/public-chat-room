package com.lb.chat.room.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lb.chat.room.model.ClientSession;
import com.lb.chat.room.model.Message;

public interface MessageRepository extends JpaRepository<Message, Integer> {

	List<Message> findByClientSession(ClientSession session);

	List<Message> findByDateSentGreaterThan(Date date);

}