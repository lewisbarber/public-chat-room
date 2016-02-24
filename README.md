Public Chat Room
================

A public chat room built using Java Spring Framework 4, Web Sockets and STOMP. This web application uses the following technologies:

__**Server**__

* [Spring Data](http://projects.spring.io/spring-data/) - used as a wrapper on top of Hibernate to simplify data-access implementation
* [Spring Web MVC](http://docs.spring.io/spring/docs/current/spring-framework-reference/html/mvc.html) - used to host the chat service as a web application
* [Spring Messaging](http://spring.io/guides/gs/messaging-stomp-websocket/) - used for sending / receiving STOMP messages
* [Spring Websocket](http://spring.io/guides/gs/messaging-stomp-websocket/) - used for sending / receiving messages in real-time via [HTML5 Web Sockets protocol](http://en.wikipedia.org/wiki/WebSocket)
* [Jackson](http://wiki.fasterxml.com/JacksonInFiveMinutes) - used for JSON serialization
* [JUnit](http://junit.org/) & [Mockito](https://code.google.com/p/mockito/) - used for unit testing
* [Gradle](http://www.gradle.org/) - used for dependency management
 
__**Database**__

* [MySQL](http://www.mysql.com/) - used to persist application data
 
__**Web Client**__

* [jQuery](http://jquery.com/) - used to build an interactive web front-end

Configuration
-------------
To run this application you should follow the steps below:

###1) Fork Code###

Fork this GitHub application and fire it up in your favourite Java IDE (probably Eclipse or IntelliJ)

###2) Add Properties File###

Add a file to the *src/main/resources* directory called *application-dev.properties* with the following contents:

```properties
#Development database connection details
dataSource.driverClass=com.mysql.jdbc.Driver
dataSource.jdbcUrl=jdbc:mysql://localhost:3306/publicChatRoom?useUnicode=true&characterEncoding=UTF-8
dataSource.user=USER_NAME
dataSource.password=PASSWORD
dataSource.hibernateDialect=org.hibernate.dialect.MySQL5InnoDBDialect
dataSource.engine=MYSQL

#Database config
showSql=true
generateDdl=true

#Environment
env=Development
```

###3) Set Application Context###

You need to set the application context in the *main.js* file. For example, if you plan to deploy your application to www.yourdomain.com/Application you should set the following value:

```javascript
var appContext = "/Application";
```

If you plan to deploy the application to the root context then set the value as follows:

```javascript
var appContext = "";
```

###4) Create Database###

Create a MySQL database called *publicChatRoom* and run the following script:

```mysql
CREATE TABLE IF NOT EXISTS `clientSession` (
  `sessionId` varchar(255) NOT NULL,
  `dateAdded` datetime NOT NULL,
  `login` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`sessionId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `message` (
  `messageId` int(11) NOT NULL AUTO_INCREMENT,
  `dateSent` datetime NOT NULL,
  `message` varchar(4000) NOT NULL,
  `clientSessionId` varchar(255) DEFAULT NULL,
  `username` varchar(255) NOT NULL,
  `seenBy` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`messageId`),
  KEY `FK_lb1hrh2tnguxvirvqpgiwvy0t` (`clientSessionId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=74 ;

ALTER TABLE `message`
  ADD CONSTRAINT `FK_lb1hrh2tnguxvirvqpgiwvy0t` FOREIGN KEY (`clientSessionId`) REFERENCES `clientSession` (`sessionId`);
```

###5) Deploy WAR File###

Export the application as a WAR file and deploy to Apache Tomcat 7 (or your preferred application server that supports Java Servlet API 3.0 or greater).
