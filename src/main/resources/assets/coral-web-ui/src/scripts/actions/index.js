import CoralPromisesAPI from './coralPromisesAPI';
var coralUrl = CoralPromisesAPI.determineUrlFromBrowser();
var coralClient = new CoralPromisesAPI.StatefulClient(coralUrl);
/*
 * action types
 */

export const LOGGED_IN = 'LOGGED_IN';
export const LOGIN_ATTEMPT = 'LOGIN_ATTEMPT';
export const ERROR = 'ERROR';
export const ROUTE_EVENT = 'ROUTE_EVENT';

/*
 * action creators
 */

export function loggedIn(credentials) {
  return { type: LOGGED_IN, credentials };
}

export function addError(data) {
  return { type: ERROR, data };
}

export function landingPage() {
  return {type: ROUTE_EVENT, location: "/"};
}

export function logInAttempt(credentials) {
  return function (dispatch) {
    dispatch({ type: LOGIN_ATTEMPT, credentials });
    coralClient.authenticate(credentials.username, credentials.password)
    .then(
      function(response) {
        dispatch(loggedIn(credentials));
      }
    )
    .then(
      function(response) {
        dispatch(landingPage());
      }
    )
    .catch(function(request) {
      console.log(request);
    });
  };
}
