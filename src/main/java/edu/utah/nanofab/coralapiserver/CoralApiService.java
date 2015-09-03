package edu.utah.nanofab.coralapiserver;


import java.util.EnumSet;
import java.util.concurrent.ConcurrentHashMap;

import io.dropwizard.Application;
import io.dropwizard.auth.basic.BasicAuthProvider;
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
import edu.utah.nanofab.coralapiserver.resources.CoralApiLabRoleResource;
import edu.utah.nanofab.coralapiserver.resources.CoralApiMachineResource;
import edu.utah.nanofab.coralapiserver.resources.CoralApiMemberResource;
import edu.utah.nanofab.coralapiserver.resources.CoralApiPasswordResetResource;
import edu.utah.nanofab.coralapiserver.resources.CoralApiProjectMembershipResource;
import edu.utah.nanofab.coralapiserver.resources.CoralApiProjectsResource;
import edu.utah.nanofab.coralapiserver.resources.CoralApiReservationResource;
import edu.utah.nanofab.coralapiserver.resources.CoralApiVersionResource;
import edu.utah.nanofab.coralapiserver.resources.CoralApiWhoAmIResource;


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
      configureCors(environment);

      environment.jersey().register(new BasicAuthProvider<User>(new SimpleAuthenticator(tokens, sessionTokens, coralConfigUrl ), "CoralAPIServer Realm"));
      environment.jersey().register(new CoralApiEntryPointResource());
      environment.jersey().register(new CoralApiVersionResource());
      environment.jersey().register(new CoralApiWhoAmIResource());
      environment.jersey().register(new CoralApiCheckKeyResource( coralConfigUrl));
      environment.jersey().register(new CoralApiAuthTokenResource( coralConfigUrl, sessionTokens));
      environment.jersey().register(new CoralApiMemberResource( coralConfigUrl));
      environment.jersey().register(new CoralApiEnableResource( coralConfigUrl));
      environment.jersey().register(new CoralApiDisableResource( coralConfigUrl));
      environment.jersey().register(new CoralApiReservationResource( coralConfigUrl));
      environment.jersey().register(new CoralApiMachineResource( coralConfigUrl));
      environment.jersey().register(new CoralApiLabRoleResource( coralConfigUrl));
      environment.jersey().register(new CoralApiAccountResource( coralConfigUrl));
      environment.jersey().register(new CoralApiProjectMembershipResource( coralConfigUrl));
      environment.jersey().register(new CoralApiProjectsResource( coralConfigUrl));
      environment.jersey().register(new CoralApiPasswordResetResource( coralConfigUrl));
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
