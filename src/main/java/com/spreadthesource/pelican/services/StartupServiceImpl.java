package com.spreadthesource.pelican.services;

import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.hibernate.Session;

import com.spreadthesource.pelican.entities.User;

public class StartupServiceImpl implements StartupService
{

    public StartupServiceImpl(Session session, HibernateSessionManager sessionManager)
    {

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
        
        sessionManager.commit();
    }
}