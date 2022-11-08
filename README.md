# ELEGANT Code Verification Service

A web service that ...

## Description

This project aims to build a RESTful API in Java that can submit requests for ...

The project uses Jakarta EE 9 and Eclipse GlassFish 6 to implement a portable API for the development, exposure, and accessing of the ELEGANT
Code Verification Webservice.

## Requirements

### 1. CBMC

```bash
sudo apt-get install g++ gcc flex bison make git curl patch maven jq
mkdir ~/Elegant
git clone git@github.com:elegant-h2020/cbmc.git Elegant/CBMC
cd ~/Elegant/CBMC
git checkout cbmc-5.58.1

make -C src minisat2-download
make -C src

make -C jbmc/src setup-submodules
make -C jbmc/src
```

### 2. JDK 8

Install OpenJDK 8u222:

```bash
wget --no-check-certificate -O /tmp/openjdk-8u222b10.tar.gz https://github.com/AdoptOpenJDK/openjdk8-upstream-binaries/releases/download/jdk8u222-b10/OpenJDK8U-jdk_x64_linux_8u222b10.tar.gz
tar xzvf /tmp/openjdk-8u222b10.tar.gz --directory /usr/lib/jvm/
mv /usr/lib/jvm/openjdk-8u222-b10 /usr/lib/jvm/java-8-openjdk-amd64
```

NOTE: Also tested with Oracle JDK8u341.

## Installation

### 1. Clone the project:

```bash 
cd ~/Elegant
git clone git@github.com:elegant-h2020/Elegant-Code-Verification-Service.git
```

### 2. Build the service:

```bash
cd ~/Elegant/Elegant-Code-Verification-Service
mvn clean install
```

### 3. Deploy a GlassFish server

a) Download GlassFish 6.0.0:

```bash
cd ~
wget 'https://www.eclipse.org/downloads/download.php?file=/ee4j/glassfish/glassfish-6.0.0.zip' -O glassfish-6.0.0.zip
unzip glassfish-6.0.0.zip
cd glassfish-6.0.0
echo "AS_JAVA=/usr/lib/jvm/openjdk-8u222-b10" >> ./glassfish/config/asenv.conf
```

b) Start GlassFish local server:

```bash
./bin/asadmin start-domain domain1
```

### 4. Utilize the API:

a) Start and initialize the service

```bash
curl http://localhost:8080/Elegant-Code-Verification-Service-1.0-SNAPSHOT/api/verification
```

b) Register a new entry for verification:

```bash
curl --header "Content-Type: application/json" --request POST  --data '{"classname":"<path.to.main>"}' http://localhost:8080/Elegant-Code-Verification-Service-1.0-SNAPSHOT/api/verification/newEntry
````

e.g.: to verify the example code (`test-cases/my/petty/examples/Simple.java`)

```bash
curl --header "Content-Type: application/json" --request POST  --data '{"classname":"my.petty.examples.Simple"}' http://localhost:8080/Elegant-Code-Verification-Service-1.0-SNAPSHOT/api/verification/newEntry
```

c) Get the verification outcome of an entry:

```bash
curl http://localhost:8080/Elegant-Code-Verification-Service-1.0-SNAPSHOT/api/verification/getEntry?entryId=<ID>
```

c) Remove an entry:

```bash
curl --request DELETE http://localhost:8080/Elegant-Code-Verification-Service-1.0-SNAPSHOT/api/verification/removeEntry?entryId=<ID>
```

d) List all known verification entries:

```bash
curl http://localhost:8080/Elegant-Code-Verification-Service-1.0-SNAPSHOT/api/verification/getEntries
```

### 3. Stop the GlassFish server

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
