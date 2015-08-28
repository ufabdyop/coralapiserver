var http = require('http');
var fs = require('fs');
var host = '172.17.42.1';
host = 'localhost';
var port = 4001;

function getJson(page, callback) {
    return http.get({
        host: host,
        port: port,
        path: page,
    }, function(response) {
        // Continuously update stream with data
        var body = '';
        response.on('data', function(d) {
            body += d;
        });
        response.on('end', function() {
            // Data reception is done, do whatever with it!
            var parsed = JSON.parse(body);
            callback( parsed );
        });
    });
};

function mkdirSync (path) {
  try {
    fs.mkdirSync(path);
  } catch(e) {
    if ( e.code != 'EEXIST' ) throw e;
  }
}


// getApiEndpoints THEN createApiData THEN downloadEndPointAPIS THEN localTransformFixes THEN convertToVersion2

var fetchData = function() {
  return new Promise(function(resolve, reject) {
    getJson('/api-docs', function(jsonDoc) {
      resolve({"root": jsonDoc, "apis": jsonDoc.apis});
    });
  });
};

var parseApiEndpoints = function(data) {
  return new Promise(function(resolve, reject) {
    var resourceUrl = "";
    var newArray = [];
    var apis = data.apis;
    for (i in apis) {
      var baseName = apis[i].path.split(/^\/v\d+\//)[1];
      resourceUrl = "http://" + host + ":" + port + "/api-docs" + apis[i].path;
      newArray.push({'baseName': baseName, 'resourceUrl': resourceUrl});
    }
    data.apis = newArray;
    resolve(data);
  });
};

var readApiEndpoints = function(data) {
  var apis = data.apis;
  var promiseChain = [];
  for (i in apis) {
    var baseName = apis[i].baseName;
    var resourceUrl = apis[i].resourceUrl;
    var tempPromise = new Promise(function(resolve, reject) {
      getJson('/api-docs/v0/' + baseName, function(jsonDoc) {
        resolve({"baseName": baseName, "api": jsonDoc.apis});
      });
    });
    promiseChain.push(tempPromise);
  }
  return Promise.all(promiseChain)
    .then(function(chainData) {
      return new Promise(function(resolve, reject) {
        data.apis = chainData;
        resolve(data);
      });
    });
};

var flattenArray = function(data) {
  return new Promise(function(resolve, reject) {
    var flatArray = [];
    for(var i in data) {
      flatArray.push(data[i][0]);
    }
    resolve(flatArray);
  });
};

var logData = function(data) {
  return new Promise(function(resolve, reject) {
    console.log(data);
    resolve(data);
  });
};

var transformJson = function(jsonDoc) {
  /* //for outer document
  data.info.title = "CoralAPIServer";
  data.info.version = apiVersion;
  */

  return new Promise(function(resolve, reject) {
    var dataArray = jsonDoc.apis;
    for (var i in dataArray) {
      data = dataArray[i].api;
      data.basePath = "http://coralapiserver.local:4001";
      var apiVersion = "0.3.5";
      for (var i in data.apis) {
        data.apis[i].path = data.apis[i].path.substring(3) ;
        for (var j in data.apis[i].operations) {
          var requiresAuth = false;
          for (var k in data.apis[i].operations[j].parameters) {
            if (data.apis[i].operations[j].parameters[k].items ) {
              delete data.apis[i].operations[j].parameters[k].items;
              data.apis[i].operations[j].parameters[k].type = 'string';
            }
            if (data.apis[i].operations[j].parameters[k].type == 'User') {
              data.apis[i].operations[j].parameters.splice(k, 1);
              requiresAuth = true;
            }
          }
          if (requiresAuth) {
            data.apis[i].operations[j].security = { "basicAuth": [] };
          }
        }
      }
      dataArray[i].api = data;
    }
    jsonDoc.apis = dataArray;
    resolve(jsonDoc);
  });
}


mkdirSync('./output');
mkdirSync('./output/swaggerv1.2');
mkdirSync('./output/swaggerv2');

fetchData().then(function(data) {
  return parseApiEndpoints(data);
}).then(function(data) {
  return readApiEndpoints(data);
}).then(function(data) {
  return transformJson(data);
}).then(function(data) {
  return logData(data);
});
