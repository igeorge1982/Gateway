package com.myapplication.listeners;

/**
 * @author George Gaspar
 * @email: igeorge1982@gmail.com 
 * 
 * @Year: 2015
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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


@SuppressWarnings("unused")
public class CustomHttpSessionListener implements HttpSessionListener, Serializable{

	private static final long serialVersionUID = -6951824749917799153L;
	
	private static Logger log = Logger.getLogger(Logger.class.getName());
	public static volatile HashMap<String, HttpSession> activeUsers = null;
	public static volatile HashMap<String, String> sessionUsers = null;
	private static volatile Multimap<String, String> sessions = null;
	private static volatile String sessionId;

	  private int sessionCount;

	  public CustomHttpSessionListener() {
	    this.sessionCount = 0;
	  }


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
        sessions = (Multimap<String, String>) context.getAttribute("sessions");
        
    	//activeUsers.put(session.getId(), session);

        //sessionUsers = (HashMap<String, String>) context.getAttribute("sessionUsers");
       
                
        if (sessions.containsEntry((String) session.getAttribute("deviceId"), (String) session.getAttribute("user"))) {
        	
        	sessionId = sessions.get((String) session.getAttribute("deviceId")).iterator().next();        	
        	sessions.remove((String) session.getAttribute("deviceId"), sessionId);
        	activeUsers.remove(sessionId);
        	activeUsers.put(session.getId(), session);
            sessions.put((String) session.getAttribute("deviceId"), session.getId());

        	//includesSet = Collections.synchronizedSet(activeUsers.keySet());
        	//includesSet.remove(sessionId);
        	//.remove(sessionId);
        	//activeUsers.put(session.getId(), session);
        
        } else {
        
        	//sessionUsers.put((String) session.getAttribute("user"), session.getId());
        	sessions.put((String) session.getAttribute("deviceId"), (String) session.getAttribute("user"));
            sessions.put((String) session.getAttribute("deviceId"), session.getId());            
        	activeUsers.put(session.getId(), session);
        }
        
        synchronized (this) {
            sessionCount++;
          }
          
          String id = session.getId();
          Date now = new Date();
          String message = new StringBuffer("New Session created on ").append(
              now.toString()).append("\nID: ").append(id).append("\n")
              .append("There are now ").append("" + sessionCount).append(
                  " live sessions in the application.").toString();

          log.info(message);
        
    	
        log.info("New SessionID: " + session.getId().toString());
        context.setAttribute("activeUsers", activeUsers);
        context.setAttribute("sessions", sessions);

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
        sessions = (Multimap<String, String>)context.getAttribute("sessions");
        
        String id = session.getId();
        
        try {
			
        	SQLAccess.logout(id, context);
	        log.info("SessionID destroyed: " + id.toString());          
			
        	} catch (Exception e) {
        		
        		e.printStackTrace();
		}
        activeUsers.remove(session.getId());
        
        synchronized (this) {
            --sessionCount;
          }
        
          String message = new StringBuffer("Session destroyed"
              + "\nValue of destroyed session ID is").append("" + id).append(
              "\n").append("There are now ").append("" + sessionCount)
              .append(" live sessions in the application.").toString();
          log.info(message);
          
        sessions.removeAll((String) session.getAttribute("deviceId"));
        
    }
    

}