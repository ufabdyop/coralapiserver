"use strict";
import CoralAPI from './coralApi'
var coralSyncClient = new CoralAPI.Client();
var CoralPromisesAPI = {};

CoralPromisesAPI.makeClient = function() {
  var CoralPromisesAPIClient = {};

  CoralPromisesAPIClient.getVersion = function(CoralAPIUrl) {
    return new Promise(function (resolve, reject) {
      CoralAPI.getVersion(CoralAPIUrl, resolve, reject);
    });
  };

  CoralPromisesAPIClient.authenticate = function(CoralAPIUrl, username, password) {
    return new Promise(function (resolve, reject) {
      coralSyncClient.authenticate(CoralAPIUrl, username, password, resolve, reject);
    });
  };

  CoralPromisesAPIClient.whoami = function(CoralAPIUrl, username, password) {
    return new Promise(function (resolve, reject) {
      coralSyncClient.whoami(CoralAPIUrl, username, password, resolve, reject);
    });
  };

  CoralPromisesAPIClient.resetPassword = function(CoralAPIUrl, username, password, targetUsername, newPassword) {
  };

  CoralPromisesAPIClient.machineList = function(CoralAPIUrl, username, password) {
  };

  CoralPromisesAPIClient.enable = function(CoralAPIUrl, username, password, project, item) {
  };

  CoralPromisesAPIClient.disable = function(CoralAPIUrl, username, password, item) {
  };

  CoralPromisesAPIClient.reserve = function(CoralAPIUrl, username, password, member, project, item, bdate, minutes) {
  };

  CoralPromisesAPIClient.getReservations = function(CoralAPIUrl, username, password, item) {
  };

  return CoralPromisesAPIClient;
};

CoralPromisesAPI.makeStatefulClient = function(url) {
  var coralUrl = url;
  var statelessClient = CoralPromisesAPI.makeClient();
  var username = false;
  var password = false;
  var authenticated = false;
  var whoami = function() {
    return statelessClient.whoami(coralUrl, username, password);
  };
  var authenticate = function(user, pass) {
    return statelessClient.authenticate(coralUrl, user, pass)
    .then(function() {
        return new Promise(function (resolve, reject) {
          username = user;
          password = pass;
          resolve();
        });
      }
    ).then(
      function() {
        return whoami();
      }
    );
  };
  return {
    "authenticate": authenticate,
    "whoami": whoami
  };
};

CoralPromisesAPI.StatefulClient = function(url) {
  return CoralPromisesAPI.makeStatefulClient(url);
};

CoralPromisesAPI.Client = function() {
  return CoralPromisesAPI.makeClient();
};

CoralPromisesAPI.determineUrlFromBrowser = function() {
  return CoralAPI.determineUrlFromBrowser();
};

export default CoralPromisesAPI;
