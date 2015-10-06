/* var makeAppStateTemplate = function() {
  return {
    "logged_in": false,
    "username": {},
    "active_page": "login"
  };
};
*/

/*
 * action types
 */

export const LOGGED_IN = 'LOGGED_IN';
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
