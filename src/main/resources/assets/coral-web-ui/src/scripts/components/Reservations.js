"use strict";
import React, { findDOMNode, Component, PropTypes } from 'react';

export default class Reservations extends Component {
  render() {
          return (
            <div>
              <h1 className="page-header">Reservations</h1>
              <div className="row">
                <form id="page-reservation-form"
                onSubmit={e => {
                  e.preventDefault();
                  this.handleSubmit(e);
                }}>
                  <div className="form-group">
                    <label htmlFor="instrument">Instrument:</label><br />
                    <select id="instrument" name="instrument" ref="instrument" >
                      {this.props.instruments.map(instrument =>
                        <option value={instrument}>instrument</option>
                      )}
                    </select>
                  </div>
                  <div className="form-group">
                    <label htmlFor="minutes">Minutes:</label><br />
                    <input id="minutes" name="minutes" ref="minutes" /><br />
                  </div>
                  <input className="btn btn-default" type="submit" id="res-submit" defaultValue="Submit" name="res-submit" /><br />
                </form>
              </div>
            </div>
          );
        }

  handleSubmit(e) {
    const instrumentNode = findDOMNode(this.refs.instrument);
    const minutesNode = findDOMNode(this.refs.minutes);
    const instrument = instrumentNode.value.trim();
    const minutes = minutesNode.value.trim();
    this.props.submit({instrument, minutes});
  }
}

Reservations.propTypes = {
  submit: PropTypes.func.isRequired
};
