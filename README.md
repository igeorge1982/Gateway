General Authentication Service @George Gaspar
(in development)

The complete service system consists of several layers that makes it easy to alter or extend the functionalities. The server part is deployable as it is, just take care of the web.xml. 

The iOS swift code contains both type of login method, because it is a research code, not a standalone application, but it also demonstrates that they subsequently work and can complement each other (the registration is not implemented yet in the native way, but it will work similarly).

Documentation will be coming soon.
----

The structure:
----
- dB layer (MySQL)
- (ORM) service model with Hibernate:
- Data Access Object layer
- RESTful service that passes the DAO objects to the controller
- RESTful controller layer to provide HTTP methods
- TOMCAT servlet container as middleware component, with crossContext enabled (required)
- APACHE httpd server with mod_jk connector to front TOMCAT (optional for load-balancing) 

Configured to run on SSL only, which is required as right now the iOS part is configured to use Certificate Authority (CA) -> https://blog.httpwatch.com/2013/12/12/five-tips-for-using-self-signed-ssl-certificates-with-ios/)
----
- as for WWW platform deploy directly your optional UI interface solution (Web app) onto TOMCAT (Note: the AngularJS is the preferred and tested)
- as for mobiles you can use the registration/login service through webview, too, besides the native way


Notes on Windows:
- mod_jk - ISAPI redirector - NSAPI redirector is available to front the IIS
- NHibernate is availabe as an object-relational mapping (ORM) solution for the Microsoft .NET platform. 

Deploy:
----
The project contains the source code of the whole system in the dedicated branches (see descriptions).

In order to deploy you need to compile it in a given way and then copy the compiled files onto TOMCAT (Place the compiled classes with their directory structure as it is and the web.xml in the ${TOMCAT_BASE}\webapps\ROOT\WEB-INF folder where the web.xml file resides. The "lib" folder at the same level will hold the required dependencies. Please refer to the official standard Tomcat directory structure and to the official Maven build procedures, as applicable):

mvn -Dmaven.test.skip=true compile 

If the required dependencies will not be resolved and copied out to the predefined folder set in the pom.xml, then use this command (then rename the dependencies folder manually to "lib", and copy it along with the classes folder to the destination directory):

mvn dependency:copy-dependencies -Dclassifier=sources -Dmaven.test.skip=true compile


- Run the dB scripts to create the dB schema necessary to operate.
- Create the dB user for the application
- Check the web.xml if the configuration is correct, and every class is defined correctly
- Listeners are initialized with annotations, but you can change it back to use them directly using the web.xml (Please refer to the Java Http Servlet documentation for it)
- Place the required log configuration file into the right class folder, and configure it for your needs
- Insert initial vouchers with activation flags set into the voucher_states table, if you want to have registration, or just put a username and a hashed password (with the same hashing algorithm that you selected in your client apps) into the logins table. 
- Registration workflow is implemented with or without voucher activation
- Unique username and email checking is not implemented yet fully, but the supplied API will perform the check
- The servlet context also makes it available to check the active users with a designated API call  (check the context for the available lists)

After you have built and deployed all parts (web app, iOS) of the service, you have to be able to login through WWW (mobile, too), native iOS and iOS webview from the app.

Maintenance:
- if you want to clear / clean up the dB or of devices, but not of the user, because something went wrong, you need to truncate the following tables:  Last_seen, Tokens, device_states, devices. 


The general concept was to create an entity class of a user that consists of username, uuid, password, device, voucher and login status(sessionid). The main link should remian the uuid.

Usage:
- For registration you need to provide a preset and available voucher, username, email and password. After having successfully registered or logged in, you will receive a token part that you need to use for API calls. Upon each login you will receive a new token part.
