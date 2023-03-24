# ELEGANT Code Verification Service

A web service that ...

## Description

This project aims to build a RESTful API in Java that can submit requests for ...

The project uses Jakarta EE 9 and Eclipse GlassFish 6 to implement a portable API for the development, exposure, and accessing of the ELEGANT
Code Verification Webservice.

## Dependencies

- CBMC v5.58.1
- ESBMC v....

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
NOTE: Use `0.0.0.0` instead of `localhost`!!


## Licenses

(TODO)

## Additional Resources

```bash
https://blogs.oracle.com/javamagazine/post/transition-from-java-ee-to-jakarta-ee

http://www.mastertheboss.com/jboss-frameworks/resteasy/getting-started-with-jakarta-restful-services/
````
