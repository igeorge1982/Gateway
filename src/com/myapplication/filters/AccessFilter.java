package com.myapplication.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AccessFilter implements Filter{
	
	private volatile static String user;

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
	   
		HttpServletRequest request = (HttpServletRequest) req;
	    HttpServletResponse response = (HttpServletResponse) res;
	    
	    HttpSession session = request.getSession(false);
	    
		user = (String) session.getAttribute("user");	
	    String loginURL = request.getContextPath() + "/logout"; 

	    if (user == null) {       
    		response.sendRedirect(loginURL);
	    } else {
	        chain.doFilter(request, response);
	    }
	}

	@Override
	public void init(FilterConfig config) throws ServletException {

	}		
	

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
}
