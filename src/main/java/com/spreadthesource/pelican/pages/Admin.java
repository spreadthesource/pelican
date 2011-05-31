package com.spreadthesource.pelican.pages;

import java.util.List;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.spreadthesource.pelican.entities.Item;


public class Admin
{
	@Property
	private Item item, currentItem;
	
	@Property
	private List<Item> listOfItems;
	
	@Inject
	private Session session;
	
	@CommitAfter
	@OnEvent(EventConstants.SUCCESS)
	public void formSuccess(){
		session.persist(item);
	}
	
	@OnEvent(EventConstants.ACTIVATE)
	public void initializeItem(long i){
		item = (Item)session.createCriteria(Item.class).add(Restrictions.eq("id", i)).uniqueResult();
	}
	@SetupRender
	public void setupRender(){
		listOfItems = session.createCriteria(Item.class).addOrder(Order.asc("id")).list();
	}
}