#!/bin/sh
curl -s -o index.json localhost:4001/api-docs
curl -s -o enable.json http://localhost:4001/api-docs/v0/enable
curl -s -o authenticate.json http://localhost:4001/api-docs/v0/authenticate
curl -s -o resetPassword.json http://localhost:4001/api-docs/v0/resetPassword
curl -s -o member.json http://localhost:4001/api-docs/v0/member
curl -s -o account.json http://localhost:4001/api-docs/v0/account
curl -s -o projects.json http://localhost:4001/api-docs/v0/projects
curl -s -o whoami.json http://localhost:4001/api-docs/v0/whoami
curl -s -o version.json http://localhost:4001/api-docs/v0/version
curl -s -o reservation.json http://localhost:4001/api-docs/v0/reservation
curl -s -o checkKey.json http://localhost:4001/api-docs/v0/checkKey
curl -s -o projectMembership.json http://localhost:4001/api-docs/v0/projectMembership
curl -s -o labRoles.json http://localhost:4001/api-docs/v0/labRoles
curl -s -o disable.json http://localhost:4001/api-docs/v0/disable
curl -s -o machine.json http://localhost:4001/api-docs/v0/machine
node convert.js > apidocsv2.json
echo apidocsv2.json is written
