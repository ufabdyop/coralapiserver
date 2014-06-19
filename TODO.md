
* ERROR: if you make a get request for a valid member, using an invalid member token,
  the server returns a 204 empty response.  Should be a 401
* Figure out how to unshade dependencies, and also run without all the packaging time
* Add authentication module
*   Goals: 
   *  make post request to https://server/coralapi/auth with {user: "username", pass: "password"}
      if valid, receive token
        * working, but the URL is wrong.  Need to extract class from MemberResource to AuthResource or something like that
   *  permanent tokens can be configured in a config file such that each token is permanently
      associated with a certain username

* Wrap all responses in some meta data resource, so exceptions can get parsed out, eg:
  {
    response_code: 200, //this should also be in the response header
    errors: []
    data: {
      member: {...}  // or project, or account, etc.
    }
  }
* Add more member resources PUT, POST, DELETE
* Add project resources
* Add lab as a parameter