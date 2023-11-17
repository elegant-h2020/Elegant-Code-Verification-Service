## Utilize the API

1. Start and initialize the service

	```bash
	curl http://0.0.0.0:8080/Elegant-Code-Verification-Service-1.0-SNAPSHOT/api/verification
	```
 
> Notice that `0.0.0.0` is used as the service is running in a Docker container.

2. Register a new  verification request:

	###### The JBMC request JSON format:
	```bash
	{
		"tool": "JBMC",
		"isJarFile" : true | false,
		"jarName" : "file.jar", # Name of the jar file that is uploaded
		"className": "package.ClassName", # Path of the class that contains the main
		"isMethod": true | false
		"methodName": "fully.qualified.name:(arg types)return type"
	}
	```

	###### The ESBMC request JSON format:
	```bash
	{
		"tool": "ESBMC",
		"fileName": "relative/path/to/c-or-cpp-file"
		"isFunction": true | false
		"functionName:" "name"
	}
	```

	###### Using the `curl`:

	```bash
	curl -X POST -H "Content-Type: multipart/form-data" -F "file=@/path/to/code/file" -F "request=@/path/to/request/json/file" http://localhost:8080/Elegant-Code-Verification-Service-1.0-SNAPSHOT/api/verification/newEntry
	```

	###### Examples:

	The example code files are under `examples/codes/`.
	The example request files are under `examples/requests/`
	
	1. Upload a class file (`my/petty/examples/Simple.class`) and verify the whole class with JBMC :
	
	```bash
	curl -X POST -H "Content-Type: multipart/form-data" -F "file=@examples/codes/java/my/petty/examples/Simple.class" -F "request=@examples/requests/jbmc/request-class.json" http://localhost:8080/Elegant-Code-Verification-Service-1.0-SNAPSHOT/api/verification/newEntry
	```
	
	2. Upload a class file (`my/petty/examples/Simple.class`) and verify the `void foo()` method with JBMC:
	
	```bash
	curl -X POST -H "Content-Type: multipart/form-data" -F "file=@examples/codes/java/my/petty/examples/Simple.class" -F "request=@examples/requests/jbmc/request-method-1.json"  http://localhost:8080/Elegant-Code-Verification-Service-1.0-SNAPSHOT/api/verification/newEntry
	```
	
	3. Upload a class file (`my/petty/examples/Simple.class`) and verify the `boolean foo(String)` method with JBMC:
	
	```bash
	curl -X POST -H "Content-Type: multipart/form-data" -F "file=@examples/codes/java/my/petty/examples/Simple.class" -F "request=@examples/requests/jbmc/request-method-2.json" http://localhost:8080/Elegant-Code-Verification-Service-1.0-SNAPSHOT/api/verification/newEntry
	```

    4. Upload a jar file (`my/petty/examples/simple.jar`) and verify the `boolean foo(String)` method from a class that is in a jar file with JBMC:

    ```bash
    cd examples/codes/java
    javac my/petty/examples/Simple.java
    jar -cvf simple.jar my/petty/examples
    cd ../../../
    curl -X POST -H "Content-Type: multipart/form-data" -F "file=@examples/codes/java/simple.jar" -F "request=@examples/requests/jbmc/request-method-3.json" http://localhost:8080/Elegant-Code-Verification-Service-1.0-SNAPSHOT/api/verification/newEntry
    ```

    4.1 Upload a jar file (`radioProfilerCluster.jar`) and verify the whole class described in the `request-class-radio-profiler.json` with JBMC:
    ```bash
    cd examples/codes/java/
    export SERVICE_HOME=~/Elegant/Elegant-Code-Verification-Service
    javac -classpath $SERVICE_HOME/nebulastream/nebulastream-java-client-0.0.83.jar stream/nebula/examples/radioProfilerCluster.java 
    jar -cvf radioProfilerCluster.jar stream/nebula/examples
    curl -X POST -H "Content-Type: multipart/form-data" -F "file=@examples/codes/java/radioProfilerCluster.jar" -F "request=@examples/requests/jbmc/request-class-radio-profiler-cluster.json" http://localhost:8080/Elegant-Code-Verification-Service-1.0-SNAPSHOT/api/verification/newEntry
    ```

	5. Verify the `examples/codes/c/ex1.c` with ESBMC:
	
	```bash
	curl -X POST -H "Content-Type: multipart/form-data" -F "file=@examples/codes/c/ex1.c" -F "request=@examples/requests/esbmc/request-1.json" http://localhost:8080/Elegant-Code-Verification-Service-1.0-SNAPSHOT/api/verification/newEntry
	```

	6. Verify the `examples/codes/c/ex2.c` with ESBMC:
	
	```bash
	curl -X POST -H "Content-Type: multipart/form-data" -F "file=@examples/codes/c/ex2.c" -F "request=@examples/requests/esbmc/request-2.json" http://localhost:8080/Elegant-Code-Verification-Service-1.0-SNAPSHOT/api/verification/newEntry
	```

	7. Verify the `examples/codes/c/ex3.c` with ESBMC:
	
	```bash
	curl -X POST -H "Content-Type: multipart/form-data" -F "file=@examples/codes/c/ex3.c" -F "request=@examples/requests/esbmc/request-3.json" http://localhost:8080/Elegant-Code-Verification-Service-1.0-SNAPSHOT/api/verification/newEntry
	```

3. Get the verification outcome of an entry:

	```bash
	curl http://0.0.0.0:8080/Elegant-Code-Verification-Service-1.0-SNAPSHOT/api/verification/getEntry?entryId=<ID>
	```

4. Remove an entry:

	```bash
	curl --request DELETE http://0.0.0.0:8080/Elegant-Code-Verification-Service-1.0-SNAPSHOT/api/verification/removeEntry?entryId=<ID>
	```

5. List all known verification entries:

```bash
curl http://0.0.0.0:8080/Elegant-Code-Verification-Service-1.0-SNAPSHOT/api/verification/getEntries
```

###### Tested Use Cases:

1. NES Example (examples/codes/c/NES-example.cpp):

```bash
curl -X POST -H "Content-Type: multipart/form-data" -F "file=@examples/codes/c/NES-example.cpp" -F "request=@examples/requests/esbmc/NES-request.json" http://localhost:8080/Elegant-Code-Verification-Service-1.0-SNAPSHOT/api/verification/newEntry
```
