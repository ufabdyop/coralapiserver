"use strict";

var CoralAPI = {};

CoralAPI.makeClient = function() {
  var coralAPIClient = {};

  coralAPIClient.getVersion = function(coralApiUrl, successCallback, errorCallback) {
      var request = new XMLHttpRequest();
      request.open("GET", coralApiUrl + '/version', true);
      request.onreadystatechange = function() {
            if (request.readyState == 4) {
              if (request.status === 200) {
                successCallback(JSON.parse(request.responseText));
              } else {
                errorCallback(request);
              }
            }
      }
      request.send(null);
  };

  coralAPIClient.authenticate = function(coralApiUrl, username, password, successCallback, errorCallback) {
    var request = new XMLHttpRequest();
    request.open("GET", coralApiUrl + '/authenticate', true);
    request.setRequestHeader("Accept", "application/json");
    request.setRequestHeader("Content-Type", "application/json");
    request.setRequestHeader("Authorization", "Basic " + btoa(username + ":" + password));

    request.onreadystatechange = function() {
          if (request.readyState == 4) {
            if (request.status === 200) {
                successCallback(JSON.parse(request.responseText));
            } else {
                errorCallback(request);
            }
          }
    }
    request.send(null);
  };

  coralAPIClient.whoami = function(coralApiUrl, username, password, successCallback, errorCallback) {
    var request = new XMLHttpRequest();
    request.open("GET", coralApiUrl + '/whoami', true);
    request.setRequestHeader("Accept", "application/json");
    request.setRequestHeader("Content-Type", "application/json");
    request.setRequestHeader("Authorization", "Basic " + btoa(username + ":" + password));

    request.onreadystatechange = function() {
          if (request.readyState == 4) {
            if (request.status === 200) {
                successCallback(JSON.parse(request.responseText));
            } else {
                errorCallback(request);
            }
          }
    }
    request.send(null);
  };

  coralAPIClient.resetPassword = function(coralApiUrl, username, password, targetUsername, newPassword, successCallback, errorCallback) {
    var request = new XMLHttpRequest();
    request.open("POST", coralApiUrl + '/resetPassword', true);
    request.setRequestHeader("Accept", "application/json");
    request.setRequestHeader("Content-Type", "application/json");
    request.setRequestHeader("Authorization", "Basic " + btoa(username + ":" + password));

    request.onreadystatechange = function() {
          if (request.readyState == 4) {
            if (request.status === 200) {
                successCallback(JSON.parse(request.responseText));
            } else {
                errorCallback(request);
            }
          }
    }
    data = {"name" : targetUsername, "password" : newPassword};
    request.send(JSON.stringify(data));
  };

  coralAPIClient.machineList = function(coralApiUrl, username, password, successCallback, errorCallback) {
    var request = new XMLHttpRequest();
    request.open("GET", coralApiUrl + '/machine', true);
    request.setRequestHeader("Accept", "application/json");
    request.setRequestHeader("Content-Type", "application/json");
    request.setRequestHeader("Authorization", "Basic " + btoa(username + ":" + password));

    request.onreadystatechange = function() {
          if (request.readyState == 4) {
            if (request.status === 200) {
                successCallback(JSON.parse(request.responseText));
            } else {
                errorCallback(request);
            }
          }
    }
    request.send(null);
  };

  coralAPIClient.enable = function(coralApiUrl, username, password, project, item, successCallback, errorCallback) {
    var request = new XMLHttpRequest();
    request.open("POST", coralApiUrl + '/enable', true);
    request.setRequestHeader("Accept", "application/json");
    request.setRequestHeader("Content-Type", "application/json");
    request.setRequestHeader("Authorization", "Basic " + btoa(username + ":" + password));
    data = {"member" : username, "project" : project, "item" : item};

    request.onreadystatechange = function() {
          if (request.readyState == 4) {
            if (request.status === 200) {
                successCallback(JSON.parse(request.responseText));
            } else {
                errorCallback(request);
            }
          }
    }
    request.send(JSON.stringify(data));
  };

  coralAPIClient.disable = function(coralApiUrl, username, password, item, successCallback, errorCallback) {
    var request = new XMLHttpRequest();
    request.open("POST", coralApiUrl + '/disable', true);
    request.setRequestHeader("Accept", "application/json");
    request.setRequestHeader("Content-Type", "application/json");
    request.setRequestHeader("Authorization", "Basic " + btoa(username + ":" + password));
    data = {"item" : item};

    request.onreadystatechange = function() {
          if (request.readyState == 4) {
            if (request.status === 200) {
                successCallback(JSON.parse(request.responseText));
            } else {
                errorCallback(request);
            }
          }
    }
    request.send(JSON.stringify(data));
  };

  coralAPIClient.reserve = function(coralApiUrl, username, password, member, project, item, bdate, minutes, successCallback, errorCallback) {
    var request = new XMLHttpRequest();
    request.open("POST", coralApiUrl + '/reservation', true);
    request.setRequestHeader("Accept", "application/json");
    request.setRequestHeader("Content-Type", "application/json");
    request.setRequestHeader("Authorization", "Basic " + btoa(username + ":" + password));
    data = {"member" : member, "project" : project, "item" : item, "bdate": bdate, "lengthInMinutes": minutes};

    request.onreadystatechange = function() {
          if (request.readyState == 4) {
            if (request.status === 200) {
                successCallback(JSON.parse(request.responseText));
            } else {
                errorCallback(request);
            }
          }
    }
    request.send(JSON.stringify(data));
  };

  coralAPIClient.getReservations = function(coralApiUrl, username, password, item , successCallback, errorCallback) {
    var request = new XMLHttpRequest();

    request.open("GET", coralApiUrl + '/reservation?machine=' + encodeURIComponent(item), true);
    request.setRequestHeader("Accept", "application/json");
    request.setRequestHeader("Content-Type", "application/json");
    request.setRequestHeader("Authorization", "Basic " + btoa(username + ":" + password));

    request.onreadystatechange = function() {
          if (request.readyState == 4) {
            if (request.status === 200) {
                successCallback(JSON.parse(request.responseText));
            } else {
                errorCallback(request);
            }
          }
    }
    request.send(null);
  };

  return coralAPIClient;
};

CoralAPI.makeStatefulClient = function(url) {
  var coralUrl = url;
  var statelessClient = CoralAPI.makeClient();
  var username = false;
  var password = false;
  var authenticated = false;
  var whoami = function(successCallback, errorCallback) {
    statelessClient.whoami(coralUrl, username, password, successCallback, errorCallback);
  };
  var authenticate = function(user, pass, successCallback, errorCallback) {
    statelessClient.authenticate(coralUrl, user, pass,
        function(evtData) {
          username = user;
          password = pass;
          whoami(successCallback, errorCallback);
        },
        function(evtData) {
          errorCallback();
        });
  };
  return {
    "authenticate": authenticate,
    "whoami": whoami
  };
};

CoralAPI.StatefulClient = function(url) {
  return CoralAPI.makeStatefulClient(url);
};

CoralAPI.Client = function() {
  return CoralAPI.makeClient();
};

export default CoralAPI;
