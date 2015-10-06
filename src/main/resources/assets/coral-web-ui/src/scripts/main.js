"use strict";

import CoralAPI from './coralApi';
import WelcomeBox from './components/welcomeComponent';
import DashboardAction from './components/dashboardActionComponent';
import DashboardActionBoard from './components/dashboardActionBoardComponent';
import LoginForm from './components/loginFormComponent'
import {debugLog, DEBUGMODE} from './debug'
import { LOGGED_IN, ERROR } from './actions'
import { loggedIn, addError } from './actions'

/***SET UP THE CORAL Client  *********************/
var coralApiUrl = CoralAPI.determineUrlFromBrowser();
debugLog("coralApiUrl: " + coralApiUrl);
var coralClient = new CoralAPI.StatefulClient(coralApiUrl);
/***********************************************/

/***********************************************/
if (DEBUGMODE) {
      //document.querySelector('#login-username').value = 'auth-token';
      //document.querySelector('#login-password').value = '0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9';
}

import { createStore } from 'redux';
import webApp from './reducers';

let store = createStore(webApp);

// Log the initial state
console.log(store.getState());

// Every time the state changes, log it
let unsubscribe = store.subscribe(() =>
  console.log(store.getState())
);

// Dispatch some actions
store.dispatch(addError('Learn about actions'));
store.dispatch(addError('Learn about reducers'));
store.dispatch(addError('Learn about store'));
store.dispatch(loggedIn({"username": "foobar"}));

// Stop listening to state updates
unsubscribe();
