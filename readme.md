[Java EE MVC Security Quickstart Archetype](https://github.com/karlnicholas/javaee-security-quickstart-archetype)
=========================================

### Summary

This project is a [Maven Archetype](https://maven.apache.org/guides/introduction/introduction-to-archetypes.html) 
for Java EE MVC web application that implements [Java EE Security](https://docs.oracle.com/javaee/7/tutorial/security-webtier002.htm) features. 
The site demonstrates form based security implemented against a database. The site allows 
users to self-register. User information is stored in the database and new 
users are assigned the USER role. The project uses FORM based security for 
protection of specified views(URLs). Additionally, an ADMIN role is defined 
and a default administrator is inserted into the database at build-time. Admin's 
can inspect and manage all users. This project is intended to be instructive 
for developers interested in Java EE site security implementation.  
 
Java EE Servlet 3.0 HttpSevletRequest.login() and logout() define security, which is handled by 
the application server, in this case <a href="http://wildfly.org/">Wildfly 10</a>. 
User and Role entities are created and query strings are put into the Wildfly configuration files. 
A logged in user may access the UserDetail and ChangePassword URLs. 
Form based security is configured in web.xml and the browser is redirected to login forms when access to restricted resources is attempted. Role base security is also implemented for EJB's, but since there is no EJB client it is redundant and provided for completeness. The currently logged in user is stored in the http session so JSF pages can access user information and roles.      

None of the code uses anything vender specific, but the container configuration files are 
Wildfly specific. It is left up to the user to port to any other Java EE 
compliant server if desired. Wildfly 10 version was used. 

This project uses Junit/Arquillian/Drone/Graphene automated testing. 

This project was inspired by [kolorobot's](https://github.com/kolorobot) 
[Spring-Mvc-Quickstart-Archetype project.](https://github.com/kolorobot/spring-mvc-quickstart-archetype). 
This readme.md started with the readme.md from the same.

[You can see the project in action on OpenShift.](http://mvc-jsec.rhcloud.com/)
Note that `OpenShift` will shutdown the server after 48 hours of inactivity. If this 
has happened, you will need to refresh the site for a couple of minutes while `OpenShift` 
starts the project up again. 

### Project characteristics
* Java EE MVC web application for Wildly 10 environment
* JSF 2.2 and Bootstrap
* JPA 2.1
* H2DB (H2 Development Database) 
* JUnit/Arquillian/Drone/Graphene for testing
* Java EE MVC Supported by JBoss/Wildfly Database Module 

### Installation

To install the archetype in your local repository execute following commands:

```bash
    git clone https://github.com/knicholas/javaee-security-archetype.git
    cd javaee-security-archetype
    mvn clean install
```

### Create a project

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


###Install the security-domain

The security configuration of Wildfly needs to be updated because this 
archetype uses container managed security. There are two ways to do this.

1) Probably the best way, run the `configure-security-domain.cli` JBoss/Wildfly command line 
interface script. This will add a security domain named `javaee-security-quickstart`. If you want to 
change it, be sure to change the `src/main/webapp/WEB-INF/jboss-web.xml` file as well.

2) Manually add the file `javaee-security-domain.xml`, found in the root of your new project, to 
the Wildfly server's configuration file, which is typically standalone.xml. Find the `<security-domains>` 
section of the configuration file and insert the contents of the jboss-security-domain.xml into the 
section as a new `<security-domain>`. 

### Test the project

Insure Wildfy's `security-domain` is configured. See specific instructions above.
Start the local wildfly server, e.g., $JBOSS_HOME/bin/standalone.sh
If you want to watch it run the tests install Firefox.

For headless testing, use:
```bash
    mvn test -Parq-wildfly-remote
```

For testing using firefox, use:
```bash
    mvn test -Parq-wildfly-remote,firefox
```

### Install the project

Insure Wildfy's `security-domain` is configured. See specific instructions above.
Start the local wildfly server, e.g., $JBOSS_HOME/bin/standalone.sh

```bash
    mvn clean install wildfly:deploy
```

### Test on the browser

Insure Wildfy's `security-domain` is configured. See specific instructions above.
Start the local wildfly server, e.g., $JBOSS_HOME/bin/standalone.sh

    http://localhost:8080/jsec  (or your package name)


### Uninstall the project


```bash
    mvn wildfly:undeploy
```

### Create your own `admin@test.com` user and password.
The admin user and password is loaded from `src/main/resources/META-INF/load-script.sql`, and the password
is SHA-256/Base64 encoded, as specified in the jboss-security-domain.xml file. To change it, you can do either of 
the following.
  * Get a new password by editing the `src/main/java/service/UserSessionBean.java` file. At the end you will find a main method that encodes a string for you. Put your password of choice in the string and run it as a Java 
  application. Copy the output string to the load-script password field. Set the email field to a field of your
  choice. It's probably not a good idea check the changes showing your new password into source control.
  * Or, use the default user id and password of `admin@test.com/admin` and change the password after you login.
  If you want to change the email, create a new account and use the current admin account to promote the new account to admin. Then you will be able to use the new admin account to delete to old one if you want to.

### Creating a new project in Eclipse

This archetype has been added to `Maven.org` and so it is available in 
the list of maven archetypes for eclipse. 
  * Be sure no projects are selected in Eclipse.
  * Select File->New->Maven Project
  * type `javaee-security-quickstart` into the filter to find this archetype.
  * Follow the prompts.
