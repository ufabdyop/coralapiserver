package edu.utah.nanofab.coralapiserver;

import edu.utah.nanofab.coralapiserver.resources.CoralApiMemberResource;
import edu.utah.nanofab.coralapiserver.auth.CoralCredentials;
import edu.utah.nanofab.coralapiserver.auth.SimpleAuthenticator;
import edu.utah.nanofab.coralapiserver.auth.User;
import edu.utah.nanofab.coralapiserver.health.TemplateHealthCheck;

import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.auth.basic.BasicAuthProvider;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

public class CoralApiService extends Service<CoralApiConfiguration> {
    public static void main(String[] args) throws Exception {
        new CoralApiService().run(args);
    }

    @Override
    public void initialize(Bootstrap<CoralApiConfiguration> bootstrap) {
        bootstrap.setName("coral-api");
    }

    @Override
    public void run(CoralApiConfiguration configuration,
                    Environment environment) {
		final String coralIor = configuration.getCoralIor();
		final String coralConfigUrl = configuration.getCoralConfigUrl();
		final TokenConfiguration[] tokens = configuration.getAuthTokensConfiguration().getTokens();
		environment.addProvider(new BasicAuthProvider<User>(new SimpleAuthenticator(tokens), "REALM STRING"));
		environment.addResource(new CoralApiMemberResource(coralIor, coralConfigUrl));
		//environment.addHealthCheck(new TemplateHealthCheck(template));
    }

}
