FROM ubuntu:20.04
# LABEL name="test-org/test-img"

ENV DEBIAN_FRONTEND noninteractive

RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    build-essential g++ gcc flex bison make git curl patch maven jq rsync wget unzip \
    && apt-get clean

# Java 8 u222
RUN wget --no-check-certificate -O /tmp/openjdk-8u222b10.tar.gz https://github.com/AdoptOpenJDK/openjdk8-upstream-binaries/releases/download/jdk8u222-b10/OpenJDK8U-jdk_x64_linux_8u222b10.tar.gz
RUN mkdir -p /usr/lib/jvm/ \
    && tar xzvf /tmp/openjdk-8u222b10.tar.gz --directory /usr/lib/jvm/

# Glassfish 6
RUN wget --no-check-certificate 'https://www.eclipse.org/downloads/download.php?file=/ee4j/glassfish/glassfish-6.0.0.zip' -O glassfish-6.0.0.zip \
    && unzip glassfish-6.0.0.zip
RUN echo "AS_JAVA=/usr/lib/jvm/openjdk-8u222-b10" >> glassfish6/glassfish/config/asenv.conf

# install CMake v3.8
RUN pwd
RUN apt remove cmake && apt purge --auto-remove cmake \
    && wget https://cmake.org/files/v3.8/cmake-3.8.0.tar.gz \
    && tar xf cmake-3.8.0.tar.gz
WORKDIR /cmake-3.8.0/
RUN ./configure && make && make install

RUN cmake --version

# define paths
ENV SERVICE_HOME=/root/Elegant/Elegant-Code-Verification-Service
ENV JAVA_HOME=/usr/lib/jvm/openjdk-8u222-b10
ENV GLASSFISH_HOME=/glassfish6/glassfish/bin

EXPOSE 8080

# enable rsync
RUN sed -i 's/RSYNC_ENABLE=false/RSYNC_ENABLE=true/g' /etc/default/rsync
# Setup rsync
ADD ./rsyncd.conf /etc/rsyncd.conf

WORKDIR $SERVICE_HOME

CMD service rsync start && bash