package com.myapplication;

//Import required java libraries
import java.io.*;

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

		HttpSession session = request.getSession();

    	HelloWorld_.user = null;
    	
    	if(session.getAttribute("user") == null){
    	
    		response.sendRedirect("https://localhost/javaScript/mainpage.html");
    	
    	}else HelloWorld_.user = (String) session.getAttribute("user");
    
    	
    	Cookie[] cookies = request.getCookies();
    	if(cookies !=null){
    	
    		for(Cookie cookie : cookies){
    	
    		if(cookie.getName().equals("user")) userName = cookie.getValue();
    		if(cookie.getName().equals("JSESSIONID")) sessionID = cookie.getValue();
    	}
    	}else{
    		sessionID = session.getId();
    	}
    	}
    
    
    public void destroy()
    {
        // do nothing.
    }
}