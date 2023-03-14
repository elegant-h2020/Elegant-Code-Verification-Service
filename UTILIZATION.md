## Utilize the API

1. Start and initialize the service

	```bash
	curl http://localhost:8080/Elegant-Code-Verification-Service-1.0-SNAPSHOT/api/verification
	```

2. Register a new  verification request:

	###### The JBMC request JSON format:
	```bash
	{
		"tool": "JBMC",
		"className": "path.to.main",
		"isMethod": true | false
		"methodName": "fully.qualified.name:(arg types)return type"
	}
	```

	###### The ESBMC request JSON format:
	```bash
	{
		"tool": "ESBMC",
		"fileName": "relative/path/to/c-or-cpp-file"
	}
	```

	###### Using the `curl`:

	```bash
	curl -X POST -H "Content-Type: multipart/form-data" -F "file=@/path/to/code/file" -F "request=@/path/to/request/json/file" http://localhost:8080/Elegant-Code-Verification-Service-1.0-SNAPSHOT/api/verification/newEntry
	```

	###### Examples:

	The example code files are under `examples/codes/`.
	The example request files are under `examples/requests/`
	
	1. Verify the whole class (`my/petty/examples/Simple.java`) with JBMC :
	
	```bash
	curl -X POST -H "Content-Type: multipart/form-data" -F "file=@examples/codes/java/my/petty/examples/Simple.class" -F "request=@examples/requests/jbmc/request-class.json" http://localhost:8080/Elegant-Code-Verification-Service-1.0-SNAPSHOT/api/verification/newEntry
	```
	
	2. Verify the `void foo()` method with JBMC:
	
	```bash
	curl -X POST -H "Content-Type: multipart/form-data" -F "file=@examples/codes/java/my/petty/examples/Simple.class" -F "request=@examples/requests/jbmc/request-method-1.json"  http://localhost:8080/Elegant-Code-Verification-Service-1.0-SNAPSHOT/api/verification/newEntry
	```
	
	3. Verify the `boolean foo(String)` method with JBMC:
	
	```bash
	curl -X POST -H "Content-Type: multipart/form-data" -F "file=@examples/codes/java/my/petty/examples/Simple.class" -F "request=@examples/requests/jbmc/request-method-2.json" http://localhost:8080/Elegant-Code-Verification-Service-1.0-SNAPSHOT/api/verification/newEntry
	```

	4. Verify the `examples/codes/c/ex1.c` with ESBMC:
	
	```bash
	curl -X POST -H "Content-Type: multipart/form-data" -F "file=@examples/codes/c/ex1.c" -F "request=@examples/requests/esbmc/request-1.json" http://localhost:8080/Elegant-Code-Verification-Service-1.0-SNAPSHOT/api/verification/newEntry
	```

	5. Verify the `examples/codes/c/ex2.c` with ESBMC:
	
	```bash
	curl -X POST -H "Content-Type: multipart/form-data" -F "file=@examples/codes/c/ex2.c" -F "request=@examples/requests/esbmc/request-2.json" http://localhost:8080/Elegant-Code-Verification-Service-1.0-SNAPSHOT/api/verification/newEntry
	```

	6. Verify the `examples/codes/c/ex3.c` with ESBMC:
	
	```bash
	curl -X POST -H "Content-Type: multipart/form-data" -F "file=@examples/codes/c/ex3.c" -F "request=@examples/requests/esbmc/request-3.json" http://localhost:8080/Elegant-Code-Verification-Service-1.0-SNAPSHOT/api/verification/newEntry
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