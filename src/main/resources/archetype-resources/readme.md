Java EE Security Quickstart Maven Archetype
=========================================

Summary
-------
The project is a Maven archetype for Java EE MVC web application. The configuration in 
this project is specific to Wildfly, so the instructions here are Wildfly specific. 

Otherwise none of the code uses anything vender specific, so it is left up to the user 
to port the configuration to any other Java EE compliant server.

This project was inspired by kolorobot's Spring-Mvc-Quickstart-Archetype project.
This readme.md started with the readme.md from the same.

Generated project characteristics
-------------------------
* Java EE MVC web application for Wildly 9 environment
* JSF 2.2 and Bootstrap
* JPA 2.1
* H2DB (H2 Development Database) 
* JUnit/Arquillian/Drone/Graphene for testing
* Java EE Security Supported by JBoss/Wildfly Database Module 

Installation
------------

To install the archetype in your local repository execute following commands:

```bash
    git clone https://github.com/knicholas/javaee-security-archetype.git
    cd javaee-security-archetype
    mvn clean install
```

Create a project
----------------

```bash
    mvn archetype:generate \
        -DarchetypeGroupId=jsec \
        -DarchetypeArtifactId=javaee-security-archetype \
        -DarchetypeVersion=1.0.0-SNAPSHOT \
        -DgroupId=my.groupid \
        -DartifactId=my-artifactId \
        -Dversion=version
```

Install the security-domain
----------------

The jboss-security-domain.xml is the xml fragment that needs to be
copied to the wildfly server's configuration file, typically
standalone.xml. Find the <security-domains> section of the
configuration file and insert the contents of the jboss-security-domain.xml
into the section as a new <security-domain>. This could conceivably be 
done through wildfly commands, but I haven't sorted that out yet.   

Test the project
----------------

First edit local wildfly server's standalone.xml configuration file to 
add the javaee-security security-domain. See specific instructions above.
Start the local wildfly server, e.g., $JBOSS_HOME/bin/standalone.sh
If you want to watch it run the tests, then install the firefox browser.

For headless testing, use:
```bash
    mvn test -Parq-wildfly-remote
```

For testing using firefox, use:
```bash
    mvn test -Parq-wildfly-remote,firefox
```

Install the project
----------------

First edit local wildfly server's standalone.xml configuration file to 
add the javaee-security security-domain. See specific instructions above.
Start the local wildfly server, e.g., $JBOSS_HOME/bin/standalone.sh

```bash
    mvn install wildfly:deploy
```

Test on the browser
-------------------

First edit local wildfly server's standalone.xml configuration file to 
add the javaee-security security-domain. See specific instructions above.
Start the local wildfly server, e.g., $JBOSS_HOME/bin/standalone.sh

    http://localhost:8080/jsec


Uninstall the project
----------------

First edit local wildfly server's standalone.xml configuration file to 
add the javaee-security security-domain. See specific instructions above.
Start the local wildfly server, e.g., $JBOSS_HOME/bin/standalone.sh

```bash
    mvn wildfly:undeploy
```

Creating a new project in Eclipse
----------------------------------

* Import archetype URI by `Import ... > Projects from Git > Clone URI`
* Install the archetype in local repository with `mvn install`
* Go to `Preferences > Maven > Archetypes` and `Add Local Catalog`
* Select the catalog from file (`archetype-catalog.xml`) 
* Create new Maven project and select the archetype

