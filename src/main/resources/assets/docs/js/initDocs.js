var apiUrl = window.location.protocol + '//' + window.location.hostname + ':' + window.location.port ;

$('#apiUrl').val(apiUrl);
$('#currentApi').html(apiUrl);

function overrideApiUrl() {
  apiUrl = $('#apiUrl').val();
  $('#currentApi').html(apiUrl);
}
