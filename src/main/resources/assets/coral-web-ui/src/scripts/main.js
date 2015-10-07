"use strict";

import CoralPromisesAPI from './coralApi';
import WelcomeBox from './components/welcomeComponent';
import DashboardAction from './components/dashboardActionComponent';
import DashboardActionBoard from './components/dashboardActionBoardComponent';
import LoginForm from './components/loginFormComponent'
import {debugLog, DEBUGMODE} from './debug'
import { LOGGED_IN, ERROR } from './actions'
import { loggedIn, addError, logInAttempt } from './actions'
import thunkMiddleware from 'redux-thunk';
import createLogger from 'redux-logger';
import { createStore, applyMiddleware } from 'redux';

import webApp from './reducers';
import App from './containers/App';
import { Provider } from 'react-redux';
import React from 'react';

/***SET UP THE CORAL Client  *********************/
var coralApiUrl = CoralPromisesAPI.determineUrlFromBrowser();
debugLog("coralApiUrl: " + coralApiUrl);
var coralClient = new CoralPromisesAPI.StatefulClient(coralApiUrl);
/***********************************************/

/***********************************************/
if (DEBUGMODE) {
      console.log('auth-token');
      console.log('0qqCSnMFXxvFK8hzBJm56eaqWgVwDUMNCF5CToiS9b5DB7TJV9');
}

const loggerMiddleware = createLogger();

const createStoreWithMiddleware = applyMiddleware(
  thunkMiddleware, // lets us dispatch() functions
  loggerMiddleware // neat middleware that logs actions
)(createStore);

const store = createStoreWithMiddleware(webApp);

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
