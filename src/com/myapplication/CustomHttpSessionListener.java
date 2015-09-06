package com.myapplication;

import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.apache.log4j.Logger;

/**
 * Listens for session events and adds or removes references to 
 * to the context scoped HashMap accordingly.
 * @author Ben Souther; ben@souther.us
 * @since Sun May  8 18:57:10 EDT 2005
 */


public class CustomHttpSessionListener implements HttpSessionListener{
	
	private static Logger log = Logger.getLogger(Logger.class.getName());
	public static volatile HashMap<String, HttpSession> activeUsers = null;

    public void init(ServletConfig config){
    	
    }

    /**
     * Adds sessions to the context scoped HashMap when they begin.
     */
    public void sessionCreated(HttpSessionEvent event){
        HttpSession    session = event.getSession();
        ServletContext context = session.getServletContext();
        activeUsers = (HashMap<String, HttpSession>) context.getAttribute("activeUsers");

        activeUsers.put(session.getId(), session);
        log.info("New SessionID: " + session.getId().toString());
        context.setAttribute("activeUsers", activeUsers);
        log.info("Users: " + activeUsers.keySet().toString());

    }

    /**
     * Removes sessions from the context scoped HashMap when they expire
     * or are invalidated.
     */
    public void sessionDestroyed(HttpSessionEvent event){
        HttpSession    session = event.getSession();
        ServletContext context = session.getServletContext();
        HashMap<String, HttpSession> activeUsers = (HashMap<String, HttpSession>)context.getAttribute("activeUsers");
        
        log.info("SessionID destroyed: " + session.getId().toString());
        
        activeUsers.remove(session.getId());
        

    }
    

}