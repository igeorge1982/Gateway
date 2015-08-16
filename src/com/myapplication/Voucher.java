package com.myapplication;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import com.myapplication.SQLAccess;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

//Extend HttpServlet class
public class Voucher extends HttpServlet {
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
	public volatile static String voucher;
	
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
    	// Set response content type
        response.setContentType("text/html");
	      
        // Actual logic goes here.
		voucher = request.getParameter("voucher");			

        try {
			if (SQLAccess.voucher(voucher)) {
								
				//set HTTP headers
	        	//response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
	        	//response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
	    	 	//response.setDateHeader("Expires", System.currentTimeMillis();	 	
	        	
				String encodedURL = response.encodeRedirectURL("https://localhost/javaScript/register.html?voucher_="+voucher);
				response.sendRedirect(encodedURL);
				
			}
			else {
				response.sendRedirect("https://localhost/javaScript/voucher.html");
				
			}
		} catch (Exception e) {
			
    		response.sendRedirect("https://localhost/javaScript/voucher.html");

		}

        
    }
    
    public synchronized void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        
    }
    
    public void destroy()
    {
        // do nothing.
    }
}
