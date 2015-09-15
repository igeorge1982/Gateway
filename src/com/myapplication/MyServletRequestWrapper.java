package com.myapplication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.*;

	public class MyServletRequestWrapper extends HttpServletRequestWrapper{
	
		private Map<String, String> headerMap;

		public void addHeader(String name, String value){
		
			headerMap.put(name, value);
		}

		public MyServletRequestWrapper(HttpServletRequest request){
		
			super(request);
		
		headerMap = new HashMap<String, String>();
		
		}
		
		public Enumeration<String> getHeaderNames(){
		
			HttpServletRequest request = (HttpServletRequest)getRequest();
		
			List<String> list = new ArrayList<String>();
		
			for( Enumeration<String> e = request.getHeaderNames() ;  e.hasMoreElements() ; )
		
				list.add(e.nextElement().toString());
		
			for( Iterator<String> i = headerMap.keySet().iterator() ; i.hasNext() ; ){
		
				list.add(i.next());
		
			}
		
			return Collections.enumeration(list);
		
		}
		
		public String getHeader(String name){
		
			Object value;
		
			if((value = headerMap.get(""+name)) != null)
		
				return value.toString();
		
			else
		
				return ((HttpServletRequest)getRequest()).getHeader(name);
			}
}