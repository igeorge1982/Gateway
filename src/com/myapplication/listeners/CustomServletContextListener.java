package com.myapplication.listeners;

import java.sql.SQLException;
import java.util.HashMap;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.Logger;

import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;
import com.myapplication.DBConnectionManager;

@WebListener
public class CustomServletContextListener implements ServletContextListener{
	
	private static Logger log = Logger.getLogger(Logger.class.getName());
	private volatile static HashMap<String, Object> activeUsers;
	private static volatile Multimap<String, String> sessions;
	private static volatile HashMap<String,String> attributes;


   public void contextInitialized(ServletContextEvent event){
    
	ServletContext context = event.getServletContext();
       
   	String url = context.getInitParameter("DBURL");
   	String u = context.getInitParameter("DBUSER");
   	String p = context.getInitParameter("DBPWD");
   	
   	//create database connection from init parameters and set it to context
   	DBConnectionManager dbManager = new DBConnectionManager(url, u, p);
   	context.setAttribute("DBManager", dbManager);
   	log.info("Database connection initialized for Application.");

       //
       // instanciate a map to store references to all the active
       // sessions and bind it to context scope.
       //
       activeUsers = new HashMap<String, Object>();
       context.setAttribute("activeUsers", activeUsers);
       
       attributes = new HashMap<String, String>();
       context.setAttribute("attributes", attributes); 
       
       sessions = TreeMultimap.create();
       context.setAttribute("sessions", sessions);
   }

   /**
    * Needed for the ServletContextListener interface.
    */
   public void contextDestroyed(ServletContextEvent event){
       
	   // To overcome the problem with losing the session references
       // during server restarts, put code here to serialize the
       // activeUsers HashMap.  Then put code in the contextInitialized
       // method that reads and reloads it if it exists...

	ServletContext context = event.getServletContext();
   	
	DBConnectionManager dbManager = (DBConnectionManager) context.getAttribute("DBManager");
   	try {
   		dbManager.closeConnection();  			
   			} catch (SQLException e) {
   				e.printStackTrace();
	}
   	log.info("Database connection closed for Application.");
   
   }
}


