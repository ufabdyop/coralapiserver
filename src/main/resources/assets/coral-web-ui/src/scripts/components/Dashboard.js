"use strict";
import React, { findDOMNode, Component, PropTypes } from 'react';
import DashboardAction from './DashboardAction';

/**
* props: thumbnail, header, description
*/
var Dashboard = React.createClass({
  render: function() {
    return (
      <div>
        <DashboardAction header="Reservations" thumb="assets/images/clock.svg" description="Make/Check Reservations" />
        <DashboardAction header="Enables" thumb="assets/images/clock.svg" description="Coming Soon" />
        <DashboardAction header="Report a Problem" thumb="assets/images/clock.svg" description="Let us know about equipment issues." />
      </div>
    );
  }
});

export default Dashboard;
