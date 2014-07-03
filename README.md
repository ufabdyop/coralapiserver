Coral API Server
===============

We are designing an API to get access to Coral's core functionality
through standard HTTP queries.  

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
    -   version
    -   more, eventually

-   Resources(nouns) in the system:

    -   Member
    -   Tool
    -   Reservation
    -   Enable
    -   Project
    -   Projects
    -   Account
    -   EquipmentRole
    -   Version
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

    -   Projects
	-   Create (PUT)
	-   Read (or View or Show) (GET)
	-   Update (POST)
	-   Delete (DELETE)
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
    
    -   Version
    



  -   URLs (examples)
    -   https://server/coral-api/v0.1/member
        -   supporting PUT, GET(list)
    -   https://server/coral-api/v0.1/member?name=ryant
        -   supporting POST, GET
        -   POST and GET would send/receive json representations of member
    -   https://server/coral-api/v0.1/reservation
        -   supporting POST, GET (list of reservations)
    -   https://server/coral-api/v0.1/reservation?id=someID (base64 encoding of DB id?)
    -   https://server/version
        -   supporting POST, GET, DELETE

-   The server should respond with appropriate messages for errors or
    successes (404s, 201s, etc. See
    [http://stackoverflow.com/questions/9345620](http://stackoverflow.com/questions/9345620/if-a-rest-api-method-fails-should-i-return-a-200-400-or-500-http-status-messa) ).
     Errors might be permission denied, or invalid data, or other things
    too.  Permission denied errors should give some explanation of what
    happened.

Implementation
--------------

This is based on the hello world from dropwizard.codahale.com

Installing and Deploying
---
to package: mvn package
to run: java -jar target/coralapiserver-{version}.jar server coral-api.yml

to run without packaging first (which takes a long time): mvn exec:java -Dexec.args="server coral-api.yml"


Communicating with Server
-------------------------

### Authenticate:

You must use tokens in an HTTP Basic Authentication scheme to talk to the server.
The configuration file (coral-api.yml) provides a place for creating permanent tokens
and associating them with users.  You can obtain a temporary token by posting your
username and password to `/authenticate`.  For example:

    curl -k -X POST -H "Content-Type: application/json" -d '{"username":"ryant","password":"pass"}' https://localhost:8443/authenticate
    {"username":"auth-token","password":"cosmouckrklkkcu579hdsbr0l8"}

You may also request a token by authenticating using basic HTTP authentication and making a GET request to the same path `/authenticate`. 

    curl -k -u coral:123456 https://localhost:8443/authenticate 
    {"username":"auth-token","password":"sibjcja4ru9u1kbq73ohhtd9pn"}

The server will response with a JSON object with a username of 'auth-token' and the token as the password. This token can be used to communicate with the server. For example:

    curl -u auth-token:cosmouckrklkkcu579hdsbr0l8 -k https://localhost:8443/member?name=coral
    {"name":"coral","address1":"","address2":"","advisor":"","altFax":"","altOffice":"","altPhone":"","city":"","disability":"","email":"","ethnicity":"","fax":"","firstName":"","lastName":"","mailCode":"","password":"*LK*","phone":"","project":"Bootstrap project","race":"","state":"","type":"","univid":"","url":"","zipcode":"","active":true}

You can create a token for another user if you use a special token in the config file.  The config file must contain an entry
with a token for a user called "proxyAuthenticator".  Once that token is defined, you can generate tokens that can be used
to authenticate as other users.  Obviously, this proxyAuthenticator token must be kept secret from the general public.
You may want to periodically change this token.  Also, best not to share this token with multiple services, instead
create multiple tokens and give one to each service.  Example command for generating a token for someone else (assuming the
proxyAuthenticator token is 12345abdef):

    curl -k -u auth-token:12345abcdef https://localhost:8443/authenticate?proxyFor=johndoe
    {"username":"auth-token","password":"ABDCDEFGHIJKL"}

The response from the server was a token of "ABDCDEFGHIJKL" that can be used to authenticate as johndoe

If authentication fails, you should get a 401 Unauthorized. For more information, please refer to the API docs.


Resources:
----------
[Here][1] is an example of a project with a good set of resources. And [another][2].

### Account 

#### GET requests

To get the details of an already existing account entity:

```
curl -u auth-token:0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9 -k https://localhost:8443/account?name=Bootstrap%20account
```

#### POST requests
To create new account entities:

```
curl -X POST -H "Content-Type: application/json" -d '{"name":"new acct"}' -u auth-token:0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9 -k https://localhost:8443/account -D -
```

#### PUT requests

To update an already existing account:

```
curl -X PUT -H "Content-Type: application/json" -d '{"name":"new acct", "description":"test"}' -u auth-token:0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9 -k https://localhost:8443/account -D -
```

### Project

#### GET requests

To get the details of an already existing Project:

```
curl -u auth-token:0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9 -k https://localhost:8443/project?name=Bootstrap%20project
```

#### POST requests

Create a new Project:

```
curl -X POST -H "Content-Type: application/json" -d '{"name":"new proj", "account":"new acct"}' -u auth-token:0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9 -k https://localhost:8443/project -D -
```

#### PUT requests

To update an existing project:

```
curl -X PUT -H "Content-Type: application/json" -d '{"name":"new proj", "account": "new acct", "description":"some description"}' -u auth-token:0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9 -k https://localhost:8443/project -D -
```

#### Get all active coral projects

To get all of the active projects:

```
curl -u auth-token:0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9 -k https://localhost:8443/projects
```

#### List member(s) on a project

```
curl -H "Content-Type: application/json" -u auth-token:0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9 -k https://localhost:8443/project-membership?project=new%20project -D -
```

### Member

#### GET requests

To get the details of an already existing coral member:

```
curl -u auth-token:0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9 -k https://localhost:8443/member?name=coral
```

#### POST requests

To create a new coral member:

```
curl -X POST -H "Content-Type: application/json" -d '{"name":"newmember", "project":"new proj"}' -u auth-token:0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9 -k https://localhost:8443/member -D -
```

To create a reservation:
```
curl -X POST -H "Content-Type: application/json" -d '{"member":"newmember", "project":"new proj", "account":"new acct", "bdate":"2014-06-12 12:00:00", "edate":"2014-06-12 13:00:00", "item": "some instrument"}' -u auth-token:0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9 -k https://localhost:8443/reservation -D -
```

To enable a tool:
```
curl -X POST -H "Content-Type: application/json" -d '{"member":"newmember", "project":"new proj", "item": "some instrument"}' -u auth-token:0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9 -k https://localhost:8443/enable -D -
```

To disable a tool:
```
curl -X POST -H "Content-Type: application/json" -d '{"member":"newmember", "item": "some instrument"}' -u auth-token:0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9 -k https://localhost:8443/disable -D -
```

#### PUT requests

To update an already existing member:

```
curl -X PUT -H "Content-Type: application/json" -d '{"name":"newmember", "project":"new proj", "firstName": "amy"}' -u auth-token:0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9 -k https://localhost:8443/member -D -
```

#### Add member(s) to a project:

```
curl -X PUT -H "Content-Type: application/json" -d '{"project":"new proj", "members":["coral"]}' -u auth-token:0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9 -k https://localhost:8443/project-membership -D -
```

#### Get projects for member

To get all of the active projects that a member is working on:

```
curl -u auth-token:0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9 -k https://localhost:8443/projects?member=coral
```

    
### API Version:

    curl -k https://localhost:8443/version




[1]:https://github.com/gary-rowe/DropwizardOpenID
[2]:http://cosmo-opticon.net/blog/2013/1/23/session-based-security-in-dropwizard
