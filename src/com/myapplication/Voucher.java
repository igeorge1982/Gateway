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

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import com.myapplication.SQLAccess;


//Extend HttpServlet class
public class Voucher extends HttpServlet {
    /**
    *
    */
    private static final long serialVersionUID = 1L;
	private volatile static String voucher;
	
    @BeforeClass
    public void setUp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

    }
    
    @AfterClass
    public static void close(){
    	
    }

    public void init() throws ServletException
    {
    	
    }
    
    public synchronized void processRequest (HttpServletRequest request, HttpServletResponse response)
    	    throws ServletException, IOException {
		    	
        String userName = "admin";
        String password = "Tapsihapsi666";
    	request.login(userName, password); 
    	
    }
    
    public synchronized void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {

    	//TODO: do something with left voucher registrations
    	// Set response content type
        response.setContentType("text/html");			

        try {       		      
            // Actual logic goes here.
    		voucher = request.getParameter("voucher");
    		ServletContext context = request.getServletContext();
    		
			if (voucher != null && SQLAccess.voucher(voucher, context)) { 	
	        	String url = getServletContext().getInitParameter("voucherRedirect");
				String encodedURL = response.encodeRedirectURL(url+voucher);
				response.sendRedirect(encodedURL);
				
			}
			else {
				String elseUrl = getServletContext().getInitParameter("voucherElseRedirect");
				response.sendRedirect(elseUrl);
				
			}
		} catch (Exception e) {
        	response.sendError(HttpServletResponse.SC_BAD_GATEWAY);
		
		} 
        
    }
    
    public synchronized void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        
    	//processRequest(request, response);

    	// Set response content type
        response.setContentType("text/html");
	      
        try {
    		voucher = request.getParameter("voucher");
			
			if (voucher.trim().isEmpty()) {
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
