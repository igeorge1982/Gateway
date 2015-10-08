package com.myapplication.listeners;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.apache.log4j.Logger;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
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
	private static volatile Multimap<String, String> sessions = null;
	private static volatile String sessionId;


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
        sessions = ArrayListMultimap.create();
        
    	//activeUsers.put(session.getId(), session);

        //sessionUsers = (HashMap<String, String>) context.getAttribute("sessionUsers");
       
                
        if (sessions.containsEntry((String) session.getAttribute("deviceId"), (String) session.getAttribute("user"))) {
        	
        	sessionId = sessions.get((String) session.getAttribute("deviceId")).iterator().next();        	
        	sessions.remove((String) session.getAttribute("deviceId"), sessionId);
        	activeUsers.remove(sessionId);
        	activeUsers.put(session.getId(), session);
            sessions.put((String) session.getAttribute("deviceId"), session.getId());
            log.info("New Session: " + sessions.keySet().toString());

        	//includesSet = Collections.synchronizedSet(activeUsers.keySet());
        	//includesSet.remove(sessionId);
        	//.remove(sessionId);
        	//activeUsers.put(session.getId(), session);
        
        } else {
        
        	//sessionUsers.put((String) session.getAttribute("user"), session.getId());
        	sessions.put((String) session.getAttribute("deviceId"), (String) session.getAttribute("user"));
            sessions.put((String) session.getAttribute("deviceId"), session.getId());
            log.info("New Session: " + sessions.keySet().toString());
            
        	activeUsers.put(session.getId(), session);
        }
        
    	
        log.info("New SessionID: " + session.getId().toString());
        context.setAttribute("activeUsers", activeUsers);
        //context.setAttribute("sessionUsers", sessionUsers);

        log.info("Active UserSessions: " + activeUsers.keySet().toString());
       // log.info("sessionUsers: " + sessionUsers.values().toString());

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
       // sessionUsers = (HashMap<String, String>)context.getAttribute("sessionUsers");
        
        try {
			
        	SQLAccess.logout(session.getId(), context);
	        log.info("SessionID destroyed: " + session.getId().toString());          
			
        	} catch (Exception e) {
        		
        		e.printStackTrace();
		}
        activeUsers.remove(session.getId());
       // sessionUsers.remove((String) session.getAttribute("user"));
        
    }
    

}