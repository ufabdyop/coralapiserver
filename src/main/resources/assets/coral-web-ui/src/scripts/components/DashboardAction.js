"use strict";
import React, { findDOMNode, Component, PropTypes } from 'react';

/**
* props: thumbnail, header, description
*/
var DashboardAction = React.createClass({
  render: function() {
    return (
      <div className="col-xs-6 col-sm-3 placeholder">
        <img src={this.props.thumb} className="img-responsive" alt={this.props.header} />
        <h4>{this.props.header}</h4>
        <span className="text-muted">{this.props.description}</span>
      </div>
    );
  }
});

export default DashboardAction;
