package com.spreadthesource.pelican.services;

import java.util.Calendar;
import java.util.Date;

import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.spreadthesource.pelican.entities.Item;
import com.spreadthesource.pelican.entities.User;

public class StartupServiceImpl implements StartupService
{

    public StartupServiceImpl(Session session, HibernateSessionManager sessionManager)
    {
    	//if a user named "joe" already exists in the database, we assume we don't have 
    	//to create new users and new items in the database
    	if (session.createCriteria(User.class).add(Restrictions.eq("name", "joe")).uniqueResult() !=null)
    	    return;
    		
        User joe = new User();
        joe.setName("joe");
        joe.setEmail("joe@email.com");
        joe.setPassword("password");
        
        
        User howard = new User();
        howard.setName("howard");
        howard.setEmail("howard@email.com");
        howard.setPassword("password");
        
        
        session.persist(joe);
        session.persist(howard);
        
        Calendar inTenMinuts = Calendar.getInstance();
        inTenMinuts.add(Calendar.MINUTE, 10);
        Date inTenMinutsDate = new Date(inTenMinuts.getTimeInMillis()) ;
        
        Item bicycle = new Item();
        bicycle.setDescription("My grand father bicycle");
        bicycle.setName("Old bicycle");
        bicycle.setPrice(30);
        bicycle.setExpireDate(inTenMinutsDate);
        // http://www.flickr.com/photos/sandcastlematt/458258255
        bicycle.setImage("http://farm1.staticflickr.com/253/458258255_6dec9b8010_q.jpg");
        
        Item car = new Item();
        car.setDescription("A collection car");
        car.setName("Car");
        car.setPrice(340);
        car.setExpireDate(inTenMinutsDate);
        // http://www.flickr.com/photos/1stpix_diecast_dioramas/4906973624
        car.setImage("http://farm5.staticflickr.com/4099/4906973624_f416f59422_q.jpg");
        
        
        
        session.persist(bicycle);
        session.persist(car);
        
        sessionManager.commit();

    }
}