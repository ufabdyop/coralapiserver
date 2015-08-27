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

function getDocs ( callback ) {
  getJson('/api-docs', function(jsonDoc) {
    callback(jsonDoc.apis);
  });
};

function mkdirSync (path) {
  try {
    fs.mkdirSync(path);
  } catch(e) {
    if ( e.code != 'EEXIST' ) throw e;
  }
}

var createApiData = function(apis) {
  var resourceUrl = "";
  var newArray = [];
  for (i in apis) {
    basename = apis[i].path.split(/^\/v\d+\//)[1];
    resourceUrl = "http://" + host + ":" + port + "/api-docs" + apis[i].path;
    newArray.push({'baseName': basename, 'resourceUrl': resourceUrl});
  }
};

getDocs(getApiEndpoints);
getDocs(writeConverterScript);

console.log("Now run:\ncd output\nnpm install swagger-converter\nsh convert.sh");
