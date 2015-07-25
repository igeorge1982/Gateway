package com.myapplication;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import com.myapplication.SQLAccess;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

//Extend HttpServlet class
public class Registration extends HttpServlet {
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
		user = request.getParameter("user");			
    	pass = request.getParameter("pswrd");

        try {
			
        	HttpSession session = request.getSession();
			
			// Set an attribute (name-value pair) if present in the request
	        voucher = request.getParameter("voucher");
	        
	        if (voucher != null) voucher = voucher.trim();
	        		        
	        if (voucher != null && !voucher.equals("") && voucher != null && !voucher.equals("") ) {
	           
	        	// synchronized session object to prevent concurrent update
	           synchronized(session) {
	        	   
	              session.setAttribute("voucher", voucher);
	           }
	        }
        	
			if (SQLAccess.new_hash(pass, user)) {
				
				session.setAttribute("user", user);
				
				
				
				//setting session to expiry in 30 mins
				session.setMaxInactiveInterval(30*60);
				Cookie userName = new Cookie("user", user);
				response.addCookie(userName);
				String encodedURL = response.encodeRedirectURL("https://localhost/login/admin");
				response.sendRedirect(encodedURL);
				
			}
		
		} catch (Exception e) {
			
			HttpSession session = request.getSession();
			session.invalidate();
    		response.sendRedirect("https://localhost/javaScript/mainpage.html");

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
