package com.myapplication;

/**
 * @author George Gaspar
 * @email: igeorge1982@gmail.com 
 * 
 * @Year: 2015
 */

//Import required java libraries
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import com.myapplication.SQLAccess;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import com.myapplication.utils.hmac512;

//Extend HttpServlet class
public class HelloWorld extends HttpServlet {
    /**
    *
    */
    private static final long serialVersionUID = 1L;
	private volatile static String pass;
	private volatile static String user;
	private volatile static String hash1;
	private volatile static String deviceId;
	private volatile static String ios;
	private volatile static String WebView;
	private volatile static String M;
	private volatile static HttpSession session;
	private volatile static boolean devices;
	private volatile static long SessionCreated;
	private volatile static String sessionID;
	private volatile static String token2;
	private volatile static String hmac;
	private volatile static String hmacHash;
	private volatile static String time;

	private static Logger log = Logger.getLogger(Logger.class.getName());


    public void init() throws ServletException
    {
        // Do required initialization
    }
    
    
    public synchronized void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	
    	// Set response content type
		response.setContentType("application/json"); 
		response.setCharacterEncoding("utf-8"); 
		
	 	session = request.getSession(false);
      	
 	     if(session != null){
 	  		 
 		     session.invalidate();
 	     }
 		
 	     ServletContext context = request.getServletContext();
	      
        // Actual logic goes here.		
        try {
        	//TODO: we get the hmac hash from the header and match it against the hmac512 hash 
        	//that the server will present of the sha512 hash of username and password 
        	hmac = request.getHeader("X-HMAC-HASH");
        	
        	//TODO: add time constraint
        	//TODO: add content length validation
        	time = request.getHeader("X-MICRO-TIME");
    		pass = request.getParameter("pswrd");	
    		user = request.getParameter("user");	
    		deviceId = request.getParameter("deviceId");
    		ios = request.getParameter("ios");
    		WebView = request.getHeader("User-Agent");
    		M = request.getHeader("M");

    		
    		hmacHash = hmac512.getLoginHmac512(user, pass, deviceId, time);
    		    		
    		log.info("HandShake was given: "+hmac+" & "+hmacHash);
    		
			hash1 = SQLAccess.hash(pass, context);
			devices = SQLAccess.insert_device(deviceId, user, context);
		
		} catch (Exception e) {
			
    		response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "There is a big problem!");

		}

        	if(pass.equals(hash1) && devices && hmac.equals(hmacHash)){
        		
        		// Create new session
				session = request.getSession(true);
				
				// synchronized session object to prevent concurrent update		        	   
				synchronized(session) {

				session.setAttribute("user", user);
				session.setAttribute("deviceId", deviceId);
				session.removeAttribute("pswrd");
				SessionCreated = session.getCreationTime();
				sessionID = session.getId();
		           }

				try {
					SQLAccess.insert_sessionCreated(deviceId, SessionCreated, sessionID, context);
				} catch (Exception e) {	
					throw new ServletException();
				}

				//setting session to expiry in 30 mins
				session.setMaxInactiveInterval(30*60); 	
		        String homePage = getServletContext().getInitParameter("homePage");

				ServletContext otherContext = getServletContext().getContext(homePage);
				
				// X-Token should be sent as json response I guess
				// native mobile
						if (ios != null) {
						try {
						
						token2 = SQLAccess.token2(deviceId, context);
						
						response.setContentType("application/json"); 
						response.setCharacterEncoding("utf-8"); 
						response.setStatus(200);
		
						PrintWriter out = response.getWriter(); 
						
						JSONObject json = new JSONObject(); 
						
						json.put("success", 1);
						json.put("JSESSIONID", sessionID);
						json.put("X-Token", token2);
						
						out.print(json.toString());
						out.flush();
						
						} catch (Exception e) {
							e.printStackTrace();
						}
						
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
								e.printStackTrace();
							}
						
						} 
						// standard path
						else {
							try {
								token2 = SQLAccess.token2(deviceId, context);
								
								// The token2 will be used as key-salt-whatever as originally planned.
								response.addHeader("X-Token", token2);
								
								PrintWriter out = response.getWriter(); 
								
								JSONObject json = new JSONObject(); 
								
								json.put("Session", "raked"); 
								json.put("Success", "true"); 
								// this is necessary because the X-Token header did not appear in the native mobile app
								json.put("X-Token", token2);
								
								out.print(json.toString());
								out.flush();
								
							} catch (Exception e) {
								e.printStackTrace();
							}

		
						}
				

			}else {
				
				response.setContentType("application/json"); 
				response.setCharacterEncoding("utf-8"); 
				response.setStatus(502);

				PrintWriter out = response.getWriter(); 
				
				JSONObject json = new JSONObject(); 
				
				json.put("Session creation", "failed"); 
				json.put("Success", "false"); 
				
				out.print(json.toString());
				out.flush();
	    		
			}
        
    }
    
    public synchronized void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        
    	
    	// Set response content type
        response.setContentType("text/html");
        
	 	session = request.getSession(false);
      	
	     if(session != null){
	  		 
		     session.invalidate();
	     }
	      
        // Actual logic goes here.		
        try {
    		pass = request.getParameter("pswrd");	
    		user = request.getParameter("user");	
    		deviceId = request.getParameter("deviceId");
			
			if (user.trim().isEmpty() || pass.trim().isEmpty() || deviceId.trim().isEmpty()) {
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