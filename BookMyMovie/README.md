Selenium html project
---

This contains the source code for running sample [Selenium](http://http://www.seleniumhq.org) tests using [TestNG](http://www.testng.org).

In order to run the tests, build or run with [Apache Maven](http://maven.apache.org)

To compile and run all tests with Maven, run:

    mvn -Dmaven.test.skip=true package

To run a single test, run:

    mvn -Dtest=your.test.java.class test
    
To run the build output, run:

	java -jar app.jar
	
The jar file will run the tests in the testng.xml. Set the properties to your needs, too.