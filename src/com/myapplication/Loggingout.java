package com.myapplication;

//Import required java libraries
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import com.myapplication.SQLAccess;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;


//Extend HttpServlet class
public class Loggingout extends HttpServlet {

  private static final long serialVersionUID = 1L;

	public final static String dbDriverClass = "com.mysql.jdbc.Driver";
	public final static String dbUrl = "jdbc:mysql://localhost:3306";
	public final static String dbUserName = "sqluser";
	public final static String dbPassWord = "sqluserpw";
	public static SQLAccess dao = new SQLAccess(dbDriverClass, dbUrl, dbUserName, dbPassWord);
	public volatile static String user;
	public volatile static HttpSession session;
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
	  }
	  
	  public synchronized void processRequest (HttpServletRequest request, HttpServletResponse response)
	  	    throws Exception {
			    	
	  }
	  
	  public synchronized void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	  {
	
	  }
	  
	  public synchronized void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	  {	 	
		     
		 	session = request.getSession(false);
		      	
		     if(session != null){
		  		 
		    	 session.removeAttribute("user");
			     session.invalidate();
		    	 
			     response.setHeader("Referer", "https://localhost/javaScript/index.html");
		    	 response.sendRedirect("https://localhost/javaScript/index.html");
		     }
		     
		     else {
		    	 response.setHeader("Referer", "https://localhost/javaScript/index.html");
		    	 response.sendRedirect("https://localhost/javaScript/index.html");
		     }
	
	  }
	  
	  public void destroy()
	  {
	      // do nothing.
	  }
}