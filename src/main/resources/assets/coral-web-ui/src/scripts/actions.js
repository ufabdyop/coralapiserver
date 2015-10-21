import CoralPromisesAPI from './coralPromisesAPI';
var coralUrl = CoralPromisesAPI.determineUrlFromBrowser();
var coralClient = new CoralPromisesAPI.StatefulClient(coralUrl);
/*
 * action types
 */

export const LOGGED_IN = 'LOGGED_IN';
export const LOGIN_ATTEMPT = 'LOGIN_ATTEMPT';
export const ERROR = 'ERROR';

/*
 * action creators
 */

export function loggedIn(credentials) {
  return { type: LOGGED_IN, credentials };
}

export function addError(data) {
  return { type: ERROR, data };
}

export function logInAttempt(credentials) {
  return function (dispatch) {
    dispatch({ type: LOGIN_ATTEMPT, credentials });
    coralClient.authenticate(credentials.username, credentials.password).then(
      function(response) {
        dispatch(loggedIn(credentials));
      }
    ).catch(function(request) {
      console.log(request);
    });
  };
}
