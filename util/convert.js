var convert = require('swagger-converter');
var resourceListing = require('./index.json');
var apiDeclarations = [
  require('./enable.json'),
  require('./authenticate.json'),
  require('./resetPassword.json'),
  require('./member.json'),
  require('./account.json'),
  require('./projects.json'),
  require('./whoami.json'),
  require('./version.json'),
  require('./reservation.json'),
  require('./checkKey.json'),
  require('./projectMembership.json'),
  require('./labRoles.json'),
  require('./disable.json'),
  require('./machine.json')];
var swagger2Document = convert(resourceListing, apiDeclarations);
console.log(JSON.stringify(swagger2Document, null, 2));
