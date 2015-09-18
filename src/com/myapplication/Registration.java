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

	public final static String dbDriverClass = "com.mysql.jdbc.Driver";
	public final static String dbUrl = "jdbc:mysql://localhost:3306";
	public final static String dbUserName = "sqluser";
	public final static String dbPassWord = "sqluserpw";
	public static SQLAccess dao = new SQLAccess(dbDriverClass, dbUrl, dbUserName, dbPassWord);
	
	private volatile static String pass;
	private volatile static String user;
	private volatile static String voucher;
	private volatile static String deviceId;
	
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
				
				ServletContext otherContext = getServletContext().getContext("/login");
				String encodedURL = response.encodeRedirectURL(otherContext.getContextPath() + "/admin");
				response.sendRedirect(encodedURL);
					
				  }	
	           }
			}
			
			else {
				SQLAccess.reset_voucher(voucher);
				if (session != null) {								
					session.invalidate();
				}
				//TODO: clear url parameter
				ServletContext otherContext = getServletContext().getContext("/login");
				String encodedURL = response.encodeRedirectURL(otherContext.getContextPath() + "/logout");
	    		response.sendRedirect(encodedURL);
			}
		
		} catch (Exception e) {
			
			//TODO: clear url parameter
			try {
				SQLAccess.reset_voucher(voucher);
			} catch (Exception e1) {
				}
			
			if (session != null) {								
				session.invalidate();
			}
			
			ServletContext otherContext = getServletContext().getContext("/login");
			String encodedURL = response.encodeRedirectURL(otherContext.getContextPath() + "/logout");
			response.sendRedirect(encodedURL);
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
