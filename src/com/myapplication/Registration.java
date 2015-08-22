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
    	// Set response content type
        response.setContentType("text/html");
	      
        
        
        // Actual logic goes here.
		user = request.getParameter("user");			
    	pass = request.getParameter("pswrd");
        voucher = request.getParameter("voucher_");
        deviceId = request.getParameter("deviceId");
    	HttpSession session = request.getSession();


        try {
			//TODO: make registration without voucher
			
            // Set an attribute (name-value pair) if present in the request
            if (voucher != null) voucher = voucher.trim();

            if (voucher != null && !voucher.equals("") )
            		{
               // synchronized session object to prevent concurrent update
               synchronized(session) {
                  session.setAttribute("voucher", voucher);
                  
			if (SQLAccess.new_hash(pass, user) && SQLAccess.register_voucher(voucher) && SQLAccess.insert_voucher(voucher, user, pass) && SQLAccess.insert_device(deviceId, user)) {
				
				session.setAttribute("user", user);				
				session.setAttribute("device", deviceId);
				
				//setting session to expiry in 30 mins
				session.setMaxInactiveInterval(30*60);	
				
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
			try {
				SQLAccess.reset_voucher(voucher);
			} catch (Exception e1) {
			}
			session.invalidate();
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
