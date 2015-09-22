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

import com.myapplication.SQLAccess;

import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

//Extend HttpServlet class
public class Registration extends HttpServlet {
	
	private static Logger log = Logger.getLogger(Logger.class.getName());

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
	private volatile static HttpSession session;
	
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
    	session = request.getSession();


        try {
			//TODO: make registration without voucher
			
            // Set an attribute (name-value pair) if present in the request
            if (voucher != null) voucher = voucher.trim();

            if (voucher != null && !voucher.equals("") ) {
            	
               // synchronized session object to prevent concurrent update
               synchronized(session) {
                  session.setAttribute("voucher", voucher);
                
			if (SQLAccess.register_voucher(voucher)) {
                  
              if (SQLAccess.new_hash(pass, user) && SQLAccess.insert_voucher(voucher, user, pass) && SQLAccess.insert_device(deviceId, user)) {
				
				session.setAttribute("user", user);				
				session.setAttribute("device", deviceId);
				
				//setting session to expiry in 30 mins
				session.setMaxInactiveInterval(30*60);	
				
				ServletContext otherContext = getServletContext().getContext("/example");
				String encodedURL = response.encodeRedirectURL(otherContext.getContextPath() + "/index.jsp");
				response.sendRedirect(encodedURL);
					
				  }	
	           } else {

	        	   response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, "Line 108");
	           }
			}
		}
		
		} catch (Exception e) {
			
			try {
				
				SQLAccess.reset_voucher(voucher);
				
			} catch (Exception e1) {
			
				log.info("Voucher reset FAILED!");
				
				}
			
			if (session != null) {								
				session.invalidate();
			}
			
			response.setHeader("Referer", request.getContextPath() + "/login/register");
			ServletContext otherContext = getServletContext().getContext("/login");
			String encodedURL = response.encodeRedirectURL(otherContext.getContextPath() + "/login/logout");
			response.sendRedirect(encodedURL);
		
		} 
    }
    
    public synchronized void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	
    	processRequest(request, response);

    	// Set response content type
        response.setContentType("text/html");
	      
        try {
    		voucher = request.getParameter("voucher");
        	pass = request.getParameter("pswrd");
            voucher = request.getParameter("voucher_");
            deviceId = request.getParameter("deviceId");
			
			if (voucher.trim().isEmpty() || user.trim().isEmpty() || pass.trim().isEmpty() || deviceId.trim().isEmpty()) {
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
