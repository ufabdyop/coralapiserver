package edu.utah.nanofab.coralapiserver;

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
        bootstrap.setName("hello-world");
    }

    @Override
    public void run(CoralApiConfiguration configuration,
                    Environment environment) {
	final String template = configuration.getTemplate();
	final String defaultName = configuration.getDefaultName();
	environment.addResource(new CoralApiResource(template, defaultName));
	environment.addHealthCheck(new TemplateHealthCheck(template));
    }

}
