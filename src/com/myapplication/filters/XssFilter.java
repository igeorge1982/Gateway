package com.myapplication.filters;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public final class XssFilter implements Filter {

    static class FilteredRequest extends HttpServletRequestWrapper {

    	/* These are the characters allowed by the Javascript validation */
    	private final static String ALLOWEDCHARS = "0123456789";

    	public FilteredRequest(ServletRequest request) {
    		super((HttpServletRequest)request);
    	}

    	//actual logic goes here
    	public String sanitize(String input) {
    		String result = "";
    		for (int i = 0; i < input.length(); i++) {
    			if (ALLOWEDCHARS.indexOf(input.charAt(i)) >= 0) {
    				result += input.charAt(i);
    			}
    		}
    		return result;
    	}

    	//call request super class to act on the matching parameter  
    	@Override
    	public String getParameter(String paramName) {
    		String value = super.getParameter(paramName);
    		if ("voucher".equals(paramName)) {
    			value = sanitize(value);
    		}
    		return value;
    	}

    	//call request super class to act on the parameter value  
    	@Override
    	
    	public String[] getParameterValues(String paramName) {
    		String values[] = super.getParameterValues(paramName);
    		if ("voucher".equals(paramName)) {
    			for (int index = 0; index < values.length; index++) {
    				values[index] = sanitize(values[index]);
    			}
    		}
    		return values;
    	}
    }

    public void doFilter(ServletRequest request, ServletResponse response,
    		FilterChain chain) throws IOException, ServletException {
    	chain.doFilter(new FilteredRequest(request), response);
    }

    public void destroy() {
    }

    public void init(FilterConfig filterConfig) {
    }
}