# Dockerized Code Verification Service

## Description

The Code Verification Service is dockerized to enhance easiness in deployment by avoiding configuration, environment and version conflicts. However, the service source is not included into the docker image since it lives in private repository. We use `docker-sync` as a workaround to enable the container to co-utilize specified directories synchronously with the host.

##### 1. Clone the service source:

```bash 
cd ~/Elegant
git clone git@github.com:elegant-h2020/Elegant-Code-Verification-Service.git
```

##### 3. Install the required docker-related packages:

```bash
sudo gem install docker-sync
sudo apt install docker-compose
sudo apt install ruby-full
```

Enable synchronization with external resources:

```bash
cd Elegant/Elegant-Code-Verification-Service/docker
docker-sync start
```

Create and build the docker image of the service:

```bash
docker-compose up --no-start
```
- To re-build after changes in Dockerfile:

	```bash
	docker-compose build
	```

Start the container:

```bash
docker start -i code-verification-service-container
```

## In the container bash:

The above steps start the `code-verification-service-container` and open a bash shell in it. From this shell you can build and deploy the code verification service. The workdir of the shell is in `$SERVICE_HOME`.

Note the following environment variables:
```
$SERVICE_HOME=/root/Elegant/Elegant-Code-Verification-Service
$JAVA_HOME=/usr/lib/jvm/openjdk-8u222-b10
$GLASSFISH_HOME=/glassfish6/glassfish/bin
```

#### 1. Build the service:

```bash
mvn clean install
```

#### 2. Set the glassfish server up:

```bash
$GLASSFISH_HOME/asadmin start-domain domain1
```

#### 3. Deploy the service:

```bash
$GLASSFISH_HOME/asadmin deploy target/Elegant-Code-Verification-Service-1.0-SNAPSHOT.war
```

#### 4. Utilize the service:

As in the non-dockerized environment by replacing the `localhost` with `0.0.0.0`.

#### 5. Exit:

To exit the container shell  and stop the container type `Ctrl-D` and then run:
```bash
docker stop code-verification-service-container
```

#### Notes: Useful docker commands:

- delete stopped containers:
```bash
docker rm -v $(docker ps -a -q -f status=exited)
```

- delete images that aren't used:
```bash
docker rmi $(docker images -f "dangling=true" -q)
```