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

Sample Commands Via Curl:
---

Here is an excerpt of my history of some commands to use:

  519  curl -u auth-token:0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9 -k https://localhost:8443/member?name=coral
  520  curl -u auth-token:0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9 -k https://localhost:8443/?name=coral
  521  curl -u auth-token:0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9 -k https://localhost:8443/project?name=coral
  522  curl -u auth-token:0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9 -k https://localhost:8443/project?name=Bootstrap%20project
  525  curl -u auth-token:0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9 -k https://localhost:8443/project?name=Bootstrap%20project
  526  curl -X POST -H "Content-Type: application/json" -d '{"name":"new project"}' -u auth-token:0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9 -k https://localhost:8443/project
  527  curl -X POST -H "Content-Type: application/json" -d '{"name":"new project"}' -u auth-token:0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9 -k https://localhost:8443/project -D -
  528  curl -X POST -H "Content-Type: application/json" -d '{"name":"new project", "account":"new account"}' -u auth-token:0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9 -k https://localhost:8443/project -D -
  529  curl -X POST -H "Content-Type: application/json" -d '{"name":"newmember", "account":"new account"}' -u auth-token:0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9 -k https://localhost:8443/member -D -
  530  curl -X POST -H "Content-Type: application/json" -d '{"name":"newmember", "project":"newproject"}' -u auth-token:0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9 -k https://localhost:8443/member -D -
  531  curl -X POST -H "Content-Type: application/json" -d '{"name":"newmember", "project":"new project"}' -u auth-token:0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9 -k https://localhost:8443/member -D -
  532  curl -X POST -H "Content-Type: application/json" -d '{"name":"newmer2", "project":"new project"}' -u auth-token:0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9 -k https://localhost:8443/member -D -
  533  curl -X POST -H "Content-Type: application/json" -d '{"name":"newmer2", "project":"new project", "firstName": "lemon"}' -u auth-token:0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9 -k https://localhost:8443/member -D -
  534  curl -X PUT -H "Content-Type: application/json" -d '{"name":"newmer2", "project":"new project", "firstName": "lemon"}' -u auth-token:0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9 -k https://localhost:8443/member -D -
  535  curl -X POST -H "Content-Type: application/json" -d '{"name":"new acct"}' -u auth-token:0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9 -k https://localhost:8443/account -D -
  536  curl -X PUT -H "Content-Type: application/json" -d '{"name":"new acct"}' -u auth-token:0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9 -k https://localhost:8443/account -D -
  539  curl -X PUT -H "Content-Type: application/json" -d '{"name":"new acct"}' -u auth-token:0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9 -k https://localhost:8443/account -D -
  540  curl -X PUT -H "Content-Type: application/json" -d '{"name":"new acct", "description":"test"}' -u auth-token:0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9 -k https://localhost:8443/account -D -
  541  curl -X PUT -H "Content-Type: application/json" -d '{"name":"newmer2", "project":"new project", "firstName": "lemon"}' -u auth-token:0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9 -k https://localhost:8443/member -D -
  542  curl -X PUT -H "Content-Type: application/json" -d '{"name":"newmer2", "project":"new project", "firstName": "lefsn"}' -u auth-token:0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9 -k https://localhost:8443/member -D -
  543  curl -X POST -H "Content-Type: application/json" -d '{"name":"new project", "account":"new account"}' -u auth-token:0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9 -k https://localhost:8443/project -D -
  544  curl -X GET -H "Content-Type: application/json" -d '{"name":"new project", "account":"new account"}' -u auth-token:0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9 -k https://localhost:8443/project -D -
  545  curl -X GET -H "Content-Type: application/json" -d '{"name":"new project", "account":"new account"}' -u auth-token:0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9 -k https://localhost:8443/project?name=new%20project -D -
  546  curl -X PUT -H "Content-Type: application/json" -d '{"name":"new project", "description":"some", "account":"new account"}' -u auth-token:0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9 -k https://localhost:8443/project -D -
  547  curl -X GET -H "Content-Type: application/json" -d '{"name":"new project", "account":"new account"}' -u auth-token:0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9 -k https://localhost:8443/project?name=new%20project -D -
  549  curl -X PUT -H "Content-Type: application/json" -d '{"name":"new project", "description":"some", "account":"new account"}' -u auth-token:0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9 -k https://localhost:8443/project-membership -D -
  550  curl -X PUT -H "Content-Type: application/json" -d '{"project":"new project", "members":["coral"]}' -u auth-token:0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9 -k https://localhost:8443/project-membership -D -
  552  curl -X PUT -H "Content-Type: application/json" -d '{"project":"new project", "members":["coral"]}' -u auth-token:0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9 -k https://localhost:8443/project-membership -D -
  553  curl -X GET -H "Content-Type: application/json" -u auth-token:0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9 -k https://localhost:8443/project-membership?project=new%20project -D -
