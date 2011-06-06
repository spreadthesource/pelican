package com.spreadthesource.pelican.pages;

import java.util.Date;
import java.util.List;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import com.spreadthesource.pelican.entities.Bid;
import com.spreadthesource.pelican.entities.Item;
import com.spreadthesource.pelican.entities.User;


public class Index
{
	@SessionState
	@Property
	private User user;
	
	@Property
	private boolean userExists;
	
	@Inject
	private Logger logger;
	
	@Property
	private Item currentItem;
	
	@Property
	private Bid bid;
	
	@Property
	private List<Item> listOfItems;
	
	@Property
	private List<Bid> listOfBids;
	
	@Inject
	private Session session;
	
	@Property
	private BeanModel<Bid> model;
	
	@Inject
	private BeanModelSource beanModelSource;
	
	@Inject 
	private Messages messages;
	
	@OnEvent(EventConstants.ACTIVATE)
	public Object checkConnected(){
		
		if(!userExists) return Login.class;
		
		return null;
	}
	
	@SetupRender
	public void setupRender(){
		
		listOfItems = session.createCriteria(Item.class).addOrder(Order.asc("id")).list();
		
		listOfBids = session.createCriteria(Bid.class).addOrder(Order.asc("id")).list();
	}
	
	@CommitAfter
	@OnEvent(value=EventConstants.ACTION,component="increase")
	public void increase(long id){
		
		Item item = (Item)session.createCriteria(Item.class).add(Restrictions.eq("id", id)).uniqueResult();
		item.setPrice(item.getPrice()+1);
		session.saveOrUpdate(item);
		
		Bid	bid = new Bid();
		bid.setDate(new Date());
		bid.setItem(item);
		bid.setPrice(item.getPrice());
		bid.setUser(user);
		
		session.persist(bid);
		
		

		//PUSH
		
	}
	
}