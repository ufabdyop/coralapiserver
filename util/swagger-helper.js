var http = require('http');
var fs = require('fs');
var swaggerConverter = require('swagger-converter');
var host = '172.17.42.1';
host = 'localhost';
var port = 4001;
var apiVersion = "0.3.5";

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
  for (var i in apis) {
    var tempPromise = new Promise(function(resolve, reject) {
      var baseName = apis[i].baseName;
      var resourceUrl = apis[i].resourceUrl;
      getJson('/api-docs/v0/' + baseName, function(jsonDoc) {
        resolve({"baseName": baseName, "api": jsonDoc});
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

var flattenApis = function(data) {
  return new Promise(function(resolve, reject) {
    var flatArray = [];
    for(var i in data['apis']) {
      flatArray.push(data['apis'][i]['api']);
    }
    data['apis'] = flatArray;
    resolve(data);
  });
};

var writeVersion1 = function(data) {
  return new Promise(function(resolve, reject) {
    for (var i in data['apis']) {
      var base = data['apis'][i]['baseName'];
      var contents = data['apis'][i]['api'];
      fs.writeFileSync('./output/swaggerv1.2/' + base + '.json', JSON.stringify(contents));
    }
    resolve(data);
  });
};

var writeVersion2 = function(data) {
  return new Promise(function(resolve, reject) {
      fs.writeFileSync('./output/swaggerv2/api.json', JSON.stringify(data));
      resolve(data);
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
    for (var h in dataArray) {
      var data = dataArray[h];
      data.basePath = "http://coralapiserver.local:4001";
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
      dataArray[h] = data;
    }
    jsonDoc.apis = dataArray;
    resolve(jsonDoc);
  });
};

var convertToVersion2 = function(data) {
  return new Promise(function(resolve, reject) {
    var resourceListing = data.root;
    var apiDeclarations = data.apis
    var swagger2Document = swaggerConverter(resourceListing, apiDeclarations);
    resolve(swagger2Document);
  });
};

var updateVersion2 = function(data) {
  return new Promise(function(resolve, reject) {
    data.info.version = apiVersion;
    data.info.title = "Coral API Server";
    resolve(data);
  });
};

mkdirSync('./output');
mkdirSync('./output/swaggerv1.2');
mkdirSync('./output/swaggerv2');

fetchData().then(function(data) {
  return parseApiEndpoints(data);
}).then(function(data) {
  console.log("----STEP 1------");
  return readApiEndpoints(data);
}).then(function(data) {
  console.log("----STEP 2.5------");
  console.log(data);
  return writeVersion1(data);
}).then(function(data) {
  console.log("----STEP 2------");
  console.log(data);
  return flattenApis(data);
}).then(function(data) {
  console.log("----STEP 3------");
  console.log(data);
  return transformJson(data);
}).then(function(data) {
  console.log("----STEP 4------");
  console.log(data);
  return convertToVersion2(data);
}).then(function(data) {
  console.log("----STEP 4.2------");
  console.log(data);
  return updateVersion2(data);
}).then(function(data) {
  console.log("----STEP 4.5------");
  console.log(data);
  return writeVersion2(data);
}).then(function(data) {
  console.log("----STEP 5------");
  console.log(data);
  //return logData(data);
});
