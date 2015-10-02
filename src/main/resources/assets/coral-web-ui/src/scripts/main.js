"use strict";

import CoralAPI from './coralApi';
import WelcomeBox from './components/welcomeComponent';
import DashboardAction from './components/dashboardActionComponent';
import DashboardActionBoard from './components/dashboardActionBoardComponent';
import LoginForm from './components/loginFormComponent'

/******** DEBUGGER LOGGER **********************/
var DEBUGMODE=true;
var debug = function(msg) {
  if(DEBUGMODE){
    console.log(msg);
  }
};
/***********************************************/

/***SET UP THE CORAL Client  *********************/
var coralApiUrl = CoralAPI.determineUrlFromBrowser();
debug("coralApiUrl: " + coralApiUrl);
var coralClient = new CoralAPI.StatefulClient(coralApiUrl);
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

        updateAppState(action, currentAppState);
      }
    );
});
/***********************************************/

/********WIRE RESERVATIONS BUTTON**********************/
var reservationsButton = document.querySelector('#reservations');
reservationsButton.addEventListener("click", function(formEvent) {
  debug(formEvent);
  var action = {
    "type": "RESERVATIONS",
    "data": {}
  };
  updateAppState(action, currentAppState);
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
    case "RESERVATIONS":
      nextAppState = makeAppStateTemplate();
      nextAppState.active_page = "reservations";
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
    case "reservations":
      renderReservations(state);
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
  React.render(
    <DashboardActionBoard />,
    document.getElementById('dashboard-actions')
  );
};

var renderReservations = function(state) {
  document.querySelector('#page-reservations').style.display = 'block';
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
