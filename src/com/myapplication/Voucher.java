package com.myapplication;

/**
 * @author George Gaspar
 * @email: igeorge1982@gmail.com 
 * 
 * @Year: 2015
 */

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.myapplication.SQLAccess;


//Extend HttpServlet class
public class Voucher extends HttpServlet {
    /**
    *
    */
    private static final long serialVersionUID = 1L;

	public final static String dbDriverClass = "com.mysql.jdbc.Driver";
	public final static String dbUrl = "jdbc:mysql://localhost:3306";
	public final static String dbUserName = "sqluser";
	public final static String dbPassWord = "sqluserpw";
	
	public static SQLAccess dao = new SQLAccess(dbDriverClass, dbUrl, dbUserName, dbPassWord);
	private volatile static String voucher;
	
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
    	    throws ServletException, IOException {
		    	
        String userName = "admin";
        String password = "Tapsihapsi666";
    	request.login(userName, password); 
    	
    }
    
    public synchronized void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	processRequest(request, response);

    	//TODO: do something with left voucher registrations
    	// Set response content type
        response.setContentType("text/html");			

        try {       		      
            // Actual logic goes here.
    		voucher = request.getParameter("voucher");
    		
			if (voucher != null && SQLAccess.voucher(voucher)) { 	
	        	
				String encodedURL = response.encodeRedirectURL("https://localhost/javaScript/register.html?voucher_="+voucher);
				response.sendRedirect(encodedURL);
				
			}
			else {
				response.sendRedirect("https://localhost/javaScript/voucher.html");
				
			}
		} catch (Exception e) {
        	response.sendError(HttpServletResponse.SC_BAD_GATEWAY);
		
		} 
        
    }
    
    public synchronized void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        
    	processRequest(request, response);

    	// Set response content type
        response.setContentType("text/html");
	      
        try {
    		voucher = request.getParameter("voucher");
			
			if (voucher.trim().isEmpty()) {
	    		response.sendError(HttpServletResponse.SC_BAD_GATEWAY);
			}
					
		} catch (Exception e) {
			
    		response.sendError(HttpServletResponse.SC_BAD_GATEWAY);

		}  	
    }
    
    public void destroy()
    {
        // do nothing.
    }
}
