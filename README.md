# ELEGANT Code Verification Service

A web service that ...

## Description

This project aims to build a RESTful API in Java that can submit requests for ...

The project uses Jakarta EE 9 and Eclipse GlassFish 6 to implement a portable API for the development, exposure, and accessing of the ELEGANT
Code Verification Webservice.

## Requirements

### 1. CBMC

1. Install the pre-required packages of CBMC:

	```bash
	sudo apt-get install g++ gcc flex bison make git curl patch maven jq
	```

2. Clone the code:
	```bash
	mkdir ~/Elegant
	git clone git@github.com:elegant-h2020/cbmc.git ~/Elegant/CBMC
	cd ~/Elegant/CBMC
	git checkout cbmc-5.58.1
	```

3. Build the tool (CMake is suggested):

	- Build with CMake:

		1. Install CMake v3.2:
		
			```bash
			cd ~
			sudo apt remove cmake
			sudo apt purge --auto-remove cmake
			sudo apt-get install build-essential
			wget https://cmake.org/files/v3.2/cmake-3.2.0.tar.gz
			tar xf cmake-3.2.0.tar.gz
			cd cmake-3.2.0/
			./configure
			make
			sudo make install
			```

		2. Build CBMC:
			```bash
			cd ~/Elegant/CBMC
			git submodule update --init
			cmake -S . -Bbuild
			mkdir -p build/minisat2-download/minisat2-download-prefix/src/
			wget http://ftp.debian.org/debian/pool/main/m/minisat2/minisat2_2.2.1.orig.tar.gz -O build/minisat2-download/minisat2-download-prefix/src/minisat2_2.2.1.orig.tar.gz
			cmake --build build
			```

	-  Build with Make:

		```bash
		cd ~/Elegant/CBMC
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

### 2. Configure the project:

Make sure that `JBMC_BIN` is properly set in `LinuxJBMC.setUpJBMCEnvironment()`in order to let the web service to utilize the JBMC tool:

- CBMC built with CMake:
	- ```environment.put("JBMC_BIN", environment.get("WORKDIR") + "/build/bin/jbmc");```
- CBMC built with Make:
	- ```environment.put("JBMC_BIN", environment.get("WORKDIR") + "/jbmc/src/jbmc/jbmc");```

### 3. Build the service:

```bash
cd ~/Elegant/Elegant-Code-Verification-Service
mvn clean install
```

### 4. Deploy a GlassFish server

1. Download GlassFish 6.0.0:

	```bash
	cd ~
	wget 'https://www.eclipse.org/downloads/download.php?file=/ee4j/glassfish/glassfish-6.0.0.zip' -O glassfish-6.0.0.zip
	unzip glassfish-6.0.0.zip
	cd glassfish-6.0.0
	echo "AS_JAVA=/usr/lib/jvm/openjdk-8u222-b10" >> ./glassfish/config/asenv.conf
	```

2. Start GlassFish local server:

	```bash
	./bin/asadmin start-domain domain1
	```

### 5. Utilize the API

1. Start and initialize the service

	```bash
	curl http://localhost:8080/Elegant-Code-Verification-Service-1.0-SNAPSHOT/api/verification
	```

2. Register a new entry for verification:

	###### The JSON input data format:	The JSON input data format:
	```bash
	{
		"className": "path.to.main",
		"isMethod": true | false
		"methodName": "fully.qualified.name:(arg types)return type"
	}
	```

	###### Using the `curl`:

	```bash
	curl --header "Content-Type: application/json" --request POST  --data '{"className": "<path.to.main>", "isMethod": true | false, "methodName": "fully.qualified.name:(arg types)return type"}' http://localhost:8080/Elegant-Code-Verification-Service-1.0-SNAPSHOT/api/verification/newEntry
	```

	###### Examples:
	
	1. Verify the whole class (`test-cases/my/petty/examples/Simple.java`) :
	
	```bash
	curl --header "Content-Type: application/json" --request POST  --data '{"className": "my.petty.examples.Simple"}' http://localhost:8080/Elegant-Code-Verification-Service-1.0-SNAPSHOT/api/verification/newEntry
	```
	
	2. Verify the `void foo()` method:
	
	```bash
	curl --header "Content-Type: application/json" --request POST  --data '{"className":"my.petty.examples.Simple", "isMethod":true, "methodName":"my.petty.examples.Simple.foo:()V"}'  http://localhost:8080/Elegant-Code-Verification-Service-1.0-SNAPSHOT/api/verification/newEntry
	```
	
	3. Verify the `boolean foo(String)` method:
	
	```bash
	curl --header "Content-Type: application/json" --request POST  --data '{"className":"my.petty.examples.Simple", "isMethod":true, "methodName":"my.petty.examples.Simple.foo:(Ljava/lang/String;)Z"}'  http://localhost:8080/Elegant-Code-Verification-Service-1.0-SNAPSHOT/api/verification/newEntry
	```

3. Get the verification outcome of an entry:

	```bash
	curl http://localhost:8080/Elegant-Code-Verification-Service-1.0-SNAPSHOT/api/verification/getEntry?entryId=<ID>
	```

4. Remove an entry:

	```bash
	curl --request DELETE http://localhost:8080/Elegant-Code-Verification-Service-1.0-SNAPSHOT/api/verification/removeEntry?entryId=<ID>
	```

5. List all known verification entries:

```bash
curl http://localhost:8080/Elegant-Code-Verification-Service-1.0-SNAPSHOT/api/verification/getEntries
```

### 6. Stop the GlassFish server

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
