package edu.utah.nanofab.coralapiserver;


import edu.utah.nanofab.coralapi.CoralAPIPool;
import java.util.EnumSet;
import java.util.concurrent.ConcurrentHashMap;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.assets.AssetsBundle;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

import org.eclipse.jetty.servlets.CrossOriginFilter;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration.Dynamic;

import edu.utah.nanofab.coralapiserver.auth.SimpleAuthenticator;
import edu.utah.nanofab.coralapiserver.auth.User;
import edu.utah.nanofab.coralapiserver.resources.CoralApiAccountResource;
import edu.utah.nanofab.coralapiserver.resources.CoralApiAuthTokenResource;
import edu.utah.nanofab.coralapiserver.resources.CoralApiCheckKeyResource;
import edu.utah.nanofab.coralapiserver.resources.CoralApiDisableResource;
import edu.utah.nanofab.coralapiserver.resources.CoralApiEnableResource;
import edu.utah.nanofab.coralapiserver.resources.CoralApiEntryPointResource;
import edu.utah.nanofab.coralapiserver.resources.CoralApiEquipmentRoleResource;
import edu.utah.nanofab.coralapiserver.resources.CoralApiLabRoleResource;
import edu.utah.nanofab.coralapiserver.resources.CoralApiMachineResource;
import edu.utah.nanofab.coralapiserver.resources.CoralApiMemberProjectsResource;
import edu.utah.nanofab.coralapiserver.resources.CoralApiMemberResource;
import edu.utah.nanofab.coralapiserver.resources.CoralApiPasswordResetResource;
import edu.utah.nanofab.coralapiserver.resources.CoralApiProjectMembershipResource;
import edu.utah.nanofab.coralapiserver.resources.CoralApiProjectRoleResource;
import edu.utah.nanofab.coralapiserver.resources.CoralApiProjectsResource;
import edu.utah.nanofab.coralapiserver.resources.CoralApiReservationResource;
import edu.utah.nanofab.coralapiserver.resources.CoralApiRunDataDefinitionResource;
import edu.utah.nanofab.coralapiserver.resources.CoralApiRunDataResource;
import edu.utah.nanofab.coralapiserver.resources.CoralApiVersionResource;
import edu.utah.nanofab.coralapiserver.resources.CoralApiWhoAmIResource;
import io.dropwizard.auth.AuthFactory;
import io.dropwizard.auth.basic.BasicAuthFactory;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import org.glassfish.jersey.filter.LoggingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CoralApiService extends Application<CoralApiConfiguration> {
    public static void main(String[] args) throws Exception {
        new CoralApiService().run(args);
    }

    @Override
    public void initialize(Bootstrap<CoralApiConfiguration> bootstrap) {
      bootstrap.addBundle(new AssetsBundle("/assets"));
      addSwaggerBundleConditionally(bootstrap);
    }
    
    /**
     * add the /swagger endpoints if environment variable ENABLE_SWAGGER is equal to "1"
     */
    private void addSwaggerBundleConditionally(Bootstrap<CoralApiConfiguration> bootstrap) {
    	if ("1".equalsIgnoreCase(System.getenv("ENABLE_SWAGGER"))) {
    		System.out.println("Adding swagger bundle");
	        bootstrap.addBundle(new SwaggerBundle<CoralApiConfiguration>() {
	            @Override
	            public SwaggerBundleConfiguration getSwaggerBundleConfiguration(CoralApiConfiguration configuration) {
	                int port = 8080;
	                String hostname = System.getenv("APIHOST");
	                if (hostname == null) {
	                    hostname = "localhost";
	                }
	                String portString = System.getenv("APIPORT");
	                if (portString != null) {
	                    try {
	                        port = Integer.valueOf(portString);
	                    } catch (Exception e) {
	                        //ignore bad port and use default one
	                    }
	                }
	                
	                return new SwaggerBundleConfiguration(hostname, port);
	            }
	        });
    	} else {
    		System.out.print("Not adding swagger bundle. \"ENABLE_SWAGGER\" env var.");
    		System.out.print("***");
    		System.out.print(System.getenv("ENABLE_SWAGGER")); 
    		System.out.println("***");
    	}
    }


    @Override
    public void run(CoralApiConfiguration configuration,
                    Environment environment) {
      final String coralConfigUrl = configuration.getCoralConfigUrl();
      final TokenConfiguration[] tokens = configuration.getAuthTokensConfiguration().getTokens();
      ConcurrentHashMap<String, TokenConfiguration> sessionTokens = new ConcurrentHashMap<String, TokenConfiguration>();
      CoralAPIPool apiPool = CoralAPIPool.getInstance(coralConfigUrl);
      apiPool.setMaxAgeInSeconds(300);
      configureCors(environment);

      environment.jersey().register(new CoralApiReservationResource(apiPool));
      
      environment.jersey().register(
        AuthFactory.binder(
            new BasicAuthFactory<User>(
                new SimpleAuthenticator(tokens, sessionTokens, apiPool ),
                "CoralAPIServer Realm",
                User.class)));      
      
      environment.jersey().register(new CoralApiEntryPointResource());
      environment.jersey().register(new CoralApiVersionResource());
      environment.jersey().register(new CoralApiWhoAmIResource());
      environment.jersey().register(new CoralApiCheckKeyResource( apiPool ));
      environment.jersey().register(new CoralApiAuthTokenResource( apiPool, sessionTokens));
      environment.jersey().register(new CoralApiMemberResource( apiPool ));
      environment.jersey().register(new CoralApiEnableResource( apiPool));
      environment.jersey().register(new CoralApiDisableResource( apiPool));
      environment.jersey().register(new CoralApiMachineResource( apiPool));
      environment.jersey().register(new CoralApiLabRoleResource( apiPool));
      environment.jersey().register(new CoralApiMemberProjectsResource( apiPool));
      environment.jersey().register(new CoralApiEquipmentRoleResource( apiPool));
      environment.jersey().register(new CoralApiProjectRoleResource( apiPool));
      environment.jersey().register(new CoralApiAccountResource( apiPool));
      environment.jersey().register(new CoralApiProjectMembershipResource( apiPool));
      environment.jersey().register(new CoralApiProjectsResource( apiPool));
      environment.jersey().register(new CoralApiPasswordResetResource( apiPool));
      environment.jersey().register(new CoralApiRunDataDefinitionResource( apiPool)); 
      environment.jersey().register(new CoralApiRunDataResource( apiPool)); 

      //LOGGING?
      java.util.logging.Logger requestLogger = java.util.logging.Logger.getLogger(LoggingFilter.class.getName());
      environment.jersey().register(new LoggingFilter(
                     (java.util.logging.Logger) requestLogger,
                     true)
                 );      
      //environment.jersey().register(new LoggingFeature(logger, LoggingFeature.Verbosity.PAYLOAD_ANY));

      
    }

   private void configureCors(Environment environment) {
     Dynamic filter = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
     filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
     filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,POST,DELETE,OPTIONS,HEAD");
     filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
     filter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
     filter.setInitParameter("allowedHeaders", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
     filter.setInitParameter("allowCredentials", "true");
   }

}
