General Authentication Service @George Gaspar

The complete service system consists of several layers that makes it easy alter or extend the functionalies that the service provides:

- dB layer (MySQL)
----
- (ORM) service model with Hibernate
- Data Access Object layer
- RESTful service that passes the DAO objects to the controller
- RESTful controller layer to provide HTTP methods
----
- TOMCAT servlet containers as midleware component
- APACHE httpd server with mod_jk connector that will serve your login pages
    - both configured to run on SSL
----
- TOMCAT JSP container 
- place directly your optional UI interface with ViewModel (AngularJS) in the JSP
- as for mobiles you can use the service through webview

This project contains the source code for the server side midleware components and dB scripts, only.

In order to run to deploy you need to compile it:

mvn -Dmaven.test.skip=true compile 

If required dependencies will not be resolved and copied out, use this command (then rename the dependencies folder to lib, and copy it along with the classes folder):

mvn dependency:copy-dependencies -Dclassifier=sources -Dmaven.test.skip=true compile

Run the dB scripts to create the dB schame neccessary to operate.

Place the compiled classes with their directory structure as it is and the web.xml in the ${TOMCAT_BASE}\webapps\ROOT\WEB-INF folder.