package com.myapplication.listeners;

import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class CustomServletContextListener implements ServletContextListener{
	
	private volatile static HashMap<String, Object> activeUsers;

   public void contextInitialized(ServletContextEvent event){
       ServletContext context = event.getServletContext();

       //
       // instanciate a map to store references to all the active
       // sessions and bind it to context scope.
       //
       activeUsers = new HashMap<String, Object>();
       context.setAttribute("activeUsers", activeUsers);
   }

   /**
    * Needed for the ServletContextListener interface.
    */
   public void contextDestroyed(ServletContextEvent event){
       // To overcome the problem with losing the session references
       // during server restarts, put code here to serialize the
       // activeUsers HashMap.  Then put code in the contextInitialized
       // method that reads and reloads it if it exists...
   }
}