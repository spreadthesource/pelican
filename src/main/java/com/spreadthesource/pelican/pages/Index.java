package com.spreadthesource.pelican.pages;

import java.util.List;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.slf4j.Logger;

import com.spreadthesource.pelican.entities.Item;
import com.spreadthesource.pelican.entities.User;


public class Index
{
	@SessionState
	@Property
	private User loggedUser;
	
	@Property
	private boolean loggedUserExists;
	
	@Inject
	private Logger logger;
	
	@Property
	private Item currentItem;
	
	@Property
	private List<Item> listOfItems;	
	
	@Inject
	private Session session;
	
	@OnEvent(EventConstants.ACTIVATE)
	public Object checkConnected(){
		
		if(!loggedUserExists) return Login.class;
		
		return null;
	}
	
	@SetupRender
	public void setupRender(){
		
		listOfItems = session.createCriteria(Item.class).addOrder(Order.asc("id")).list();
	}
	
	public long getMaxPrice(){
		return getMaxPrice(currentItem);
	}
	
	public long getMaxPrice(Item item){
		
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