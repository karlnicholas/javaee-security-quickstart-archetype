Java EE Security Quickstart Maven Archetype
=========================================

Summary
-------
This project is a Maven archetype for Java EE MVC web application that 
implements Java EE security features. The project allows users to self-register 
at the website. User information is stored in a database and new users are 
assigned the USER role. The project configures FORM based security for 
protection of specified views(URLs). Additionally, an ADMIN role is defined 
and a default administrator is inserted into the database at build-time. Admin's 
can inspect and manage all users. This project is intended to be a starting
point for websites/services that expect to require user self-registration 
and security.  
 
This program uses the Servlet 3.0 HttpSevletRequest.login() and logout() calls
in order to login users. Role base security is also implemented for EJB's, but 
since there is no client, EJB security is redundant but provided for completeness. 
The currently logged in user is stored in the session so that JSF pages can 
access user information and roles.      

None of the code uses anything vender specific, but the configuration is
Wildfly specific. It is left up to the user to port to any other Java EE 
compliant server if desired. Wildfly 9 version was used. 

This project has a Junit/Arquillian/Drone/Graphene testing setup that required 
release candidate versions of Arquillian POMS. Hopefully this will be updated 
when the final releases come out.  

This project was inspired by kolorobot's Spring-Mvc-Quickstart-Archetype project.
This readme.md started with the readme.md from the same.

[You can see the project in action on OpenShift.](http://mvc-jsec.rhcloud.com/) 

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
        -DarchetypeVersion=1.0.0 \
        -DgroupId=my.groupid \
        -DartifactId=my-artifactId \
        -Dversion=version
```
or
```bash
    mvn archetype:generate \
    -DarchetypeCatalog=local
```


Install the security-domain
----------------

The jboss-security-domain.xml is the xml fragment that needs to be
copied to the Wildfly server's configuration file, typically
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

First edit local Wildfly server's standalone.xml configuration file to 
add the javaee-security security-domain. See specific instructions above.
Start the local wildfly server, e.g., $JBOSS_HOME/bin/standalone.sh

```bash
    mvn clean install wildfly:deploy
```

Test on the browser
-------------------

First edit local wildfly server's standalone.xml configuration file to 
add the javaee-security security-domain. See specific instructions above.
Start the local wildfly server, e.g., $JBOSS_HOME/bin/standalone.sh

    http://localhost:8080/jsec  (or your package name)


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

