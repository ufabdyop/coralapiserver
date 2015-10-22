"use strict";
import React, { findDOMNode, Component, PropTypes } from 'react';

var Welcome = React.createClass({
  displayName: 'Welcome',
  render: function() {
    if (this.props.show == true) {
      return (
        <div className="welcomeBox">
          Welcome! You are logged in as {this.props.username}
        </div>
      );
    } else {
      return (<div/>);
    }
  }
});

export default Welcome;
