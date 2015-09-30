package com.myapplication.listeners;

import java.sql.SQLException;
import java.util.HashMap;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.log4j.Logger;
import com.myapplication.DBConnectionManager;

public class CustomServletContextListener implements ServletContextListener{
	
	private static Logger log = Logger.getLogger(Logger.class.getName());
	private volatile static HashMap<String, Object> activeUsers;
	private volatile static HashMap<String, String> sessionUsers;

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
       
       sessionUsers = new HashMap<String, String>();
       context.setAttribute("sessionUsers", sessionUsers);
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

