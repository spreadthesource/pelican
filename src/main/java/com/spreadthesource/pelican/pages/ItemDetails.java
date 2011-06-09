package com.spreadthesource.pelican.pages;

import java.util.Date;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.spreadthesource.pelican.entities.Bid;
import com.spreadthesource.pelican.entities.Item;
import com.spreadthesource.pelican.entities.User;

public class ItemDetails {

	@Property
	private Item item;
	
	@Component
	private Form form; 
	
	@SessionState
	@Property
	private User user;
	
	private boolean userExists;
	
	@Property
	private long bidValue;
	
	@Inject
	private Session session;
	
	@SetupRender
	public void setupRender(){
		bidValue=getMaxPrice(item);
	}
	
	@OnEvent(EventConstants.ACTIVATE)
	public Object setItemFromUrl(long id){
		
		if(!userExists) return Login.class;
		
		item=(Item)session.createCriteria(Item.class).add(Restrictions.eq("id", id)).uniqueResult();
	
		return null;
	}
	
	@OnEvent(EventConstants.PASSIVATE)
	public long putToUrl(){
		return item.getId();
	}
	
	@OnEvent(value=EventConstants.VALIDATE, component="form")
	public void validateForm(){
		System.out.print("$$$$$$$$ "+item.getName());
		long price = getMaxPrice(item);
		System.out.println("Price = " + price + " newBid = " + bidValue);
		if(price>=bidValue)
			form.recordError("Your new bid has to be greater than initial price!!");
	}
	
	@CommitAfter
	@OnEvent(EventConstants.SUBMIT)
	public void submitForm(){
		
		if(form.isValid()){
			Bid	bid = new Bid();
			bid.setDate(new Date());
			bid.setItem(item);
			bid.setPrice(bidValue);
			bid.setUser(user);
			
			session.persist(bid);
		}
	}
	
	public long getMaxPrice(Item item){
		
		System.out.print("$$$$$$$$$"+item.getName());
		
		
		Query query = (Query)session.getNamedQuery("com.spreadthesource.pelican.entities.Bid.findMaxBid");
		
		
		query.setParameter("item", item);
		
		long price = 0;
		
		try {
			price = (Long) query.uniqueResult();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			price = item.getPrice();
		}
		
		return price;
	}
	
}
