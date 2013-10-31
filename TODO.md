* Figure out how to unshade dependencies, and also run without all the packaging time
* Add authentication module
* Wrap all responses in some meta data resource, so exceptions can get parse out, eg:
  {
    response_code: 200, //this should also be in the response header
    errors: []
    data: {
      member: {...}  // or project, or account, etc.
    }
  }
* Add more member resources PUT, POST, DELETE
* Add project resources
