package com.spreadthesource.pelican.pages;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;

import com.spreadthesource.pelican.entities.User;

public class Register {

	@Property
	private String email;
	
	@Property
	private String name;
	
	@Property
	private String password;
	
	@SessionState
	private User loggedUser;
	
	@Inject
	private Session session;
	
	@CommitAfter
	@OnEvent(EventConstants.SUCCESS)
	public Object register(){
		User user = new User();
		user.setName(name);
		user.setEmail(email);
		user.setPassword(password);
		session.persist(user);
		loggedUser = user;
		return Index.class;
	}
}
