Coral API Server
===

We are designing an API to get access to Coral's core functionality
through standard http queries.  

Design Goals: 
-------------

-   RESTful
    ([http://mvccontrib.codeplex.com/wikipage?title=SimplyRestfulRouting&referringTitle=Documentation&ProjectName=mvccontrib](http://mvccontrib.codeplex.com/wikipage?title=SimplyRestfulRouting&referringTitle=Documentation&ProjectName=mvccontrib))
-   JSON based
-   HTTP based

Requirements:
-------------

We need the API to meet the following requirements:

-   All Communication is Encrypted over SSL
-   Authentication is required (http basic auth should work:
    [http://stackoverflow.com/questions/319530/restful-authentication](http://stackoverflow.com/questions/319530/restful-authentication))
-   Must Provide Distinct URLs for each resource ([See: RESTful Routing](http://mvccontrib.codeplex.com/wikipage?title=SimplyRestfulRouting&referringTitle=Documentation&ProjectName=mvccontrib) )
-   Must support the following functionality (pseudo-code method names that would map to RESTful api calls):

    -   addEquipmentRoleToMember(String member, String roleName, String resource)
    -   removeEquipmentRoleFromMember(String member, String roleName, String resource)
    -   addProjectRoleToMember(String member, String roleName, String resource)
    -   removeProjectRoleFromMember(String member, String roleName, String resource)
    -   addSafetyFlagToMember(String member )
    -   removeSafetyFlagFromMember(String member)
    -   addMemberProjects(String member, String[] projects)
    -   removeMemberProjects(String member, String[] projects)
    -   createNewMember(Member member)
    -   createNewProject(Project project)
    -   addProjectMembers(String project, String[] members)
    -   removeProjectMembers(String project, String[] members)
    -   enable( tool, agent, member, project, account )
    -   disable(tool)
    -   qualify(tool, member, role)
    -   disqualify(tool, member, role)
    -   reserve( tool, agent, member, project, account, begin time, end time(or length) )
    -   deleteReservation( tool, member, time, length )
    -   costRecovery (month, year)          
    -   more, eventually

-   Resources(nouns) in the system:

    -   Member
    -   Tool
    -   Reservation
    -   Enable
    -   Project
    -   Account
    -   EquipmentRole
    -   perhaps more eventually, like Lab, Supply, LabRole, Rate, Rundata, ...

-   Actions(verbs) supported on the resources:
    -   Member
        -   Create (PUT)
        -   Read (or View or Show) (GET)
        -   Update (POST)
        -   Delete (DELETE) (Do we need this one?)
        -   List (GET)

    -   Tool
        -   Read (View or Show) (GET)
        -   List (GET)

    -   Reservation
        -   Create (PUT)
        -   Read (or View or Show) (GET)
        -   Update (POST)
        -   Delete (DELETE)
        -   List (GET)

    -   Enable
        -   Create (PUT)
        -   Read (or View or Show) (GET)
        -   Update (POST)
        -   Delete (DELETE)
        -   List (GET)

    -   Project
        -   Create (PUT)
        -   Read (or View or Show) (GET)
        -   Update (POST)
        -   Delete (DELETE) (Do we need this one?)
        -   List (GET)

    -   Account
        -   Create (PUT)
        -   Read (or View or Show) (GET)
        -   Update (POST)
        -   Delete (DELETE) (Do we need this one?)
        -   List (GET)

    -   EquipmentRole
        -   Create (PUT)
        -   Read (or View or Show) (GET)
        -   Update (POST)
        -   Delete (DELETE)
        -   List (GET)

  -   URLs (examples)
    -   https://server/coral-api/v0.1/member
        -   supporting PUT, GET(list)
    -   https://server/coral-api/v0.1/member?name=ryant
        -   supporting POST, GET
        -   POST and GET would send/receive json representations of member
    -   https://server/coral-api/v0.1/reservation
        -   supporting POST, GET (list of reservations)
    -   https://server/coral-api/v0.1/reservation?id=someID (base64 encoding of DB id?)
        -   supporting POST, GET, DELETE

-   The server should respond with appropriate messages for errors or
    successes (404s, 201s, etc. See
    [http://stackoverflow.com/questions/9345620](http://stackoverflow.com/questions/9345620/if-a-rest-api-method-fails-should-i-return-a-200-400-or-500-http-status-messa) ).
     Errors might be permission denied, or invalid data, or other things
    too.  Permission denied errors should give some explanation of what
    happened.

Implementation
---

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

You may also request a token by authenticating using http basic auth and making a get request to the same path.
Make sure you pay attention to the status code after completion in case auth fails. Example:

    curl -k -u coral:123456 https://localhost:8443/authenticate 
    {"username":"auth-token","password":"sibjcja4ru9u1kbq73ohhtd9pn"}

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

Here are the supported commands so far (as curl commands):

GET commands:

    curl -u auth-token:0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9 -k https://localhost:8443/account?name=Bootstrap%20account
    curl -u auth-token:0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9 -k https://localhost:8443/project?name=Bootstrap%20project
    curl -u auth-token:0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9 -k https://localhost:8443/member?name=coral

POST commands (creating new entities):

    curl -X POST -H "Content-Type: application/json" -d '{"name":"new acct"}' -u auth-token:0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9 -k https://localhost:8443/account -D -
    curl -X POST -H "Content-Type: application/json" -d '{"name":"new proj", "account":"new acct"}' -u auth-token:0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9 -k https://localhost:8443/project -D -
    curl -X POST -H "Content-Type: application/json" -d '{"name":"newmember", "project":"new proj"}' -u auth-token:0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9 -k https://localhost:8443/member -D -

PUT commands (updating entities--should be idempotent):

    curl -X PUT -H "Content-Type: application/json" -d '{"name":"new acct", "description":"test"}' -u auth-token:0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9 -k https://localhost:8443/account -D -
    curl -X PUT -H "Content-Type: application/json" -d '{"name":"new proj", "account": "new acct", "description":"some description"}' -u auth-token:0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9 -k https://localhost:8443/project -D -
    curl -X PUT -H "Content-Type: application/json" -d '{"name":"newmember", "project":"new proj", "firstName": "amy"}' -u auth-token:0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9 -k https://localhost:8443/member -D -

Add member(s) to a project:

    curl -X PUT -H "Content-Type: application/json" -d '{"project":"new proj", "members":["coral"]}' -u auth-token:0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9 -k https://localhost:8443/project-membership -D -

List member(s) on a project:

    curl -H "Content-Type: application/json" -u auth-token:0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9 -k https://localhost:8443/project-membership?project=new%20project -D -
