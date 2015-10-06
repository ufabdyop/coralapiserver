var DEBUGMODE = true;
var debugLog;
if (DEBUGMODE) {
  debugLog = function(msg) {console.log(msg)};
} else {
  debugLog = function() {}
}

export {debugLog, DEBUGMODE};
