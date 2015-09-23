var express = require('express');
var app = new express();
var port = 4002;

app.use(express.static('dist'));

app.get("/", function(req, res) {
  res.sendFile(__dirname + '/dist/index.html');
});

app.listen(port, function(error) {
  if (error) {
    console.error(error);
  } else {
    console.info("==> 🌎  Listening on port %s. Open up http://localhost:%s/ in your browser.", port, port);
  }
});
