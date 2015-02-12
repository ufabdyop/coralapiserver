var port = window.location.port;
var portOverride=window.location.href.split('#')[1];
if (typeof portOverride != 'undefined' ) {
  port = portOverride;
}
var versionPrefix = '/v0';
var apiUrl = window.location.protocol + '//' + window.location.hostname + ':' + port + versionPrefix;

//port override

$('#apiUrl').val(apiUrl);
$('#currentApi').html(apiUrl);

function overrideApiUrl() {
  apiUrl = $('#apiUrl').val();
  $('#currentApi').html(apiUrl);
}
