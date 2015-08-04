package com.myapplication;

//Import required java libraries
import java.io.*;
import java.util.Date;
import java.util.Enumeration;
import java.util.ResourceBundle;

import javax.servlet.*;
import javax.servlet.http.*;

import com.myapplication.SQLAccess;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

//Extend HttpServlet class
public class AdminServlet extends HttpServlet {
    /**
    *
    */
    private static final long serialVersionUID = 1L;
	private String message;
	public final static String dbDriverClass = "com.mysql.jdbc.Driver";
	public final static String dbUrl = "jdbc:mysql://localhost:3306";
	public final static String dbUserName = "sqluser";
	public final static String dbPassWord = "sqluserpw";
	public static SQLAccess dao = new SQLAccess(dbDriverClass, dbUrl, dbUserName, dbPassWord);
	private static String userName = null;
	private static String sessionID = null;
	
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
    
    public synchronized void doPost(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException
    {
        
    }
    
    public synchronized void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {

    	//TODO: handle back button somehow
    	
    	// Set the response message's MIME type
        response.setContentType("text/html;charset=UTF-8");
       
        // Allocate a output writer to write the response message into the network socket
        PrintWriter out = response.getWriter();
   
        // Use ResourceBundle to keep localized string in "LocalStrings_xx.properties"
        ResourceBundle rb = ResourceBundle.getBundle("LocalStrings",  request.getLocale());
   
        // Write the response message, in an HTML page
        try {
           
           out.println("<!DOCTYPE html");  // HTML 5
           out.println("<html><head>");
           out.println("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
           
           String title = rb.getString("sessions.title");
           
           out.println("<head><title>" + title + "</title></head>");
           out.println("<body>");
           out.println("<h3>" + title + "</h3>");
   
           // Return the existing session if there is one. Otherwise, create a new session
    	
		HttpSession session = request.getSession();
		//TODO: add more attributes
    	HelloWorld.user = null;
    	
    	if(session.getAttribute("user") == null){
    	
    		response.sendRedirect("https://localhost/javaScript/mainpage.html");
    	
    	}else 
    		
    		HelloWorld.user = (String) session.getAttribute("user");
    	
    		//set HTTP headers
        	response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        	response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        	response.setDateHeader("Expires", 0);
        	
			//get HTTP headers
        	request.getHeader("Cache-Control"); // HTTP 1.1.
        	request.getHeader("Pragma"); // HTTP 1.0.
    
    		sessionID = session.getId();
    	
        out.println("<!DOCTYPE html");  // HTML 5
        out.println("<html><head>");
        out.println("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
        String title_ = rb.getString("requestinfo.title");
        out.println("<head><title>" + title_ + "</title></head>");
        out.println("<body>");
        out.println("<h3>" + title_ + "</h3>");

        // Tabulate the request information 
        out.println("<table>");
        out.println("<tr><td>" + rb.getString("requestinfo.label.protocol") + "</td>");
        out.println("<td>" + request.getProtocol() + "</td></tr>");
        out.println("<tr><td>" + rb.getString("requestinfo.label.method") + "</td>");
        out.println("<td>" + request.getMethod() + "</td></tr>");
        out.println("</td></tr><tr><td>");
        out.println("<tr><td>" + rb.getString("requestinfo.label.requesturi") + "</td>");
        out.println("<td>" + HTMLFilter.filter(request.getRequestURI()) + "</td></tr>");
        out.println("<tr><td>" + rb.getString("requestinfo.label.pathinfo") + "</td>");
        out.println("<td>" + HTMLFilter.filter(request.getPathInfo()) + "</td></tr>");
        out.println("<tr><td>Path Translated:</td>");
        out.println("<td>" + request.getPathTranslated() + "</td></tr>");
        out.println("<tr><td>" + rb.getString("requestinfo.label.remoteaddr") + "</td>");
        out.println("<td>" + request.getRemoteAddr() + "</td></tr>");
    	
    	// Display session information
        out.println(rb.getString("sessions.id") + " " + session.getId() + "<br />");
        out.println(rb.getString("sessions.created") + " ");
        out.println(new Date(session.getCreationTime()) + "<br />");
        out.println(rb.getString("sessions.lastaccessed") + " ");
        out.println(new Date(session.getLastAccessedTime()) + "<br /><br />");
        
        //TODO: get system.property for unique identifier
        
        // Set an attribute (name-value pair) if present in the request
        String attName = request.getParameter("attribute_name");
        
        if (attName != null) attName = attName.trim();
        
        String attValue = request.getParameter("attribute_value");
        
        if (attValue != null) attValue = attValue.trim();
        if (attName != null && !attName.equals("") && attValue != null && !attValue.equals("") ) {
           
        	// synchronized session object to prevent concurrent update
           synchronized(session) {
        	   
              session.setAttribute(attName, attValue);
           }
        }

        // Display the attributes (name-value pairs) stored in this session
        out.println(rb.getString("sessions.data") + "<br>");
        
        Enumeration<String> names = session.getAttributeNames();
        
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            String value = session.getAttribute(name).toString();
            
            out.println(HTMLFilter.filter(name) + " = " + HTMLFilter.filter(value) + "<br>");
        }
        out.println("<br />");

        // Display a form to prompt user to create session attribute
        out.println("<form method='get'>");
        out.println(rb.getString("sessions.dataname"));
        out.println("<input type='text' name='attribute_name'><br />");
        out.println(rb.getString("sessions.datavalue"));
        out.println("<input type='text' name='attribute_value'><br />");
        out.println("<input type='submit' value='SEND'>");
        out.println("</form><br />");

        out.print("<a href='");
        // Encode URL by including the session ID (URL-rewriting)
        out.print(response.encodeURL(request.getRequestURI() + "?attribute_name=foo&attribute_value=bar"));
        out.println("'>Encode URL with session ID (URL re-writing)</a>");
        out.println("</body></html>");
        
        out.close();  // Always close the output writer

     } catch (Exception e) {

    	 HttpSession session = request.getSession();
    	 session.invalidate();
    	 out.close();  // Always close the output writer

     }
    	
    	}
    
    
    public void destroy()
    {
        // do nothing.
    }
}