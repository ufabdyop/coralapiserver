import React, { Component, PropTypes } from 'react';
import { connect } from 'react-redux';
import { LOGGED_IN, ERROR } from '../actions'
import { loggedIn, addError, logInAttempt } from '../actions'
import LoginForm from '../components/loginFormComponent';

class App extends Component {
  render() {
    const dispatch = this.props.dispatch;
    return (
      <div>
        <LoginForm
          onLoginSubmit={credentials =>
            dispatch(logInAttempt(credentials))
          } />
      </div>
    );
  }
}

// Which props do we want to inject, given the global state?
// Note: use https://github.com/faassen/reselect for better performance.
function select(state) {
  return state;
}

// Wrap the component to inject dispatch and state into it
export default connect(select)(App);
