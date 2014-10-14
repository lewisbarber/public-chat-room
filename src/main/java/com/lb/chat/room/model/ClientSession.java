package com.lb.chat.room.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "clientSession")
public class ClientSession {

	@Id
	@Column(name = "sessionId", nullable = false)
	private String sessionId;

	@Column(name = "login", nullable = false)
	private String login;

	@Column(name = "username", nullable = false)
	private String username;

	@Column(name = "dateAdded", nullable = false)
	private Date dateAdded = new Date();

	@Column(name = "idle", nullable = false)
	private Boolean idle = false;

	public ClientSession() {

	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public Date getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Boolean getIdle() {
		return idle;
	}

	public void setIdle(Boolean idle) {
		this.idle = idle;
	}

}