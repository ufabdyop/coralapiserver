var port = window.location.port;
var portOverride=window.location.href.split('#')[1];
if (typeof portOverride != 'undefined' ) {
  port = portOverride;
}
var versionPrefix = '/v0';
var apiUrl = window.location.protocol + '//' + window.location.hostname + ':' + port + versionPrefix;

//port override

document.getElementById('apiUrl').value = apiUrl;
document.getElementById('currentApi').innerHTML = apiUrl;

function overrideApiUrl() {
  apiUrl = document.getElementById('apiUrl').value;
  document.getElementById('currentApi').innerHTML = apiUrl;
}
