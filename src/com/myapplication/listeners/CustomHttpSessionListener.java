package com.myapplication.listeners;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

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
	public static volatile HashMap<String, String> sessionUsers = null;
	private static volatile String sessionId;
	private volatile Set<String> includesSet = null; 


    public void init(ServletConfig config){
    	
    }

    /**
     * Adds sessions to the context scoped HashMap when they begin.
     */
    @SuppressWarnings("unchecked")
	public void sessionCreated(HttpSessionEvent event){
        
    	HttpSession session = event.getSession();
        ServletContext context = session.getServletContext();
        activeUsers = (HashMap<String, HttpSession>) context.getAttribute("activeUsers");
        sessionUsers = (HashMap<String, String>) context.getAttribute("sessionUsers");
        
        if (sessionUsers.containsKey((String) session.getAttribute("user"))) {
        	
        	sessionId = sessionUsers.get((String) session.getAttribute("user"));        	
        	sessionUsers.replace((String) session.getAttribute("user"), sessionId, session.getId());
        	
        	includesSet = Collections.synchronizedSet(activeUsers.keySet());
        	includesSet.remove(sessionId);
        	//.remove(sessionId);
        	activeUsers.put(session.getId(), session);
        
        } else {
        
        	sessionUsers.put((String) session.getAttribute("user"), session.getId());
        	activeUsers.put(session.getId(), session);
        }
                
        log.info("New SessionID: " + session.getId().toString());
        context.setAttribute("activeUsers", activeUsers);
        context.setAttribute("sessionUsers", sessionUsers);

        log.info("Active UserSessions: " + activeUsers.keySet().toString());
        log.info("sessionUsers: " + sessionUsers.values().toString());

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
        sessionUsers = (HashMap<String, String>)context.getAttribute("sessionUsers");
        
        try {
			
        	SQLAccess.logout(session.getId(), context);
	        log.info("SessionID destroyed: " + session.getId().toString());          
			
        	} catch (Exception e) {
        		
        		e.printStackTrace();
		}
        activeUsers.remove(session.getId());
        sessionUsers.remove((String) session.getAttribute("user"));
        
    }
    

}