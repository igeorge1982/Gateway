package com.myapplication.listeners;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

public class SessionBindingListener implements  HttpSessionBindingListener {
	  ServletContext context;

	  public SessionBindingListener(ServletContext context) {
	    this.context = context;
	  }

	  public void valueBound(HttpSessionBindingEvent event) {
	    context.log("The value bound is " + event.getName());
	  }

	  public void valueUnbound(HttpSessionBindingEvent event) {
	    context.log("The value unbound is " + event.getName());
	  }
	}