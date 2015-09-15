package com.myapplication.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import com.myapplication.Token;


public class DecodeFilter implements Filter {
	
	private static volatile String token;
	private static volatile String decryptedToken;

    @Override
    public void doFilter (ServletRequest request, ServletResponse response, FilterChain chain)
                          throws IOException, ServletException {
    	
  	  HttpServletResponse response_ = (HttpServletResponse) response;

		token = response_.getHeader(TokenFilter.TOKEN_HEADER);

		try {

			decryptedToken = Token.decrypt(token);

			//TODO: decrpyptedToken to match the salt; 
			if (!decryptedToken.isEmpty()) {
				response_.addHeader("test", decryptedToken);
			}
		} catch (Exception e) {
			response_.sendError(HttpServletResponse.SC_UNAUTHORIZED, "REST signature failed validation.");
			return;
		}

		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void destroy() {
		
	}
	
}
