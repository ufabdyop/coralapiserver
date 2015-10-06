"use strict";

import CoralAPI from './coralApi';
import WelcomeBox from './components/welcomeComponent';
import DashboardAction from './components/dashboardActionComponent';
import DashboardActionBoard from './components/dashboardActionBoardComponent';
import LoginForm from './components/loginFormComponent'
import {debugLog, DEBUGMODE} from './debug'
import { LOGGED_IN, ERROR } from './actions'
import { loggedIn, addError } from './actions'
import { createStore } from 'redux';
import webApp from './reducers';
import App from './containers/App';
import { Provider } from 'react-redux';
import React from 'react';

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

let store = createStore(webApp);

// Log the initial state
debugLog("initial state");
debugLog(store.getState());

let rootElement = document.getElementById('page-root');
React.render(
  // The child must be wrapped in a function
  // to work around an issue in React 0.13.
  <Provider store={store}>
    {() => <App />}
  </Provider>,
  rootElement
);
