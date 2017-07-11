Coral API Server
===============

We are designing an API to get access to Coral's core functionality
through standard HTTP queries.  

Design Goals:
-------------

-   RESTful
-   JSON based
-   HTTP based

Requirements:
-------------

We need the API to meet the following requirements:

-   All Communication is Encrypted over SSL
-   Authentication is required (http basic auth should work:
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
    -   https://coral-api-server/v0/members
        -   supporting PUT, GET(list)
    -   https://coral-api-server/v0/members?name=ryant
        -   supporting POST, GET
        -   POST and GET would send/receive json representations of member
    -   https://coral-api-server/v0/reservation
        -   supporting POST, GET (list of reservations)
    -   https://coral-api-server/v0/reservation?id=someID (base64 encoding of DB id?)

-   The server should respond with appropriate status codes and messages for errors or
    successes (404s, 201s, etc.)  Errors might be permission denied, or invalid data,
    or other things too.  Permission denied errors should give some explanation of what
    happened.

Implementation
--------------

This is based on the dropwizard framework: dropwizard.codahale.com

Installing and Deploying
---
to package: mvn package
to run: java -jar target/coralapiserver-{version}.jar server coral-api.yml

to run without packaging first (which takes a long time): mvn exec:java -Dexec.args="server coral-api.yml"


Communicating with Server
-------------------------

### Authenticate:

Most api calls require authentication.  Authentication is done using http basic auth.
You may either send your username and password as credentials, or you can send the
username of 'auth-token' with a special token for the password for the credentials.
Tokens can be set ahead of time by editing the coral-api.yml config file.  Otherwise,
you can obtain a temporary token by posting your username and password to `/authenticate`.
For example:

```
curl -k -u johndoe:myPassword https://localhost/v0/authenticate
```

The server will respond with a JSON object with a username of 'auth-token' and the token as the password.
You can use that auth token for future requests.
```
{"username":"auth-token","password":"sibjcja4ru9u1kbq73ohhtd9pn"}
```

### Proxy Authentication:

If you have a service that needs to be able to authenticate as a proxy user without knowing that user's password,
you can configure a special token that grants the holder of the token the ability to authenticate as any other
user.  The config file must contain an entry with a token for a user called "proxyAuthenticator".  Once that token is
defined, you can generate tokens that can be used to authenticate as other users.  Obviously, this
proxyAuthenticator token must be kept secret from the general public.  You may want to periodically change this token.
Also, best not to share this token with multiple services, instead create multiple tokens and give one to each service.
Example command for generating a token for someone else (assuming the proxyAuthenticator token is 12345abdef):

```
curl -k -u auth-token:12345abcdef https://localhost/v0/authenticate?proxyFor=johndoe
{"username":"auth-token","password":"ABDCDEFGHIJKL"}
```

The response from the server was a token of "ABDCDEFGHIJKL" that can be used to authenticate as johndoe
If authentication fails, you should get a 401 Unauthorized.


Resources:
----------
Here are some examples of using curl to access the functionality of the coral api server.
For the purposes of these examples, the auth string is "auth-token:abcdefg", but any 
valid auth string should work. 

### Accounts

#### GET requests

To get the details of an already existing account:
```
curl -u auth-token:abcdefg -k https://localhost/v0/account?name=Bootstrap%20account
```

#### POST requests

To create new account entities:
```
curl -X POST -H "Content-Type: application/json" -d '{"name":"new acct"}' -u auth-token:abcdefg -k https://localhost/v0/account -D -
```

#### PUT requests

To update an already existing account:
```
curl -X PUT -H "Content-Type: application/json" -d '{"name":"new acct", "description":"test"}' -u auth-token:abcdefg -k https://localhost/v0/account -D -
```

### Project

#### GET requests

To get the details of an already existing Project:
```
curl -u auth-token:abcdefg -k https://localhost/v0/project?name=Bootstrap%20project
```

#### POST requests

Create a new Project:
```
curl -X POST -H "Content-Type: application/json" -d '{"name":"new proj", "account":"new acct"}' -u auth-token:abcdefg -k https://localhost/v0/project -D -
```

#### PUT requests

To update an existing project:
```
curl -X PUT -H "Content-Type: application/json" -d '{"name":"new proj", "account": "new acct", "description":"some description"}' -u auth-token:abcdefg -k https://localhost/v0/project -D -
```

#### Get all active coral projects

To get all of the active projects:
```
curl -u auth-token:abcdefg -k https://localhost/v0/projects
```

#### List member(s) on a project

```
curl -H "Content-Type: application/json" -u auth-token:abcdefg -k https://localhost/v0/projectMemberships?project=new%20project -D -
```

### Member

#### GET requests

To get the details of an already existing coral member:

```
curl -u auth-token:abcdefg -k https://localhost/v0/members?name=coral
```

#### POST requests

To create a new coral member:
```
curl -X POST -H "Content-Type: application/json" -d '{"name":"newmember", "project":"new proj"}' -u auth-token:abcdefg -k https://localhost/v0/members -D -
```

To change a password:
```
curl  -X POST -H "Content-Type: application/json" -d '{"name":"coral", "password":"1234567"}' -k -u auth-token:abcdefg https://localhost/v0/resetPassword
```

To create a reservation:
```
curl -X POST -H "Content-Type: application/json" -d '{"member":"coral", "project":"Bootstrap project", "item": "some instrument", "bdate": "2015-01-29 15:00:00", "lengthInMinutes": 30}' -u coral:password -k https://localhost/v0/reservation

```

To enable a tool:
```
curl -X POST -H "Content-Type: application/json" -d '{"member":"newmember", "project":"new proj", "item": "some instrument"}' -u auth-token:abcdefg -k https://localhost/v0/enables -D -
```

To disable a tool:
```
curl -X POST -H "Content-Type: application/json" -d '{"item": "some instrument"}' -u auth-token:abcdefg -k https://localhost/v0/disables -D -
```

#### PUT requests

To update an already existing member:
```
curl -X PUT -H "Content-Type: application/json" -d '{"name":"newmember", "project":"new proj", "firstName": "amy"}' -u auth-token:abcdefg -k https://localhost/v0/members -D -
```

#### Add member(s) to a project:

```
curl -X PUT -H "Content-Type: application/json" -d '{"project":"new proj", "members":["coral"]}' -u auth-token:abcdefg -k https://localhost/v0/projectMemberships -D -
```

#### Get projects for member

To get all of the active projects that a member is working on:
```
curl -u auth-token:abcdefg -k https://localhost/v0/projects?member=coral
```

### API Version:

```
curl -k https://localhost/v0/version
```

### Check Some Security Settings:

```
curl -k https://localhost/v0/checkKey
```

### Who Am I (useful if authenticating with token

```
curl -k https://localhost/v0/whoami
```

[1]:https://github.com/gary-rowe/DropwizardOpenID
[2]:http://cosmo-opticon.net/blog/2013/1/23/session-based-security-in-dropwizard
[3]:http://stackoverflow.com/questions/319530/restful-authentication
[4]:http://stackoverflow.com/questions/9345620
