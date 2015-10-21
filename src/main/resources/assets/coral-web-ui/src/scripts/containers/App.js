import React, { Component, PropTypes } from 'react';
import { connect } from 'react-redux';
import { logInAttempt } from '../actions'
import LoginForm from '../components/loginFormComponent';
import Reservations from '../components/reservationsComponent';

class App extends Component {
  render() {
    const dispatch = this.props.dispatch;
    switch(this.props.router.location) {
      case '/reservations':
        return <Reservations/>;
      default:
        return (
            <LoginForm
              onLoginSubmit={credentials =>
                dispatch(logInAttempt(credentials))
              } />
        );
    }
  }
}

// Which props do we want to inject, given the global state?
// Note: use https://github.com/faassen/reselect for better performance.
function select(state) {
  return state;
}

// Wrap the component to inject dispatch and state into it
export default connect(select)(App);
