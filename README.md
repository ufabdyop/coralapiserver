This is based on the hello world from dropwizard.codahale.com

Installing and Deploying
---
to package: mvn package
to run: java -jar target/coralapiserver-{version}.jar server coral-api.yml

to run without packaging first (which takes a long time): mvn exec:java -Dexec.args="server coral-api.yml"


To post to the server (for example):
curl -k -X POST -H "Content-Type: application/json" -d '{"username":"xyz","password":"xyz"}' https://localhost:8443/member


Resources:
---
This is a good one:
https://github.com/gary-rowe/DropwizardOpenID (gary-rowe has some sample applications)

so is this:
http://cosmo-opticon.net/blog/2013/1/23/session-based-security-in-dropwizard (sessions)

