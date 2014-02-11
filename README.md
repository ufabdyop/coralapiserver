This is based on the hello world from dropwizard.codahale.com

Installing and Deploying
---
to package: mvn package
to run: java -jar target/coralapiserver-{version}.jar server coral-api.yml

to run without packaging first (which takes a long time): mvn exec:java -Dexec.args="server coral-api.yml"


Communicating with Server
---

#### Authenticate:

You must use tokens in an HTTP Basic Authentication scheme to talk to the server.
The configuration file (coral-api.yml) provides a place for creating permanent tokens
and associating them with users.  You can obtain a temporary token by posting your
username and password to /authenticate.  Here is an example of getting a temporary
token:

    curl -k -X POST -H "Content-Type: application/json" -d '{"username":"ryant","password":"mypassword"}' https://localhost:8443/authenticate
    {"username":"auth-token","password":"cosmouckrklkkcu579hdsbr0l8"}

The server will response with a json object with a username of auth-token and the token as the password. To use the token to 
communicate with the server, "auth-token" should be the username and the token should be the password.  Here is an example:

    curl -u auth-token:cosmouckrklkkcu579hdsbr0l8 -k https://localhost:8443/member?name=coral
    {"name":"coral","address1":"","address2":"","advisor":"","altFax":"","altOffice":"","altPhone":"","city":"","disability":"","email":"","ethnicity":"","fax":"","firstName":"","lastName":"","mailCode":"","password":"*LK*","phone":"","project":"Bootstrap project","race":"","state":"","type":"","univid":"","url":"","zipcode":"","active":true}

You should get a 401 Unauthorized if you fail to authenticate properly


Resources:
---
This is a good one:
https://github.com/gary-rowe/DropwizardOpenID (gary-rowe has some sample applications)

so is this:
http://cosmo-opticon.net/blog/2013/1/23/session-based-security-in-dropwizard (sessions)

