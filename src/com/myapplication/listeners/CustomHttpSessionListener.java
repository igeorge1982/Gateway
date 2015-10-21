package com.myapplication.listeners;

/**
 * @author George Gaspar
 * @email: igeorge1982@gmail.com 
 * 
 * @Year: 2015
 */

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.apache.log4j.Logger;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.SetMultimap;
import com.myapplication.SQLAccess;

@WebListener
public class CustomHttpSessionListener implements HttpSessionListener, Serializable, HttpSessionAttributeListener {

	private static final long serialVersionUID = -6951824749917799153L;
	
	private static Logger log = Logger.getLogger(Logger.class.getName());
	public static volatile HashMap<String, HttpSession> activeUsers;
	private static volatile SetMultimap<String, String> sessions;
	private static volatile TreeMap<String,String> attributes = new TreeMap<String, String>();
	private static volatile TreeMap<String,String> attributes_ = new TreeMap<String, String>();
	private static volatile String sessionId;
	private static volatile ListMultimap<String, String> mappings;
	private static volatile String id;
	private static volatile String name;
	private static volatile String value;
	private static volatile String D;
	private static volatile String D_;
	private static volatile String G;
	private static volatile String G_;
	private static volatile String useR;

	  private int sessionCount;

	  public CustomHttpSessionListener() {
	    this.sessionCount = 0;
		log.info("Class name: "+getClass().getName());

	  }


    public void init(ServletConfig config){
    	
    }

    /**
     * Returns a TreeMap with a name - value entry of a given attribute.
     * 
     * @param name
     * @param value
     * @return attributes
     */
    private TreeMap<String,String> SetMappings(String name, String value) {
    		    	
    		attributes.put(name, value);
   
    		Collection<String> fruits = attributes.values();    		
    		log.info("Values: " + fruits);
    		
        	return 	attributes;
    	}

    
    /**
     * Returns the value of the given attribute name.
     * 
     * @param name
     * @return G
     */
    private String GetMappings(String name) {
    	
    	G = attributes.get(name);
    	return 	G;
    }
    
    /**
     * Returns a TreeMap with a name - value entry of a given attribute.
     * 
     * @param name
     * @param value
     * @return attributes_
     */
    private TreeMap<String,String> SetMappings_(String name, String value) {
    	
    	attributes_.put(name, value);
		
    	Collection<String> fruits_ = attributes_.values();		
		log.info("Values: " + fruits_);

		return 	attributes_;
    }
    
    /**
     * Returns the value of the given attribute name.
     * 
     * @param name
     * @return G_
     */
    private String GetMappings_(String name) {
    	
    	G_ = attributes_.get(name);
    	return 	G_;
    }
    
	@SuppressWarnings("unchecked")
	@Override
	public void attributeAdded(HttpSessionBindingEvent se) {
		
	    HttpSession session = se.getSession();
	    ServletContext context = session.getServletContext();
        activeUsers = (HashMap<String, HttpSession>) context.getAttribute("activeUsers");
        sessions = (SetMultimap<String, String>) context.getAttribute("sessions");

	    id = session.getId();
	    name = se.getName();
	    value = (String)se.getValue();
	    
	    log.info("Name: " + attributes.keySet() + "Value: " + attributes.values());
	    
	    String source = se.getSource().getClass().getName();
	    String message = new StringBuffer("Attribute bound to session in ")
	        .append(source).append("\nThe attribute name: ").append(name)
	        .append("\n").append("The attribute value:").append(value)
	        .append("\n").append("The session ID: ").append(id).toString();
	    log.info(message);		
	    
	 SetMappings(name, value);
	    
   	 D = GetMappings("deviceId");
     useR = GetMappings("user");
    
      if (sessions.containsKey(D)) {
    
    	  mappings = (ListMultimap<String, String>) sessions;
      	  sessionId = mappings.get(D).get(1);
      	  log.info("sessionId to remove: " + sessionId);

      	activeUsers.remove(sessionId);
    	  
     Set<String> sessionData = sessions.get(D);
	    
     		sessionData.clear();
     		sessionData.add(useR);
     		sessionData.add(id);
      
          } else {
        	  
        	  sessions.put(D, useR);
        	  sessions.put(D, id);

          }
      context.setAttribute("activeUsers", activeUsers);
      context.setAttribute("sessions", sessions);
      
     log.info("SessionUsers: " + sessions.entries());
     log.info("Active UserSessions: " + activeUsers.keySet().toString());
     
	}


	@Override
	public void attributeRemoved(HttpSessionBindingEvent se) {
	   
		HttpSession session = se.getSession();

	    id = session.getId();
	    name = se.getName();
	    if (name == null)
	      name = "Unknown";
	    value = (String) se.getValue();
	    	    
	    String source = se.getSource().getClass().getName();
	    String message = new StringBuffer("Attribute unbound from session in ")
	        .append(source).append("\nThe attribute name: ").append(name)
	        .append("\n").append("The attribute value: ").append(value)
	        .append("\n").append("The session ID: ").append(id).toString();
	    log.info(message);	
	    
	    SetMappings_(name, value);
        String D_ = GetMappings_("deviceId");

        sessions.removeAll(D_);
        log.info("SessionUsers: " + sessions.entries());


	}


	@Override
	public void attributeReplaced(HttpSessionBindingEvent arg0) {
		
	}
    
    /**
     * Adds sessions to the context scoped HashMap when they begin.
     */
    @SuppressWarnings("unchecked")
	public void sessionCreated(HttpSessionEvent event){
    	
    	HttpSession session = event.getSession();

        ServletContext context = session.getServletContext();
        log.info("Context attributes: " + context.getAttributeNames().nextElement());
        activeUsers = (HashMap<String, HttpSession>) context.getAttribute("activeUsers");
        sessions = (SetMultimap<String, String>) context.getAttribute("sessions");
               	

	    	 D = GetMappings("deviceId");
	       //  String useR = GetMappings("user");
	         
	         if (!sessions.containsKey(D)) {
	         	
	         	activeUsers.put(session.getId(), session);

	         } 
	         
	         else {

	        	mappings = (ListMultimap<String, String>) sessions;
	        	sessionId = mappings.get(D).get(1);
	        	log.info("sessionId to remove: " + sessionId);

	        	activeUsers.remove(sessionId);
	         	activeUsers.put(session.getId(), session);
	         	
	         } 	         
	      
        context.setAttribute("activeUsers", activeUsers);
        context.setAttribute("sessions", sessions);
      
        log.info("Active UserSessions: " + activeUsers.keySet().toString());
    	
    }

    /**
     * Removes sessions from the context scoped HashMap when they expire
     * or are invalidated.
     */
    @SuppressWarnings("unchecked")
	public void sessionDestroyed(HttpSessionEvent event){
        
    	HttpSession session = event.getSession();
        ServletContext context = session.getServletContext();
        activeUsers = (HashMap<String, HttpSession>)context.getAttribute("activeUsers");
        sessions = (SetMultimap<String, String>)context.getAttribute("sessions");
        
        D_ = GetMappings_("deviceId");
        id = session.getId();
        
        try {
			
        	SQLAccess.logout(id, context);
	        log.info("SessionID destroyed: " + id.toString());          
			
        	} catch (Exception e) {
        		
        		log.info(e.getMessage());
		}


            activeUsers.remove(session.getId());
            sessions.removeAll(D_);
            log.info("SessionUsers: " + sessions.entries());
          
        
          String message = new StringBuffer("Session destroyed"
              + "\nValue of destroyed session ID is").append("" + id).append(
              "\n").append("There are now ").append("" + sessionCount)
              .append(" live sessions in the application.").toString();
          log.info(message);
          
        
    }




}