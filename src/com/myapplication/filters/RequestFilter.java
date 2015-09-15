package com.myapplication.filters;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse; 
import org.apache.log4j.Logger;

import com.myapplication.MyServletRequestWrapper;
 
	public class RequestFilter implements Filter {

		private static Logger log = Logger.getLogger(Logger.class.getName());
 
		public RequestFilter(){
			super();
		}
 
	public void init(FilterConfig filterConfig) throws ServletException {
	
	}
	 
	public void doFilter( ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
	 
	MyServletRequestWrapper httpReq = new MyServletRequestWrapper((HttpServletRequest)req);
	HttpServletResponse httpRes = (HttpServletResponse)res;
	 	 
	httpReq.addHeader("myHeader", "value");
	log.info("New headers added");
	 
	filterChain.doFilter(httpReq, httpRes);
	 
	}
	 
	public void destroy(){
}
}


