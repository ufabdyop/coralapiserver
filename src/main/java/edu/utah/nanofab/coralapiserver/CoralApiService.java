package edu.utah.nanofab.coralapiserver;


import java.util.EnumSet;
import java.util.concurrent.ConcurrentHashMap;

import io.dropwizard.Application;
import io.dropwizard.auth.basic.BasicAuthProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.assets.AssetsBundle;

import org.eclipse.jetty.servlets.CrossOriginFilter;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration.Dynamic;

import edu.utah.nanofab.coralapiserver.auth.SimpleAuthenticator;
import edu.utah.nanofab.coralapiserver.auth.User;
import edu.utah.nanofab.coralapiserver.resources.CoralApiAccountResource;
import edu.utah.nanofab.coralapiserver.resources.CoralApiAuthTokenResource;
import edu.utah.nanofab.coralapiserver.resources.CoralApiCheckKeyResource;
import edu.utah.nanofab.coralapiserver.resources.CoralApiEntryPointResource;
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
      configureCors(environment);

      environment.jersey().register(new BasicAuthProvider<User>(new SimpleAuthenticator(tokens, sessionTokens, coralIor, coralConfigUrl ), "REALM STRING"));
      environment.jersey().register(new CoralApiEntryPointResource());
      environment.jersey().register(new CoralApiVersionResource());
      environment.jersey().register(new CoralApiWhoAmIResource());
      environment.jersey().register(new CoralApiCheckKeyResource(coralIor, coralConfigUrl));
      environment.jersey().register(new CoralApiAuthTokenResource(coralIor, coralConfigUrl, sessionTokens));
      environment.jersey().register(new CoralApiMemberResource(coralIor, coralConfigUrl));
      environment.jersey().register(new CoralApiLabRoleResource(coralIor, coralConfigUrl));
      environment.jersey().register(new CoralApiProjectResource(coralIor, coralConfigUrl));
      environment.jersey().register(new CoralApiAccountResource(coralIor, coralConfigUrl));
      environment.jersey().register(new CoralApiProjectMembershipResource(coralIor, coralConfigUrl));
      environment.jersey().register(new CoralApiProjectsResource(coralIor, coralConfigUrl));
      environment.jersey().register(new CoralApiPasswordResetResource(coralIor, coralConfigUrl));
    }

   private void configureCors(Environment environment) {
     Dynamic filter = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
     filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
     filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,POST,DELETE,OPTIONS");
     filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
     filter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
     filter.setInitParameter("allowedHeaders", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
     filter.setInitParameter("allowCredentials", "true");
   }

}
