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

import org.json.JSONObject;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

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
	private volatile static HttpSession session;
	private volatile static boolean devices;
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
    
    
    public synchronized void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	
    	// Set response content type
        response.setContentType("text/html");
        
	 	session = request.getSession(false);
      	
 	     if(session != null){
 	  		 
 		     session.invalidate();
 	     }
 		ServletContext context = request.getServletContext();
	      
        // Actual logic goes here.		
        try {
    		pass = request.getParameter("pswrd");	
    		user = request.getParameter("user");	
    		deviceId = request.getParameter("deviceId");
    		
			hash1 = SQLAccess.hash(pass, context);
			devices = SQLAccess.insert_device(deviceId, user, context);
		
		} catch (Exception e) {
			
    		response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "Line 75");

		}
        
        	if(pass.equals(hash1) && devices){
        		
        		// Create new session
				session = request.getSession(true);
			
				// synchronized session object to prevent concurrent update		        	   
				synchronized(session) {

				session.setAttribute("user", user);
				session.setAttribute("deviceId", deviceId);
				request.removeAttribute("pswrd");
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
		        
				ServletContext otherContext = getServletContext().getContext("/example");
				String encodedURL = response.encodeRedirectURL(otherContext.getContextPath() +"/index.jsp");
				response.sendRedirect(encodedURL);

			}else{
				
				response.setContentType("application/json"); 
				response.setCharacterEncoding("utf-8"); 
				response.setStatus(502);

				PrintWriter out = response.getWriter(); 
				
				//create Json Object 
				JSONObject json = new JSONObject(); 
				
				// put some value pairs into the JSON object . 				
				json.put("Session creation", "failed"); 
				json.put("Success", "false"); 
				
				// finally output the json string 
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