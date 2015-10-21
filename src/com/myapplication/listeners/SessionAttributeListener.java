package com.myapplication.listeners;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import org.apache.log4j.Logger;


public class SessionAttributeListener implements HttpSessionAttributeListener {

	private static Logger log = Logger.getLogger(Logger.class.getName());
	private static volatile String id;
	private static volatile String name;
	private static volatile String value;

  /** Creates new SessionAttribListen */
  public SessionAttributeListener() {

	  log.info(getClass().getName());
  }

  public void attributeAdded(HttpSessionBindingEvent se) {

    HttpSession session = se.getSession();
    id = session.getId();
    name = se.getName();
    value = (String) se.getValue();
    String source = se.getSource().getClass().getName();
    String message = new StringBuffer("Attribute bound to session in ")
        .append(source).append("\nThe attribute name: ").append(name)
        .append("\n").append("The attribute value:").append(value)
        .append("\n").append("The session ID: ").append(id).toString();
    log.info(message);
  }

  public void attributeRemoved(HttpSessionBindingEvent se) {

    HttpSession session = se.getSession();
    id = session.getId();
    name = se.getName();
    if (name == null)
      name = "Unknown";
    value = (String) se.getValue();
    String source = se.getSource().getClass().getName();
    String message = new StringBuffer("Attribute unbound from session in ")
        .append(source).append("\nThe attribute name: ").append(name)
        .append("\n").append("The attribute value: ").append(value)
        .append("\n").append("The session ID: ").append(id).toString();
    log.info(message);
  }

  public void attributeReplaced(HttpSessionBindingEvent se) {

    String source = se.getSource().getClass().getName();
    String message = new StringBuffer("Attribute replaced in session  ")
        .append(source).toString();
    log.info(message);
  }
}