package com.myapplication;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class RohadekFilter implements Filter
{
    @Override
    public void doFilter( ServletRequest request, ServletResponse response, FilterChain chain )
                          throws IOException, ServletException
    {
    	  HttpServletResponse response_ = (HttpServletResponse) response;

          response_.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
          response_.setHeader("Pragma", "no-cache"); // HTTP 1.0.
  	 	  response_.setDateHeader("Expires", 0);	 	

          chain.doFilter(request, response);
    }

    @Override
    public void init( FilterConfig filterConfig )
    {        
        
    }

    @Override
    public void destroy()
    {
    }
}