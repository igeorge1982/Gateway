package com.myapplication.listeners;

import java.io.Serializable;
import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.apache.log4j.Logger;

import com.myapplication.SQLAccess;

/**
 * Listens for session events and adds or removes references to 
 * to the context scoped HashMap accordingly.
 * @author Ben Souther; ben@souther.us
 * @since Sun May  8 18:57:10 EDT 2005
 */


public class CustomHttpSessionListener implements HttpSessionListener, Serializable{

	private static final long serialVersionUID = -6951824749917799153L;
	
	private static Logger log = Logger.getLogger(Logger.class.getName());
	public static volatile HashMap<String, HttpSession> activeUsers = null;

    public void init(ServletConfig config){
    	
    }

    /**
     * Adds sessions to the context scoped HashMap when they begin.
     */
    @SuppressWarnings("unchecked")
	public void sessionCreated(HttpSessionEvent event){
        
    	HttpSession    session = event.getSession();
        ServletContext context = session.getServletContext();
        activeUsers = (HashMap<String, HttpSession>) context.getAttribute("activeUsers");
        activeUsers.put(session.getId(), session);
        
        // TODO: sql can run here to insert user sessions into the dB
        log.info("New SessionID: " + session.getId().toString());
        context.setAttribute("activeUsers", activeUsers);
        log.info("Actice UserSessions: " + activeUsers.keySet().toString());

    }

    /**
     * Removes sessions from the context scoped HashMap when they expire
     * or are invalidated.
     */
    @SuppressWarnings("unchecked")
	public void sessionDestroyed(HttpSessionEvent event){
        
    	HttpSession    session = event.getSession();
        ServletContext context = session.getServletContext();
        activeUsers = (HashMap<String, HttpSession>)context.getAttribute("activeUsers");
        
        try {
			
        	SQLAccess.logout(session.getId());
	        log.info("SessionID destroyed: " + session.getId().toString());          
			
        	} catch (Exception e) {
        		
        		e.printStackTrace();
		}
        activeUsers.remove(session.getId());
        
    }
    

}