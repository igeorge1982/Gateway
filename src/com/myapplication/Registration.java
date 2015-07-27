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
        voucher = request.getParameter("voucher_");

        try {
			//TODO: this stage must be available if some token pair is matching the one generated in voucher servlet, else redirect 
        	//or see below
        	HttpSession session = request.getSession();
			
            // Set an attribute (name-value pair) if present in the request
            if (voucher != null) voucher = voucher.trim();

            if (voucher != null && !voucher.equals("") )
            		{
               // synchronized session object to prevent concurrent update
               synchronized(session) {
                  session.setAttribute("voucher", voucher);
                  
        	//TODO: voucher could be tested here if it's still valid to avoid forgery - the same way when checking the voucher if it's free
            //TODO: insert (with an update) the voucher code into vouchers table next to the triggered uuid 
			if (SQLAccess.new_hash(pass, user) && SQLAccess.register_voucher(voucher)) {
				
				session.setAttribute("user", user);				
				//setting session to expiry in 30 mins
				session.setMaxInactiveInterval(30*60);
				Cookie userName = new Cookie("user", user);
				response.addCookie(userName);
				
				String encodedURL = response.encodeRedirectURL("https://localhost/login/admin");
				response.sendRedirect(encodedURL);
					
				  }	
	           }
			}
			
			else {
				SQLAccess.reset_voucher(voucher);
				session.invalidate();
				//TODO: send some error toast
				//TODO: clear url parameter
	    		response.sendRedirect("https://localhost/javaScript/voucher.html");
			}
		
		} catch (Exception e) {
			
			//TODO: send some error toast
			//TODO: clear url parameter
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
