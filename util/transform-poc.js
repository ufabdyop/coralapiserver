var apiVersion = "0.3.5";
var fs = require('fs');
var account = require("./output/account.json");
account.basePath = "http://coralapiserver.local:4001";

/* //for outer document
account.info.title = "CoralAPIServer";
account.info.version = apiVersion;
*/

for (var i in account.apis) {
  account.apis[i].path = account.apis[i].path.substring(3) ;
  for (var j in account.apis[i].operations) {
    var requiresAuth = false;
    for (var k in account.apis[i].operations[j].parameters) {
      if (account.apis[i].operations[j].parameters[k].items ) {
        delete account.apis[i].operations[j].parameters[k].items;
        account.apis[i].operations[j].parameters[k].type = 'string';
      }
      if (account.apis[i].operations[j].parameters[k].type == 'User') {
        account.apis[i].operations[j].parameters.splice(k, 1);
        requiresAuth = true;
      }
    }
    if (requiresAuth) {
      account.apis[i].operations[j].security = { "basicAuth": [] };
    }
  }
}



console.log(JSON.stringify(account));


//parameters:
//   [ { name: 'body',
//          required: false,
//                 type: 'Account',
//                        paramType: 'body' },
//                             { name: 'body', required: false, type: 'User'
