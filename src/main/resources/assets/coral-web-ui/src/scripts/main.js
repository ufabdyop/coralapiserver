"use strict";

import CoralPromisesAPI from './lib/coralApi';
import {debugLog, DEBUGMODE} from './util/debug'
import thunkMiddleware from 'redux-thunk';
import createLogger from 'redux-logger';
import { createStore, applyMiddleware } from 'redux';

import reducer from './reducers';
import App from './components/App';
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

const store = createStoreWithMiddleware(reducer);

// Log the initial state
debugLog("initializing. store.getState():");
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
