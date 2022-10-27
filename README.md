# ELEGANT Code Verification Service

A web service that ...

## Description

This project aims to build a RESTful API in Java that can submit requests for ...

The project uses Jakarta EE 9 and Eclipse GlassFish 6 to implement a portable API for the development, exposure, and accessing of the ELEGANT
Code Verification Webservice.

## Requirements

### 1. JDK 8

(TODO): Add instructions. Tested with:  openjdk-8u222-b10, oraclejdk-8u341.

## Installation

### 1. Clone the project:

```bash 
git clone https://github.com/elegant-h2020/Elegant-Code-Verification-Service.git
```

### 2. Build the service:

```bash
mvn clean install
```

### 3. Deploy a GlassFish server

a) Download GlassFish 6.0.0:

```bash
$ wget 'https://www.eclipse.org/downloads/download.php?file=/ee4j/glassfish/glassfish-6.0.0.zip' -O glassfish-6.0.0.zip
$ unzip glassfish-6.0.0.zip
$ cd glassfish-6.0.0.zip
$ echo "AS_JAVA=<path-to-JDK8>" >> ./glassfish/config/asenv.conf
```

b) Start GlassFish local server:

```bash
./bin/asadmin start-domain domain1
```

c) Stop GlassFish local server:

```bash
./bin/asadmin stop-domain domain1
```

## Licenses

(TODO)

## Additional Resources

```bash
https://blogs.oracle.com/javamagazine/post/transition-from-java-ee-to-jakarta-ee

http://www.mastertheboss.com/jboss-frameworks/resteasy/getting-started-with-jakarta-restful-services/
````
