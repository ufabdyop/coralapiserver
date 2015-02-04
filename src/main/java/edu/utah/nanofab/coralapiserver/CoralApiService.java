package edu.utah.nanofab.coralapiserver;

import java.util.concurrent.ConcurrentHashMap;

import io.dropwizard.Application;
import io.dropwizard.auth.basic.BasicAuthProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.assets.AssetsBundle;
import edu.utah.nanofab.coralapiserver.auth.SimpleAuthenticator;
import edu.utah.nanofab.coralapiserver.auth.User;
import edu.utah.nanofab.coralapiserver.resources.CoralApiAccountResource;
import edu.utah.nanofab.coralapiserver.resources.CoralApiAuthTokenResource;
import edu.utah.nanofab.coralapiserver.resources.CoralApiLabRoleResource;
import edu.utah.nanofab.coralapiserver.resources.CoralApiMemberResource;
import edu.utah.nanofab.coralapiserver.resources.CoralApiPasswordResetResource;
import edu.utah.nanofab.coralapiserver.resources.CoralApiProjectMembershipResource;
import edu.utah.nanofab.coralapiserver.resources.CoralApiProjectResource;
import edu.utah.nanofab.coralapiserver.resources.CoralApiProjectsResource;
import edu.utah.nanofab.coralapiserver.resources.CoralApiVersionResource;
import edu.utah.nanofab.coralapiserver.resources.CoralApiWhoAmIResource;


public class CoralApiService extends Application<CoralApiConfiguration> {
    public static void main(String[] args) throws Exception {
        new CoralApiService().run(args);
    }

    @Override
    public void initialize(Bootstrap<CoralApiConfiguration> bootstrap) {
      bootstrap.addBundle(new AssetsBundle("/assets"));
    }

    @Override
    public void run(CoralApiConfiguration configuration,
                    Environment environment) {
      final String coralIor = configuration.getCoralIor();
      final String coralConfigUrl = configuration.getCoralConfigUrl();
      final TokenConfiguration[] tokens = configuration.getAuthTokensConfiguration().getTokens();
      ConcurrentHashMap<String, TokenConfiguration> sessionTokens = new ConcurrentHashMap<String, TokenConfiguration>();

      environment.jersey().register(new BasicAuthProvider<User>(new SimpleAuthenticator(tokens, sessionTokens, coralIor, coralConfigUrl ), "REALM STRING"));
      environment.jersey().register(new CoralApiVersionResource());
      environment.jersey().register(new CoralApiWhoAmIResource());
      environment.jersey().register(new CoralApiAuthTokenResource(coralIor, coralConfigUrl, sessionTokens));
      environment.jersey().register(new CoralApiMemberResource(coralIor, coralConfigUrl));
      environment.jersey().register(new CoralApiLabRoleResource(coralIor, coralConfigUrl));
      environment.jersey().register(new CoralApiProjectResource(coralIor, coralConfigUrl));
      environment.jersey().register(new CoralApiAccountResource(coralIor, coralConfigUrl));
      environment.jersey().register(new CoralApiProjectMembershipResource(coralIor, coralConfigUrl));
      environment.jersey().register(new CoralApiProjectsResource(coralIor, coralConfigUrl));
      environment.jersey().register(new CoralApiPasswordResetResource(coralIor, coralConfigUrl));
    }

}
