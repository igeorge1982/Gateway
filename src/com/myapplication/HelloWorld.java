package com.myapplication;

//Import required java libraries
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import com.myapplication.SQLAccess;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

//Extend HttpServlet class
public class HelloWorld extends HttpServlet {
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
	public volatile static String hash1;
	public volatile static String deviceId;
	public volatile static boolean devices;
	private volatile static long SessionCreated;
	
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
        try {
    		pass = request.getParameter("pswrd");	
    		user = request.getParameter("user");	
    		deviceId = request.getParameter("deviceId");
    		
			hash1 = SQLAccess.hash(pass);
			devices = SQLAccess.insert_device(deviceId, user);
		
		} catch (Exception e) {
			
    		response.sendRedirect("https://localhost/javaScript/mainpage.html");

		}
		
			if(pass.equals(hash1) && devices){
				
				//Create session
				HttpSession session = request.getSession();
			
				// synchronized session object to prevent concurrent update		        	   
				synchronized(session) {

				session.setAttribute("user", user);
				session.setAttribute("deviceId", deviceId);
				request.removeAttribute("pswrd");
				SessionCreated = session.getCreationTime();

		           }

				try {
					SQLAccess.insert_sessionCreated(deviceId, SessionCreated);
				} catch (Exception e) {					
				}

				//setting session to expiry in 30 mins
				session.setMaxInactiveInterval(30*60); 	
	        	
				//ServletContext otherContext = getServletContext().getContext("/exercise_");

				String encodedURL = response.encodeRedirectURL("https://localhost/example/index.jsp");
				response.sendRedirect(encodedURL);

			}else{
	    		response.sendRedirect("https://localhost/javaScript/mainpage.html");
	    		
	    		/*
				RequestDispatcher rd = getServletContext().getRequestDispatcher("https://localhost/javaScript/mainpage.html");
				rd.include(request, response);
				out.println("<font color=red>User is not found</font>");
				*/
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