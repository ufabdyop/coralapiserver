Coral Web UI
===
Simple implementation of client for Coral API Server.

Install Prerequisites
---
    npm install

Build and Serve Files
---
    npm run build && npm start

Files can be served with any static asset server (no server side logic needed)

During development, it's useful to use 'watch' to recompile assets upon file changes:

    npm start &
    npm run watch


Architecture
---
Uses redux and react.

App state
---
App state is represented with something like this (see details in src/scripts/reducers.js):

{
  "logged_in": true,
  "credentials": {
    "username": "foo"
  }
  "current_page": "reservations"
}
