package com.myapplication;

/**
 * @author George Gaspar
 * @email: igeorge1982@gmail.com 
 * 
 * @Year: 2015
 */

import java.io.*;
import java.util.HashMap;
import javax.servlet.*;
import javax.servlet.http.*;
import com.myapplication.SQLAccess;
import org.apache.log4j.Logger;
import org.json.JSONObject;


//Extend HttpServlet class
public class AdminServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
	private volatile static String user = null;
	private volatile static String token_;
	private volatile static String Response = null;
	private volatile static String deviceId;
	protected volatile static HttpSession session = null;
	protected volatile static String sessionId = null;
	protected volatile static String sessionId_ = null;
	
	private static Logger log = Logger.getLogger(Logger.class.getName());
	private static volatile HashMap<String, HttpSession> activeUsers;


    public void init() throws ServletException
    {
        // Do required initialization

    }
    
    public synchronized void doPost(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException
    {

    }
    

	private synchronized void performTask(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {	

			//session = request.getSession();
    		ServletContext context = session.getServletContext();
			//log.info("Session ID check: "+ session.getId());
		
		try {
			
			// Get deviceId from session
			deviceId = (String) session.getAttribute("deviceId");
			
			// Get user from session
			user = (String) session.getAttribute("user");
			
			// Get token1 from dB. token1 will be used to make a user related API call
			token_ = SQLAccess.token(deviceId, context);
			
			// Get voucher activation method
			Response = SQLAccess.isActivated(user, context);

		
		} catch (Exception e) {
			
			response.setContentType("application/json"); 
			response.setCharacterEncoding("utf-8"); 
			response.setStatus(502);

			PrintWriter out = response.getWriter(); 
			
			//create Json Object 
			JSONObject json = new JSONObject(); 
			
			// put some value pairs into the JSON object . 				
			json.put("SQLAccess", "failed"); 
			json.put("Success", "false"); 
			
			// finally output the json string 
			out.print(json.toString());
			out.flush();
			
		}
		
		if (deviceId == null || user == null) {
			
			response.setContentType("application/json"); 
			response.setCharacterEncoding("utf-8"); 
			response.setStatus(502);

			PrintWriter out = response.getWriter(); 
			
			//create Json Object 
			JSONObject json = new JSONObject(); 
			
			// put some value pairs into the JSON object . 				
			json.put("deviceId", "null"); 
			json.put("user", "null"); 
			
			// finally output the json string 
			out.print(json.toString());
			out.flush();

		}
		
		// else if voucher needs activation
		if(Response == "S") {
				
				response.setContentType("application/json"); 
				response.setCharacterEncoding("utf-8"); 
				response.setHeader("Response", "S");
				response.setStatus(300);
				response.setHeader("User", user);

				PrintWriter out = response.getWriter(); 
				
				//create Json Object 
				JSONObject json = new JSONObject(); 
				
				// put some value pairs into the JSON object . 				
				json.put("Activation", "false"); 
				json.put("Success", "false"); 
				
				// finally output the json string 
				out.print(json.toString());
				out.flush();

			}
		
		// Get user entity using API GET method, with user and token as request params
		else if(token_ != null && session != null /*&& request.isRequestedSessionIdValid()*/) {			
			
			String webApiContext = context.getInitParameter("webApiContext");
			String webApiContextUrl = context.getInitParameter("webApiContextUrl");
			
        ServletContext otherContext = getServletContext().getContext(webApiContext);

			RequestDispatcher rd = otherContext.getRequestDispatcher(webApiContextUrl + user.trim().toString()+"/"+token_.trim().toString());

			rd.forward(request, response); 
			}
		else {
			
			response.setContentType("application/json"); 
			response.setCharacterEncoding("utf-8"); 
			response.setStatus(502);

			PrintWriter out = response.getWriter(); 
			
			//create Json Object 
			JSONObject json = new JSONObject(); 
			
			// put some value pairs into the JSON object . 				
			json.put("Success", "false"); 
			
			// finally output the json string 
			out.print(json.toString());
			out.flush();
			
				}

		}
    
    @SuppressWarnings("unchecked")
	public synchronized void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {

    	// Set the response message's MIME type
        response.setContentType("text/html;charset=UTF-8");
               
        // Get JSESSION url parameter. Later it needs to be sent as header
        sessionId = request.getParameter("JSESSIONID");			
        log.info("SessionId from request parameter: " + sessionId);
       
        // Return current session
		session = request.getSession();		
        //log.info("admin SessionID:" + session.getId().toString());
    	
        // Check session for user attribute
    	if(session.getAttribute("user") == null){
    	
            if (sessionId != null) {
                
                // Get the existing session and creates a new one
            	session = request.getSession(true);
            	
            	// Get ServletContext
                ServletContext context = session.getServletContext();
                
                // Init HashMap that stores session objects
                activeUsers = (HashMap<String, HttpSession>)context.getAttribute("activeUsers");
                
                // Get session with sessionId
                session = activeUsers.get(sessionId);
                //log.info("activeUsers sessionId:" + session.getId().toString());
            
            } else {
            	
    			response.setContentType("application/json"); 
    			response.setCharacterEncoding("utf-8"); 
    			response.setStatus(502);

    			PrintWriter out = response.getWriter(); 
    			
    			//create Json Object 
    			JSONObject json = new JSONObject(); 
    			
    			// put some value pairs into the JSON object . 				
    			json.put("acticeUsers", "failed"); 
    			json.put("Success", "false"); 
    			
    			// finally output the json string 
    			out.print(json.toString());
    			out.flush();
    			
            }
    	
    	}
    	
        if (session == null || session.getAttribute("user") == null) {
        	
        	response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Line 220");

    		session = request.getSession(false);		

        	try {
        	sessionId_ = session.getId();
        	activeUsers.remove(sessionId_);
        		} catch (Exception e) {
        		log.info("No sessionId found during invalid session request...");
        	}
        	

        } else {
    		
		            performTask(request, response);
    			//	log.info("CurrentUserSessionId: " + sessionId);
        	}
    	
    	}
    
    
    public void destroy()
    {
        // do nothing.
    }
  
   
}