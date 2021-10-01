# RequestIdLoggingFilter
A small library to add x-request-id and x-correlation-id logging support for Tomcat and Spring apps

## Usage

### Maven

Add the dependency and repository to the `pom.xml`:
```xml
<project>
  ...
  <dependencies>
    <dependency>
      <groupId>io.github.jtgasper3</groupId>
      <artifactId>requestid-logging</artifactId>
      <version>0.0.1-SNAPSHOT</version>
    </dependency>
  <dependencies>

  <repositories>
    <repository>
      <id>my-internal-site</id>
      <url>https://maven.pkg.github.com/jtgasper3/requestid-logging</url>
    </repository>
  </repositories>
  ...
</project>
```

### Gradle

Add the dependency and repository to the `build.gradle`:

```groovy
repositories {
    maven { url "https://maven.pkg.github.com/jtgasper3/requestid-logging" }
}

dependencies {
    implementation "io.github.jtgasper3:requestid-logging:0.0.1-SNAPSHOT"
}
```

## Overriding Default Filter Values

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xmlns="http://java.sun.com/xml/ns/javaee"
         xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd"
         version="2.5">
    <filter>
        <filter-name>RequestIdLoggingFilter</filter-name>
        <filter-class>io.github.jtgasper3.requestlogging.RequestIdLoggingFilter</filter-class>
        <init-param>
            <param-name>RequestIdHeaderName</param-name>
            <param-value>X-Request-Id</param-value>
        </init-param>
        <init-param>
            <param-name>RequestIdMdcName</param-name>
            <param-value>X-Request-Id</param-value>
        </init-param>
    </filter>
    <filter-mapping>
        <filter-name>RequestIdLoggingFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
</web-app>
```

## Logging Provider Configs

## Log4j2

`%X{X-Request-Id}`

```xml
<Configuration status="INFO">
    <Appenders>
        <Console name="stdout" target="SYSTEM_OUT">
            <PatternLayout pattern="%-4r [%t] %5p %c{1} - %X{X-Request-Id} - %m %n" />
        </Console>
    </Appenders>
    <Loggers>
        ...
    </Loggers>
</Configuration>
```

### Logback

`%X{X-Request-Id}`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>    
  <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">        
    <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
      <Pattern>%d{HH:mm:ss.SSS} [%thread] %X{X-Request-Id} %-5level %logger{36} - %msg%n</Pattern>
    </encoder>    
  </appender>    
  ...
</configuration>
```
