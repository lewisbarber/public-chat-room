package com.lb.chat.room.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "message")
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "messageId", nullable = false)
	private Integer messageId;

	@ManyToOne(cascade = CascadeType.DETACH)
	@Fetch(value = FetchMode.JOIN)
	@JoinColumn(name = "clientSessionId", nullable = true)
	private ClientSession clientSession;

	@Column(name = "dateSent", nullable = false)
	private Date dateSent;

	@Column(name = "message", nullable = false, length = 4000)
	private String message;

	@Column(name = "username", nullable = false)
	private String username;

	@Column(name = "seenBy", nullable = true)
	private String seenBy;

	public Message() {

	}

	public Integer getMessageId() {
		return messageId;
	}

	public void setMessageId(Integer messageId) {
		this.messageId = messageId;
	}

	public Date getDateSent() {
		return dateSent;
	}

	public void setDateSent(Date dateSent) {
		this.dateSent = dateSent;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ClientSession getClientSession() {
		return clientSession;
	}

	public void setClientSession(ClientSession clientSession) {
		this.clientSession = clientSession;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSeenBy() {
		return seenBy;
	}

	public void setSeenBy(String seenBy) {
		this.seenBy = seenBy;
	}

}