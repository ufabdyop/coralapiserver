"use strict";
import React, { findDOMNode, Component, PropTypes } from 'react';

export default class LoginForm extends Component {
  render() {
          return (
            <div>
              <h1 className="page-header">Login</h1>
              <div className="row">
                <form id="page-login-form"
                onSubmit={e => {
                  e.preventDefault();
                  this.handleSubmit(e);
                }}>
                  <div className="form-group">
                    <label htmlFor="login-username">Username:</label><br />
                    <input id="login-username" name="login-username" ref="username" /><br />
                  </div>
                  <div className="form-group">
                    <label htmlFor="login-password">Password:</label><br />
                    <input type="password" id="login-password" name="login-password" ref="password" /><br />
                  </div>
                  <input className="btn btn-default" type="submit" id="login-submit" defaultValue="Login" name="login-submit" /><br />
                </form>
              </div>
            </div>
          );
        }

  handleSubmit(e) {
    const usernameNode = findDOMNode(this.refs.username);
    const passwordNode = findDOMNode(this.refs.password);
    const username = usernameNode.value.trim();
    const password = passwordNode.value.trim();
    this.props.onLoginSubmit(username, password);
  }
}

LoginForm.propTypes = {
  onLoginSubmit: PropTypes.func.isRequired
};
