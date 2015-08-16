package com.myapplication;

//Import required java libraries
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.*;
import javax.servlet.http.*;

import com.myapplication.SQLAccess;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

//Extend HttpServlet class
public class Loggingout extends HttpServlet {
  /**
  *
  */
  private static final long serialVersionUID = 1L;
  @SuppressWarnings("unused")
	private String message;

	public final static String dbDriverClass = "com.mysql.jdbc.Driver";
	public final static String dbUrl = "jdbc:mysql://localhost:3306";
	public final static String dbUserName = "sqluser";
	public final static String dbPassWord = "sqluserpw";
	public static SQLAccess dao = new SQLAccess(dbDriverClass, dbUrl, dbUserName, dbPassWord);
	public volatile static String pass;
	public volatile static String user;
	public volatile static String hash1;
	public volatile static String deviceId;
	
  @BeforeClass
  public void setUp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

  }
  
  @AfterClass
  public static void close(){
  	
  }

  public void init() throws ServletException
  {
      // Do required initialization
      message = "Hello World";
  }
  
  public synchronized void processRequest (HttpServletRequest request, HttpServletResponse response)
  	    throws Exception {
		    	
  }
  
  public synchronized void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
  		/*
	  // Set response content type
      response.setContentType("text/html");

     HttpSession session = request.getSession();
     
	//set HTTP headers
 	response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
 	response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
 	response.setDateHeader("Expires", System.currentTimeMillis());	 		

     if(session!=null) {

    	 session.removeAttribute("user");

    	 response.sendRedirect("https://localhost/javaScript/mainpage.html"); 
     }
     
     session.invalidate();
  		 */
  }
  
  public synchronized void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
		
	  	//set HTTP headers
	 	//response.setHeader("Cache-Control", "no-cache, no-store, private, must-revalidate"); // HTTP 1.1.
	 	//response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
	 	//response.setDateHeader("Expires", System.currentTimeMillis());	 	
	     
	 	HttpSession session = request.getSession();

	     session.removeAttribute("user");
	      	
	     if(session.getAttribute("user") == null){
		     session.invalidate();
		     session.setMaxInactiveInterval(1);
	    	 response.setHeader("Referer", "https://localhost/javaScript/index.html");
	    	 response.sendRedirect("https://localhost/javaScript/index.html");
	     }

  }
  
  public void destroy()
  {
      // do nothing.
  }
}