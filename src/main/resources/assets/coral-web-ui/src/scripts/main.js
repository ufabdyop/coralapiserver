"use strict";

import CoralAPI from './coralApi';
import WelcomeBox from './components/welcomeComponent';
import DashboardAction from './components/dashboardActionComponent';
import DashboardActionBoard from './components/dashboardActionBoardComponent';
import LoginForm from './components/loginFormComponent'
import {debugLog, DEBUGMODE} from './debug'

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
