#!/bin/sh

# This is a shell script that :
#     1) builds the code verification service source code
#     2) sets a glassfish server up
#     3) instruct the server to deploy the service
# for easiness in dockerized environment.

mvn clean install
$GLASSFISH_HOME/asadmin start-domain domain1
$GLASSFISH_HOME/asadmin deploy target/Elegant-Code-Verification-Service-1.0-SNAPSHOT.war

