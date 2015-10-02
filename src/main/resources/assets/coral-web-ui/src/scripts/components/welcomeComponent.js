var WelcomeBox = React.createClass({
  displayName: 'WelcomeBox',
  render: function() {
    return (
      <div className="welcomeBox">
        Welcome! You are logged in as {this.props.username}
      </div>
    );
  }
});

export default WelcomeBox;
