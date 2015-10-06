import { LOGGED_IN } from './actions';

const initialState = {
  "logged_in": false,
  "credentials": {
    "username": null,
    "password": null,
    "auth-token": null
  },
  "current_page": "login"
};

export default function webApp(state = initialState, action) {
  switch (action.type) {
    case LOGGED_IN:
      return Object.assign({}, state, {
        "current_page": "dashboard",
        "credentials": action.credentials
      });
    default:
      return state;
  }
}
