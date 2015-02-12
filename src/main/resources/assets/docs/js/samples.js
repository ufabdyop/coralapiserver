function getVersion(coralApiUrl) {
  var request = new XMLHttpRequest();
  request.open("GET", coralApiUrl + '/version', true);
  request.onreadystatechange = function() {
        if (request.readyState == 4) {
          if (request.status === 200) {  
              alert(request.responseText);
          } else {
              alert('ERROR: ' + request.statusText);
          }
        }
  }
  request.send(null);
};

function authenticate(coralApiUrl, username, password) {
  var request = new XMLHttpRequest();
  request.open("GET", coralApiUrl + '/authenticate', true);
  request.setRequestHeader("Accept", "application/json");
  request.setRequestHeader("Content-Type", "application/json");
  request.setRequestHeader("Authorization", "Basic " + btoa(username + ":" + password));

  request.onreadystatechange = function() {
        if (request.readyState == 4) {
          if (request.status === 200) {
              alert(request.responseText);
          } else {
              alert('ERROR: ' + request.statusText);
          }
        }
  }
  request.send(null);
};

function resetPassword(coralApiUrl, username, password, targetUsername, newPassword) {
  var request = new XMLHttpRequest();
  request.open("POST", coralApiUrl + '/resetPassword', true);
  request.setRequestHeader("Accept", "application/json");
  request.setRequestHeader("Content-Type", "application/json");
  request.setRequestHeader("Authorization", "Basic " + btoa(username + ":" + password));

  request.onreadystatechange = function() {
        if (request.readyState == 4) {
          if (request.status === 200) {
              alert(request.responseText);
          } else {
              alert('ERROR: ' + request.statusText);
          }
        }
  }
  data = {"name" : targetUsername, "password" : newPassword};
  request.send(JSON.stringify(data));
};
