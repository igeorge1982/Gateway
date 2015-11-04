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
	private volatile static String pass;
	private volatile static String user;
	private volatile static String voucher;
	private volatile static String deviceId;
	private volatile static HttpSession session;
	private volatile static long SessionCreated;
	private volatile static String sessionID;
	
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
		ServletContext context = request.getServletContext();


        try {
			//TODO: make registration without voucher
			
            // Set an attribute (name-value pair) if present in the request
            if (voucher != null) voucher = voucher.trim();

            if (voucher != null && !voucher.equals("") ) {
            	
            	session = request.getSession(true);

               // synchronized session object to prevent concurrent update
               synchronized(session) {
                  
            	   session.setAttribute("voucher", voucher);
                
			if (SQLAccess.register_voucher(voucher, context)) {
                  
              if (SQLAccess.new_hash(pass, user, context) && SQLAccess.insert_voucher(voucher, user, pass, context) && SQLAccess.insert_device(deviceId, user, context)) {
				
				session.setAttribute("user", user);				
				session.setAttribute("deviceId", deviceId);
				
				//setting session to expiry in 30 mins
				session.setMaxInactiveInterval(30*60);
				SessionCreated = session.getCreationTime();
				sessionID = session.getId();
				
				try {
					SQLAccess.insert_sessionCreated(deviceId, SessionCreated, sessionID, context);
				} catch (Exception e) {	
					throw new ServletException();
				}
		        String homePage = getServletContext().getInitParameter("homePage");
		        String homePageIndex = getServletContext().getInitParameter("homePageIndex");
		        
				ServletContext otherContext = getServletContext().getContext(homePage);
				String encodedURL = response.encodeRedirectURL(otherContext.getContextPath() + homePageIndex);
				response.sendRedirect(encodedURL);
					
				  }	
	           } else {

	        	   response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, "Line 115");
	           }
			}
		}
		
		} catch (Exception e) {
			
			try {
				
				SQLAccess.reset_voucher(voucher, context);
				
			} catch (Exception e1) {
			
				log.info("Voucher reset FAILED!");
				
				}
			
			if (session != null) {								
				session.invalidate();
			}
	        String loginContext = getServletContext().getInitParameter("loginContext");
	        String loginToRegister = getServletContext().getInitParameter("loginToRegister");
	        String loginToLogout = getServletContext().getInitParameter("loginToLogout");
			
			response.setHeader("Referer", request.getContextPath() + loginToRegister);
			ServletContext otherContext = getServletContext().getContext(loginContext);
			String encodedURL = response.encodeRedirectURL(otherContext.getContextPath() + loginToLogout);
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
	    		response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "Line 162");
			}
					
		} catch (Exception e) {			
    		response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "Line 166");

		}
        
    }
    
    public void destroy()
    {
        // do nothing.
    }
}
