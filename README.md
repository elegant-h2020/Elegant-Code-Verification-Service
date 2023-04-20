# ELEGANT Code Verification Service

A web service that employs the [ESBMC](https://github.com/elegant-h2020/esbmc "ESBMC") and [JBMC](https://github.com/elegant-h2020/cbmc "JBMC") verification tools to verify C/C++ and Java code.

> *This product includes software developed by Daniel Kroening,
Edmund Clarke,
Computer Science Department, University of Oxford
Computer Science Department, Carnegie Mellon University*Computer Science Department, Carnegie Mellon University*


## Description

This project aims to build a RESTful API in Java that can submit verification requests on the ESBMC and JBMC tools for C/C++ and Java codes. 

The project uses Jakarta EE 9 and Eclipse GlassFish 6 to implement a portable API for the development, exposure, and accessing of the ELEGANT
Code Verification Webservice.

## Dependencies

- CBMC v5.58.1
- ESBMC v7.0

## Getting the source code

The service expects the source code to be in the following tree:

```
Elegant
├── Elegant-Code-Verification-Service
├── CBMC
├── ESBMC_Project
```

To get the source code in this layout execute:

```bash
mkdir ~/Elegant
git clone git@github.com:elegant-h2020/Elegant-Code-Verification-Service.git ~/Elegant/Elegant-Code-Verification-Service
```

## Dockerized Installation:

```bash
cd ~/Elegant/Elegant-Coder-Verification-Service
```
Create a Docker Volume:
```bash
docker volume create service_files
```
Build the container:
```bash
docker build -t code-verification-service-container .
```
Run the container:
```bash
docker run -it -p 8080:8080 -v service_files:/service/files code-verification-service-container
```

##### Now you can [Utilize the API](UTILIZATION.md) !


## Licenses

[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

## Additional Resources

```bash
https://blogs.oracle.com/javamagazine/post/transition-from-java-ee-to-jakarta-ee

http://www.mastertheboss.com/jboss-frameworks/resteasy/getting-started-with-jakarta-restful-services/
````
