import { LOGGED_IN } from './actions';

const initialState = {
  "application": {
    "logged_in": false,
    "credentials": {
      "username": null,
      "password": null,
      "auth-token": null
    }
  },
  "router": {
    "location": null
  }
};

export default function webApp(state = initialState, action) {
  switch (action.type) {
    case LOGGED_IN:
      return Object.assign({}, state, {
        "application": {
          "credentials": action.credentials,
          "logged_in": true
        },
        "router": {
          "location": "/reservations"
        }
      });
    default:
      return state;
  }
}
