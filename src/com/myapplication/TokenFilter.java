package com.myapplication;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;


public class TokenFilter implements Filter
{
	public static final String TOKEN_HEADER = "Token";
	public static final String s = "This is a test";

    @Override
    public void doFilter( ServletRequest request, ServletResponse response, FilterChain chain )
                          throws IOException, ServletException
    {
    	  HttpServletResponse response_ = (HttpServletResponse) response;
          
          try{
                  
          MessageDigest m=MessageDigest.getInstance("MD5");
          m.update(s.getBytes(),0,s.length());
          
          String password = new BigInteger(1,m.digest()).toString(16);

          String passwordEnc = Token.encrypt(password);
          response_.addHeader(TOKEN_HEADER, passwordEnc);
          } catch (Exception e){
        	  
          }

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