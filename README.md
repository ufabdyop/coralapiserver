This is based on the hello world from dropwizard.codahale.com

to package: mvn package
to run: java -jar target/coralapiserver-{version}.jar server coral-api.yml

to run without packaging first (which takes a long time): mvn exec:java -Dexec.args="server coral-api.yml"
