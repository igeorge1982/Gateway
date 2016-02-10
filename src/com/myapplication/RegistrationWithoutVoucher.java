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
import com.myapplication.utils.hmac512;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

//Extend HttpServlet class
public class RegistrationWithoutVoucher extends HttpServlet {
	
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
	

	private volatile static String email;
	private volatile static String ios;
	private volatile static String WebView;
	private volatile static String M;
	private volatile static boolean devices;
	private volatile static String token2;
	private volatile static String hmac;
	private volatile static String hmacHash;
	private volatile static String time;
	
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
		response.setContentType("application/json"); 
		response.setCharacterEncoding("utf-8"); 	             
        
        // Actual logic goes here.
		user = request.getParameter("user").trim();			
    	pass = request.getParameter("pswrd").trim();
    	email = request.getParameter("email").trim();
        deviceId = request.getParameter("deviceId").trim();
        
    	hmac = request.getHeader("X-HMAC-HASH");
    	
    	//TODO: add time constraint
    	//TODO: add content length validation
    	time = request.getHeader("X-MICRO-TIME");
		ios = request.getParameter("ios");
		WebView = request.getHeader("User-Agent");
		M = request.getHeader("M");
		
		ServletContext context = request.getServletContext();

        // Check core request parameters first

        if (user.equals("") && user.length() > 0) {
        	
        	hmacHash = hmac512.getRegWithoutVoucherHmac512(user, email, pass, deviceId, time);
    		
    		log.info("HandShake was given: "+hmac+" & "+hmacHash);
        	
        	session = request.getSession(true);

           // synchronized session object to prevent concurrent update
           synchronized(session) {
              
        // Try - catch is necessary anyways, and will catch user names that have become identical in the meantime
        try {
			
			if (hmac.equals(hmacHash)) {
                  
              if (SQLAccess.new_hash(pass, user, email, context) && SQLAccess.insert_device(deviceId, user, context)) {
				
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
		        
				ServletContext otherContext = getServletContext().getContext(homePage);
				
				// native mobile
				if (ios != null) {
					
					response.setContentType("application/json"); 
					response.setCharacterEncoding("utf-8"); 
					response.setStatus(200);
	
					PrintWriter out = response.getWriter(); 
					
					//create Json Object 
					JSONObject json = new JSONObject(); 
					
					// put some value pairs into the JSON object . 				
					json.put("success", 1);
					json.put("JSESSIONID", sessionID);
					json.put("X-Token", token2);
					
					// finally output the json string 
					out.print(json.toString());
					out.flush();
					
					// mobile webview
					} else if (WebView.contains("Mobile") && M.equals("M")){ 
						
						try {
							token2 = SQLAccess.token2(deviceId, context);
							
							// The token2 will be used as key-salt-whatever as originally planned.
							response.addHeader("X-Token", token2);
							
							JSONObject json = new JSONObject(); 
							
							json.put("Session", "raked"); 
							json.put("Success", "true"); 
							
							// this is necessary because the X-Token header did not appear in the native mobile app
							json.put("X-Token", token2);
			
							response.sendRedirect(otherContext.getContextPath() + "/tabularasa.jsp?JSESSIONID="+sessionID);		

							
						} catch (Exception e) {
							System.out.println(e.getMessage());
						}
					
					// standard path
					} else {
						try {
							token2 = SQLAccess.token2(deviceId, context);
							
							// The token2 will be used as key-salt-whatever as originally planned.
							response.addHeader("X-Token", token2);
							
							PrintWriter out = response.getWriter(); 
							
							//create Json Object 
							JSONObject json = new JSONObject(); 
							
							// put some value pairs into the JSON object . 				
							json.put("Session", "raked"); 
							json.put("Success", "true"); 
							
							// finally output the json string 
							out.print(json.toString());
							out.flush();
							
						} catch (Exception e) {
							System.out.println(e.getMessage());
						}

	
					}
					
				  }	
              
	           } else {

	        	   response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Line 208");
	           }
		
		} catch (Exception e) {
			
			try {
				
				SQLAccess.reset_voucher(voucher, context);
				
			} catch (Exception e1) {
			
				log.info("Voucher reset FAILED!");
				
				}
		
		}

           } 
        
        } else {
        	
			try {
				
				SQLAccess.reset_voucher(voucher, context);
				
			} catch (Exception e1) {
			
				log.info("Voucher reset FAILED!");
				
				}
			
     	   response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, "Line 251");
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
