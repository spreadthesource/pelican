package com.spreadthesource.pelican.pages;

import java.util.Date;
import java.util.List;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.ajax.MultiZoneUpdate;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.AssetSource;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import com.spreadthesource.pelican.entities.Bid;
import com.spreadthesource.pelican.entities.Item;
import com.spreadthesource.pelican.entities.User;

@Import(stylesheet={"context:css/index.css"})
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
	
	//@Property
	private List<Item> listOfItems;	
	
	@Inject
	private Session session;
	
	@OnEvent(EventConstants.ACTIVATE)
	public Object checkConnected(){
		
		if(!loggedUserExists) return Login.class;
		
		return null;
	}
	
	public List<Item> getlistOfItems(){
		return session.createCriteria(Item.class).addOrder(Order.asc("id")).list();
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
	
	@Inject 
	private AssetSource assetSource;
	
	public String getImageOfItem(){
		Asset image = assetSource.getContextAsset("static/uploadedPictures/"+currentItem.getImage(), null);
		return image.toClientURL();
	}
	
	
	@Property
	private Item item;
	
	
	@SessionState
	@Property
	private User user;
	
	private boolean userExists;
	
	@Property
	private long bidValue;
	
	@Component
	private Zone itemZone;
	
	@Component
	private Zone list;
	
	
	@OnEvent(value=EventConstants.ACTION, component="ajax")
	public Object setItemFromUrl(long id){
		
		if(!userExists) return Login.class;
		
		item=(Item)session.createCriteria(Item.class).add(Restrictions.eq("id", id)).uniqueResult();
		bidValue=getMaxPrice(item);
		return itemZone.getBody();
	}
	
	
	
	
	
	
	@CommitAfter
	@OnEvent(value="add")
	public Object addBid(int bidAmmnt, long itemId){
		item=(Item)session.createCriteria(Item.class).add(Restrictions.eq("id", itemId)).uniqueResult();
		long price = getMaxPrice(item);
		//if(price>=bidValue)
			
		Bid	bid = new Bid();
		bid.setDate(new Date());
		bid.setItem(item);
		bid.setPrice(price+bidAmmnt);
		bid.setUser(user);
		
		session.persist(bid);
		return new MultiZoneUpdate("itemZone",itemZone).add("list", list);
		//return itemZone.getBody();
	}
	

	
}