package sampleapp629.app629;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.EndpointAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityFilterAutoConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import io.oasp.module.jpa.dataaccess.api.AdvancedRevisionEntity;

/**
 * {@link SpringBootApplication} for running this batch.
 */
@SpringBootApplication(exclude = { EndpointAutoConfiguration.class, SecurityAutoConfiguration.class,
SecurityFilterAutoConfiguration.class, })
@EntityScan(basePackages = { "sampleapp629.app629" }, basePackageClasses = { AdvancedRevisionEntity.class })
@EnableGlobalMethodSecurity(jsr250Enabled = false)
public class SpringBootBatchApp {

  /**
   * Entry point for spring-boot based app
   *
   * @param args - arguments
   */
  public static void main(String[] args) {

    SpringApplication.run(SpringBootBatchApp.class, args);
  }
}
