package com.spreadthesource.pelican.pages;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.BeanEditForm;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.spreadthesource.pelican.entities.User;

public class Login {

	@SessionState
	private User loggedUser;
	
	@Property
	private User user;
	
	@Inject
	private Session session;
	
	@InjectComponent
	private BeanEditForm beaneditform;
	
	@OnEvent(EventConstants.VALIDATE_FORM)
	public Object verifyIfUserExists(){
		User u = (User)session.createCriteria(User.class).add(Restrictions.eq("name", user.getName())).uniqueResult();
		
		if(u==null){
			beaneditform.recordError("user doesn't exist");
			return null;
		}
		
		if(!u.getPassword().equals(user.getPassword())){
			beaneditform.recordError("wrong password");
			return null;
		}
		
		loggedUser = u;
		return Index.class;
		
	}
}
