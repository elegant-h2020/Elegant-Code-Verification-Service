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

# for esbmc
RUN apt-get update && apt-get install -y clang-tidy python-is-python3 csmith python3 ccache libcsmith-dev gperf libgmp-dev gcc-multilib linux-libc-dev libboost-all-dev ninja-build python3-setuptools libtinfo-dev pkg-config python3-pip && pip install toml
WORKDIR /root/Elegant/ESBMC_Project
# Boolector 3.2.2
RUN git clone --depth=1 --branch=3.2.2 https://github.com/boolector/boolector && cd boolector && ./contrib/setup-lingeling.sh && ./contrib/setup-btor2tools.sh && ./configure.sh --prefix $PWD/../boolector-release && cd build && make -j4 && make install
# Z3 4.8.9
#RUN wget https://github.com/Z3Prover/z3/releases/download/z3-4.8.9/z3-4.8.9-x64-ubuntu-16.04.zip && unzip z3-4.8.9-x64-ubuntu-16.04.zip && mv z3-4.8.9-x64-ubuntu-16.04/ z3
# Yices 2.6.4
#RUN wget https://gmplib.org/download/gmp/gmp-6.1.2.tar.xz && tar xf gmp-6.1.2.tar.xz && rm gmp-6.1.2.tar.xz && cd gmp-6.1.2 && ./configure --prefix $PWD/../gmp --disable-shared ABI=64 CFLAGS=-fPIC CPPFLAGS=-DPIC && make -j4 && make install
#RUN git clone https://github.com/SRI-CSL/yices2.git && cd yices2 && git checkout Yices-2.6.4 && autoreconf && ./configure --prefix /root/Elegant/ESBMC_Project/yices  --with-static-gmp=$PWD/../gmp/lib/libgmp.a && make -j4 && make static-lib && make install && cp ./build/x86_64-pc-linux-gnu-release/static_lib/libyices.a ../yices/lib
# CVC4 (b826fc8ae95fc)
#RUN apt-get install -y openjdk-11-jdk
#RUN git clone https://github.com/CVC4/CVC4.git && cd CVC4 && git reset --hard b826fc8ae95fc && ./contrib/get-antlr-3.4 && ./configure.sh --optimized --prefix=../cvc4 --static --no-static-binary && cd build && make -j4 && make install
# Bitwuzla (smtcomp-2021)
#RUN git clone --depth=1 --branch=smtcomp-2021 https://github.com/bitwuzla/bitwuzla.git && cd bitwuzla && ./contrib/setup-lingeling.sh && ./contrib/setup-btor2tools.sh && ./contrib/setup-symfpu.sh && ./configure.sh --prefix $PWD/../bitwuzla-release && cd build && cmake -DGMP_INCLUDE_DIR=$PWD/../../gmp/include -DGMP_LIBRARIES=$PWD/../../gmp/lib/libgmp.a -DONLY_LINGELING=ON ../ && make -j8 && make install
# Download clang11
RUN wget https://github.com/llvm/llvm-project/releases/download/llvmorg-11.0.0/clang+llvm-11.0.0-x86_64-linux-gnu-ubuntu-20.04.tar.xz  && tar xf clang+llvm-11.0.0-x86_64-linux-gnu-ubuntu-20.04.tar.xz && mv clang+llvm-11.0.0-x86_64-linux-gnu-ubuntu-20.04 clang11 && rm clang+llvm-11.0.0-x86_64-linux-gnu-ubuntu-20.04.tar.xz
# Setup ibex 2.8.9
RUN wget http://www.ibex-lib.org/ibex-2.8.9.tgz && tar xvfz ibex-2.8.9.tgz && cd ibex-2.8.9 && ./waf configure --lp-lib=soplex && ./waf install

# clone the source code of the verification tools
RUN git clone https://github.com/diffblue/cbmc.git /root/Elegant/CBMC               && cd /root/Elegant/CBMC                && git checkout cbmc-5.58.1
RUN git clone https://github.com/esbmc/esbmc.git /root/Elegant/ESBMC_Project/esbmc  && cd /root/Elegant/ESBMC_Project/esbmc && git checkout v7.0

# build CBMC
WORKDIR /root/Elegant/CBMC
RUN git submodule update --init \
    && cmake -S . -Bbuild \
    && mkdir -p build/minisat2-download/minisat2-download-prefix/src/ \
    && wget http://ftp.debian.org/debian/pool/main/m/minisat2/minisat2_2.2.1.orig.tar.gz -O build/minisat2-download/minisat2-download-prefix/src/minisat2_2.2.1.orig.tar.gz \
    && cmake --build build

# build ESBMC
#WORKDIR

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