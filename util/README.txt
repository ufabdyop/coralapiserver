convert our 1.2 swagger docs to 2.0

run:
npm install swagger-converter json2yaml
node swagger-helper.js

if successful:
cp output/swaggerv2/coralApi.yaml ../src/main/resources/assets/swagger/api-docs/coralApi.yaml
