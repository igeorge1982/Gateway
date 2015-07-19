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
	public static String pass;
	public static String user;
	
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
    
    public synchronized void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	// Set response content type
        response.setContentType("text/html");
	      
        PrintWriter out = response.getWriter();

        // Actual logic goes here.
		//pass = request.getParameter("pswrd");
		//out.println("<h1>" + pass + "</h1>");			
		
		
		user = request.getParameter("user");
		
		if(!request.getParameter("pswrd").isEmpty()){

			HttpSession session = request.getSession();
			session.setAttribute("user", "Pankaj");

			//setting session to expiry in 30 mins
			session.setMaxInactiveInterval(30*60);
			Cookie userName = new Cookie("user", user);
			response.addCookie(userName);
			String encodedURL = response.encodeRedirectURL("https://localhost/login/admin");
			response.sendRedirect(encodedURL);

		}else{
			RequestDispatcher rd = getServletContext().getRequestDispatcher("https://localhost/login/admin");
			out.println("<font color=red>User is not found</font>");
			rd.include(request, response);
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