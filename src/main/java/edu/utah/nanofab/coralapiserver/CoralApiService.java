package edu.utah.nanofab.coralapiserver;

import edu.utah.nanofab.coralapiserver.resources.CoralApiMemberResource;
import edu.utah.nanofab.coralapiserver.resources.CoralApiResource;
import edu.utah.nanofab.coralapiserver.health.TemplateHealthCheck;

import com.yammer.dropwizard.Service;
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
		final String template = configuration.getTemplate();
		final String defaultName = configuration.getDefaultName();
		final TokenConfiguration[] tokens = configuration.getAuthTokensConfiguration().getTokens();
		environment.addResource(new CoralApiResource(template, defaultName, tokens));
		environment.addResource(new CoralApiMemberResource(template, defaultName));
		environment.addHealthCheck(new TemplateHealthCheck(template));
    }

}
