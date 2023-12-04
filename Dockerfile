FROM ubuntu:20.04
# LABEL name="test-org/test-img"

ENV DEBIAN_FRONTEND noninteractive

RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    build-essential tree g++ gcc flex bison make git curl patch maven jq rsync wget unzip \
    && apt-get clean

# Java 8 u222
RUN wget --no-check-certificate -O /tmp/openjdk-8u222b10.tar.gz https://github.com/AdoptOpenJDK/openjdk8-upstream-binaries/releases/download/jdk8u222-b10/OpenJDK8U-jdk_x64_linux_8u222b10.tar.gz
RUN mkdir -p /usr/lib/jvm/ \
    && tar xzvf /tmp/openjdk-8u222b10.tar.gz --directory /usr/lib/jvm/

# Glassfish 6
RUN wget --no-check-certificate 'https://www.eclipse.org/downloads/download.php?file=/ee4j/glassfish/glassfish-6.0.0.zip' -O glassfish-6.0.0.zip \
    && unzip glassfish-6.0.0.zip
RUN echo "AS_JAVA=/usr/lib/jvm/openjdk-8u222-b10" >> glassfish6/glassfish/config/asenv.conf

# install CMake v3.14
RUN pwd
RUN apt remove cmake && apt purge --auto-remove cmake \
    && wget https://cmake.org/files/v3.14/cmake-3.14.0.tar.gz \
    && tar xf cmake-3.14.0.tar.gz
WORKDIR /cmake-3.14.0/
RUN ./configure && make && make install

RUN cmake --version

# clone the source code of CBMC
RUN git clone https://github.com/diffblue/cbmc.git /root/Elegant/CBMC               && cd /root/Elegant/CBMC                && git checkout cbmc-5.58.1

# build CBMC
WORKDIR /root/Elegant/CBMC
RUN git submodule update --init \
    && cmake -S . -Bbuild \
    && mkdir -p build/minisat2-download/minisat2-download-prefix/src/ \
    && wget http://ftp.debian.org/debian/pool/main/m/minisat2/minisat2_2.2.1.orig.tar.gz -O build/minisat2-download/minisat2-download-prefix/src/minisat2_2.2.1.orig.tar.gz \
    && cmake --build build

# set up ESBMC
WORKDIR /root/Elegant
RUN wget https://github.com/esbmc/esbmc/releases/download/v7.3/ESBMC-Linux.zip 

RUN unzip ESBMC-Linux.zip -d ESBMC-Linux/ \
    && chmod +x ESBMC-Linux/bin/esbmc

# define paths
ENV SERVICE_HOME=/root/Elegant/Elegant-Code-Verification-Service
ENV JAVA_HOME=/usr/lib/jvm/openjdk-8u222-b10
ENV GLASSFISH_HOME=/glassfish6/glassfish/bin

# bring service files from host into container
COPY . /root/Elegant/Elegant-Code-Verification-Service

# a directory to store the files produced by the service
VOLUME /service/files

# working directory
WORKDIR $SERVICE_HOME

# build the sevice
RUN mvn clean install

EXPOSE 8080

ENTRYPOINT ["./start-glassfish-server.sh"]
