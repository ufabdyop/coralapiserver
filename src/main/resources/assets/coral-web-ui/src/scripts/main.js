"use strict";

import CoralAPI from './coralApi';
import WelcomeBox from './welcomeComponent';

/***SET UP THE CORAL URL  *********************/
var apiVersion = 'v0';
var coralApiUrl = window.location.search.match(/url=([^&]+)/);
if (coralApiUrl && coralApiUrl.length > 1) {
  coralApiUrl = decodeURIComponent(coralApiUrl[1]);
} else {
  coralApiUrl = window.location.protocol + '//' + window.location.hostname + ':' + window.location.port + '/' + apiVersion ;
}
console.log("coralApiUrl: " + coralApiUrl);

/***SET UP SOME GLOBAL DATA*********************/
var makeAppStateTemplate = function() {
  return {
    "logged_in": false,
    "username": {},
    "active_page": "login"
  };
};
var copyAppState = function(state) {
  return Object.create(state);
};
var initialAppState = makeAppStateTemplate();

var actionTypes = ["LOGGED_IN"];
var validPages = ["login", "dashboard"];
var coralUrl = 'http://localhost:4001/v0';
var coralClient = new CoralAPI.StatefulClient(coralUrl);
var currentAppState = initialAppState;
var DEBUGMODE=true;
/***********************************************/

/******** DEBUGGER LOGGER **********************/
var debug = function(msg) {
  console.log(msg);
};
/***********************************************/

/********WIRE LOGIN BUTTON**********************/
var loginForm = document.querySelector('#page-login-form');
loginForm.addEventListener("submit", function(formEvent) {
  debug(formEvent);
  formEvent.preventDefault();
  coralClient.authenticate(
      document.querySelector('#login-username').value,
      document.querySelector('#login-password').value,
      function(credentials) {
        var action = {
          "type": "LOGGED_IN",
          "data": credentials
        };
        updateAppState(action, currentAppState);
      },
      function(evtData) {
        var action = {
          "type": "ERROR",
          "data": evtData
        };
        updateAppState(action, currentAppState);
      }
    );
});
/***********************************************/

/********SEND ACTIONS THROUGH APP **************/
var updateAppState = function(action, previousAppState) {
  debug(action);
  var nextAppState = copyAppState(previousAppState);
  switch(action.type){
    case "LOGGED_IN":
      nextAppState = makeAppStateTemplate();
      nextAppState.username = action.data.username;
      nextAppState.active_page = "dashboard";
      debug(nextAppState);
      break;
    default:
      debug("No action for " + action.type);
  }
  currentAppState = nextAppState;
  renderApp(currentAppState);
};

var renderApp = function(state) {
  debug(state);
  hidePages();
  switch(state.active_page) {
    case "dashboard":
      renderDashboard(state);
      break;
    default:
      debug("No such page renderer: " + state.active_page);
  }
};

var renderDashboard = function(state) {
  document.querySelector('#page-dashboard').style.display = 'block';
  React.render(
    <WelcomeBox username={state.username} />,
    document.getElementById('welcome-component')
  );
};

var hidePages = function() {
  for( var i in validPages) {
    var page = validPages[i];
    document.querySelector('#page-' + page).style.display = 'none';
  }
};

/***********************************************/

if (DEBUGMODE) {
      document.querySelector('#login-username').value = 'auth-token';
      document.querySelector('#login-password').value = '0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9';
}
