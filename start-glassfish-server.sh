#!/bin/sh

$GLASSFISH_HOME/asadmin start-domain domain1

$GLASSFISH_HOME/asadmin deploy target/Elegant-Code-Verification-Service-1.0-SNAPSHOT.war

bash