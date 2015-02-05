function getVersion (coralApiUrl) {
  $.ajax({
    url: coralApiUrl + "/version",
    type: "GET"
  }).success(function(data, code) {
    alert("Version is " + data.versionNumber);
  }).error(function(data, code) {
    alert(" Error " + data);
  });
};

function authenticate(coralApiUrl, username, password) {
  $.ajax({
    url: coralApiUrl + "/authenticate",
    type: "GET",
    beforeSend: function(xhr) {
      xhr.setRequestHeader("Authorization",
        "Basic " + btoa(username + ":" + password)
        );
    }
  }).success(function(data, code) {
    alert("Response is " + JSON.stringify(data));
  }).error(function(data, code) {
    alert(" Error " + JSON.stringify(data));
  });
};
