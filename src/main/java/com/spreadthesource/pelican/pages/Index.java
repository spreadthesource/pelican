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


public class Index
{
	@Property
	private Item currentItem;
	
	@Property
	private List<Item> listOfItems;
	
	@Inject
	private Session session;
	
	
	@SetupRender
	public void setupRender(){
		listOfItems = session.createCriteria(Item.class).addOrder(Order.asc("id")).list();
	}
	
	@CommitAfter
	@OnEvent(value=EventConstants.ACTION,component="increase")
	public void increase(long id){
		
		Item item = (Item)session.createCriteria(Item.class).add(Restrictions.eq("id", id)).uniqueResult();
		item.setPrice(item.getPrice()+1);
		session.saveOrUpdate(item);
		
		//PUSH
		
	}
	
}