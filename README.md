General Authentication Service @George Gaspar
(in development)

The complete service system consists of several layers that makes it easy alter or extend the functionalities:

- dB layer (MySQL)
----
- (ORM) service model with Hibernate
- Data Access Object layer
- RESTful service that passes the DAO objects to the controller
- RESTful controller layer to provide HTTP methods
----
- TOMCAT servlet containers as middleware component
- APACHE httpd server with mod_jk connector to front TOMCAT (optional for load-balancing) 
    - both configured to run on SSL (required - right now the iOS part is configured to use Certificate Authority (CA) -> https://blog.httpwatch.com/2013/12/12/five-tips-for-using-self-signed-ssl-certificates-with-ios/)
----
- TOMCAT servlet/JSP container 
- deploy directly your optional UI interface onto TOMCAT (Note: the AngularJS is the preferred and tested)
- as for mobiles you can use the registration/login service through web view, too, besides the native way

The project contains the source code of the whole system in the dedicated branches (see descriptions).

In order to deploy you need to compile it in a given way and then copy the compiled files onto TOMCAT (Please refer to the official Maven build procedures, as applicable):

mvn -Dmaven.test.skip=true compile 

If the required dependencies will not be resolved and copied out to the predefined folder in the pom.xml, use this command (then rename the dependencies folder manually to "lib", and copy it along with the classes folder):

mvn dependency:copy-dependencies -Dclassifier=sources -Dmaven.test.skip=true compile

Deploy:
--
- Run the dB scripts to create the dB schema necessary to operate.
- Place the compiled classes with their directory structure as it is and the web.xml in the ${TOMCAT_BASE}\webapps\ROOT\WEB-INF folder where web.xml file resides: the classes folder including the compiled classes. The "lib" folder at the same level will hold the required dependencies (Please refer to the official standard Tomcat directory structure).
- Listeners are initialized with annotations, but you can change it back to use them directly using the web.xml (Please refer to the Java Http Servlet docs for it)
- Place the required log configuration file into the right class folder
- Insert initial vouchers with activation flags into the voucher_states table, if you want to start to register, or just put a username and a hashed password (with the same hashing algorithm that you selected in your client apps) into the logins table. 
- unique username function is not implemented yet fully!

Maintenance:
- if you want to clear / clean up the dB or of devices, but not of the user, you need to truncate the following tables:  Last_seen, Tokens, device_states, devices. 

Usage:
--
- The general concept was to create an entity class of a user that consists of username, uuid, password, device, voucher and login status(sessionid).
- The main link should be the uuid.
