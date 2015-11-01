package com.juanpabloprado.images;

import com.juanpabloprado.images.resources.ClientResource;
import com.juanpabloprado.images.resources.FileResource;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import java.util.EnumSet;
import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

/**
 * Hello world!
 */
public class App extends Application<ImagesConfiguration> {
  public static void main(String[] args) throws Exception {
    new App().run(args);
  }

  @Override
  public void initialize(Bootstrap<ImagesConfiguration> bootstrap) {
    final AssetsBundle assetBundle = new AssetsBundle("/assets/css", "/css", null, "css");
    final ViewBundle viewBundle = new ViewBundle();
    bootstrap.addBundle(assetBundle);
    bootstrap.addBundle(viewBundle);
  }

  @Override
  public void run(ImagesConfiguration configuration, Environment environment) throws Exception {
    configureCors(environment);
    environment.jersey().register(MultiPartFeature.class);
    environment.jersey().register(new FileResource(configuration.getDest()));
    environment.jersey().register(new ClientResource());
  }

  private void configureCors(Environment environment) {
    FilterRegistration.Dynamic filter = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
    filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
    filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,POST,DELETE,OPTIONS");
    filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
    filter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
    filter.setInitParameter("allowedHeaders", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
    filter.setInitParameter("allowCredentials", "true");
  }
}
