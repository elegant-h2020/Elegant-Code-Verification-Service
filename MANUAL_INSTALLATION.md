## Manual Installation:

```bash
cd ~/Elegant
git clone https://github.com/diffblue/cbmc.git ~/Elegant/CBMC
git clone https://github.com/esbmc/esbmc.git ~/Elegant/ESBMC_Project/esbmc
```

Checkout to specific versions of the tools:

- CBMC

	```bash
	cd ~/Elegant/CBMC
	git checkout cbmc-5.58.1
	```

- ESBMC

	```bash
	cd ~/Elegant/ESBMC_Project/esbmc
	git checkout v7.0
	```

### 1. JDK 8

Install OpenJDK 8u222:

```bash
wget --no-check-certificate -O /tmp/openjdk-8u222b10.tar.gz https://github.com/AdoptOpenJDK/openjdk8-upstream-binaries/releases/download/jdk8u222-b10/OpenJDK8U-jdk_x64_linux_8u222b10.tar.gz
tar xzvf /tmp/openjdk-8u222b10.tar.gz --directory /usr/lib/jvm/
mv /usr/lib/jvm/openjdk-8u222-b10 /usr/lib/jvm/java-8-openjdk-amd64
```

NOTE: Also tested with Oracle JDK8u341.

### 2. Glassfish 6.0.0

```bash
cd ~
wget 'https://www.eclipse.org/downloads/download.php?file=/ee4j/glassfish/glassfish-6.0.0.zip' -O glassfish-6.0.0.zip
unzip glassfish-6.0.0.zip
cd ~/glassfish6
echo "AS_JAVA=/usr/lib/jvm/openjdk-8u222-b10" >> ./glassfish/config/asenv.conf
```

### 3. CMake v3.14:

```bash
cd ~
sudo apt remove cmake
sudo apt purge --auto-remove cmake
sudo apt-get install build-essential
wget https://cmake.org/files/v3.14/cmake-3.14.0.tar.gz
tar xf cmake-3.14.0.tar.gz
cd cmake-3.14.0/
./configure
make
sudo make install
```

### 4. CBMC

1. Install the pre-required packages for CBMC:

	```bash
	sudo apt-get install g++ gcc flex bison make git curl patch maven jq
	```

2. Build the CBMC tool (CMake is suggested):

	- Build with CMake:
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

### 5. ESBMC

1. Install the pre-required packages and solvers (Boolector only) for ESBMC:

	```bash
	sudo apt-get install clang-tidy python-is-python3 csmith python3 ccache libcsmith-dev gperf libgmp-dev gcc-multilib linux-libc-dev libboost-all-dev ninja-build python3-setuptools libtinfo-dev pkg-config python3-pip && pip install toml
	cd ~/Elegant/ESBMC_Project
	
	# Boolector 3.2.2
	git clone --depth=1 --branch=3.2.2 https://github.com/boolector/boolector && cd boolector && ./contrib/setup-lingeling.sh && ./contrib/setup-btor2tools.sh && ./configure.sh --prefix $PWD/../boolector-release && cd build && make -j4 && make install
	
	# Download clang11
	RUN wget https://github.com/llvm/llvm-project/releases/download/llvmorg-11.0.0/clang+llvm-11.0.0-x86_64-linux-gnu-ubuntu-20.04.tar.xz  && tar xf clang+llvm-11.0.0-x86_64-linux-gnu-ubuntu-20.04.tar.xz && mv clang+llvm-11.0.0-x86_64-linux-gnu-ubuntu-20.04 clang11 && rm clang+llvm-11.0.0-x86_64-linux-gnu-ubuntu-20.04.tar.xz
	# Setup ibex 2.8.9
	wget http://www.ibex-lib.org/ibex-2.8.9.tgz && tar xvfz ibex-2.8.9.tgz && cd ibex-2.8.9 && ./waf configure --lp-lib=soplex && ./waf install
	```

2. Build the ESBMC tool:

	```bash
	cd ~/Elegant/ESBMC_Project/esbmc
	mkdir build
	cd build
	cmake .. -GNinja -DBUILD_TESTING=On -DENABLE_REGRESSION=On -DClang_DIR=$PWD/../../clang11 -DLLVM_DIR=$PWD/../../clang11 -DBUILD_STATIC=On -DBoolector_DIR=$PWD/../../boolector-release -DCMAKE_INSTALL_PREFIX:PATH=$PWD/../../release
	cmake --build . && ninja install
	```

### 6. Set Environment variables:
```bash
export SERVICE_HOME=~/Elegant/Elegant-Code-Verification-Service
export JAVA_HOME=/usr/lib/jvm/openjdk-8u222-b10
export GLASSFISH_HOME=~/glassfish6/glassfish/bin
```

### 7. Configure the project:

Make sure that `JBMC_BIN` is properly set in `LinuxJBMC.setUpJBMCEnvironment()`in order to let the web service to utilize the JBMC tool:

- CBMC built with CMake:
	- ```environment.put("JBMC_BIN", environment.get("WORKDIR") + "/build/bin/jbmc");```
- CBMC built with Make:
	- ```environment.put("JBMC_BIN", environment.get("WORKDIR") + "/jbmc/src/jbmc/jbmc");```

### 8. Build the service:

Generates the war file in `~/Elegant/Elegant-Code-Verification-Service/target/`.
```bash
cd ~/Elegant/Elegant-Code-Verification-Service
mvn clean install
```

### 9. Set a GlassFish domain up:

```bash
$GLASSFISH_HOME/asadmin start-domain domain1
```

- To stop a GlassFish domain:

	```bash
	$GLASSFISH_HOME/asadmin stop-domain domain1
	```

### 10. Deploy the service:

```bash
$GLASSFISH_HOME/asadmin deploy <path/to/service/war>
```

- for our service:
	```bash
	$GLASSFISH_HOME/asadmin deploy $SERVICE_HOME/target/Elegant-Code-Verification-Service-1.0-SNAPSHOT.war
	```