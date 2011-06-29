package com.spreadthesource.pelican.pages;

import java.io.File;
import java.util.List;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ApplicationGlobals;
import org.apache.tapestry5.upload.services.UploadedFile;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.spreadthesource.pelican.entities.Item;
import com.spreadthesource.pelican.entities.User;


public class Admin
{
	@Property
	private Item currentItem;
	
	@Property
	private String name;
	
	@Property
	private String description;
	
	@Property
	private long price;
	
	@Property
	private List<Item> listOfItems;
	
	@SessionState
	private User user;
	
	private boolean userExists;
	
	@Property
    private UploadedFile file;

	
	@Inject
	private Session session;
	
	@Inject
	private ApplicationGlobals globals;
	
	@CommitAfter
	@OnEvent(EventConstants.SUCCESS)
	public void formSuccess(){
		Item item = new Item();
		item.setDescription(description);
		item.setName(name);
		item.setPrice(price);
		item.setUser(user);
		item.setImage(file.getFileName());

		File copied = new File(globals.getContext().getRealFile("/static/uploadedPictures").getAbsolutePath()+ File.separator +  file.getFileName());

        file.write(copied);
		
		session.persist(item);
	}
	
	@OnEvent(EventConstants.ACTIVATE)
	public Object initializeItem(long i){
		if(!userExists)
			return Login.class;
		Item currentItem = (Item)session.createCriteria(Item.class).add(Restrictions.eq("id", i)).uniqueResult();
		
		price = currentItem.getPrice();
		name = currentItem.getName();
		description = currentItem.getDescription();
		
		return null;
	}
	@SetupRender
	public void setupRender(){
		listOfItems = session.createCriteria(Item.class).addOrder(Order.asc("id")).list();
	}
}