package com.myapplication.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CORSFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
       
        HttpServletResponse httpResp = (HttpServletResponse) resp;

            // This is a cross-domain request, add headers allowing access
        	httpResp.setHeader("Access-Control-Allow-Headers", "X-Requested-With, X-Token");    
        	httpResp.setHeader("Access-Control-Expose-Headers", "X-Token");
         
            httpResp.setHeader("Access-Control-Allow-Origin", "*");
            httpResp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
            httpResp.setHeader("Access-Control-Max-Age", "3600");
            chain.doFilter(req, resp);

 }

    public void init(FilterConfig config) throws ServletException {

    }
}