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
import com.myapplication.UrlManager;

@WebListener
public class CustomServletContextListener implements ServletContextListener{
	
	private static Logger log = Logger.getLogger(Logger.class.getName());
	private volatile static HashMap<String, Object> activeUsers;
	private static volatile Multimap<String, String> sessions;
	private static volatile HashMap<String,String> attributes;
	private static volatile HashMap<String,String> urls;


   public void contextInitialized(ServletContextEvent event){
    
	ServletContext context = event.getServletContext();
       
   	final String url = context.getInitParameter("DBURL");
   	final String u = context.getInitParameter("DBUSER");
   	final String p = context.getInitParameter("DBPWD");
   	
	final String voucherRedirect = context.getInitParameter("voucherRedirect");
	final String voucherElseRedirect = context.getInitParameter("voucherElseRedirect");
	final String homePage = context.getInitParameter("homePage");
	final String homePageIndex = context.getInitParameter("homePageIndex");
	final String loginContext = context.getInitParameter("loginContext");
	final String loginToRegister = context.getInitParameter("loginToRegister");
	final String loginToLogout = context.getInitParameter("loginToLogout");
	final String webApiContext = context.getInitParameter("webApiContext");
	final String webApiContextUrl = context.getInitParameter("webApiContextUrl");
	
	// loading the context InitParameters once, then putting them into HashMap<String, String> that is set to context then, 
	// using an UrlManager class
	// that initialize all the urls and has getter / setter methods.
	// Urls are loadable from context scope or at class level with "getServletContext().getInitParameter("voucherRedirect");".
	// The purpose of the context scope is to make editable the urls during runtime, through API.
	// TODO: set the edited urls as new InitParameter value
	UrlManager urlManager = new UrlManager(voucherRedirect, voucherElseRedirect, homePage, homePageIndex, loginContext, loginToRegister, loginToLogout, webApiContextUrl, webApiContext);
   	
    urls = new HashMap<String, String>();

   	String voucherRedirect_ = urlManager.getVoucherRedirect();
   	String voucherElseRedirect_ = urlManager.getVoucherElseRedirect();
	String homePage_ = urlManager.getHomePage();
	String homePageIndex_ = urlManager.getHomePageIndex();
	String loginContext_ = urlManager.getLoginContext();
	String loginToRegister_ = urlManager.getLoginToRegister();
	String loginToLogout_ = urlManager.getLoginToLogout();
	String webApiContext_ = urlManager.getWebApiContext();
	String webApiContextUrl_ = urlManager.getWebApiContextUrl();
	
   	urls.put(voucherRedirect, voucherRedirect_);
   	urls.put(voucherElseRedirect, voucherElseRedirect_);
   	urls.put(homePage, homePage_);
   	urls.put(homePageIndex, homePageIndex_);
   	urls.put(loginContext, loginContext_);
   	urls.put(loginToRegister, loginToRegister_);
   	urls.put(loginToLogout, loginToLogout_);
   	urls.put(webApiContext, webApiContext_);
   	urls.put(webApiContextUrl, webApiContextUrl_);

   	context.setAttribute("UrlManager", urls);

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


