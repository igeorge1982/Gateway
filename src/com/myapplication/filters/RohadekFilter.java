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


@SuppressWarnings("unused")
public class RohadekFilter implements Filter
{

    @Override
    public void doFilter( ServletRequest request, ServletResponse response, FilterChain chain )
                          throws IOException, ServletException
    {
    	  HttpServletResponse response_ = (HttpServletResponse) response;
    	  //HttpServletRequest request_ = (HttpServletRequest) request;


          response_.setHeader("Cache-Control", "no-cache, must-revalidate"); // HTTP 1.1.
          response_.setHeader("Pragma", "no-cache"); // HTTP 1.0.
          
          // Set IE extended HTTP 1.1 no-cache header
          response_.addHeader("Cache-Control", "post-check=0,pre-check=0");
          
          // Tell proxy caches not to cache a given resource
          response_.addHeader("Cache-Control", "proxy-revalidate");

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