package sampleapp629.app629.general.common.impl.config;

import javax.inject.Named;

import org.springframework.security.web.csrf.CsrfToken;

import io.oasp.module.json.common.base.ObjectMapperFactory;

/**
 * The MappingFactory class to resolve polymorphic conflicts within the app629 application.
 */
@Named("ApplicationObjectMapperFactory")
public class ApplicationObjectMapperFactory extends ObjectMapperFactory {

  /**
   * The constructor.
   */
  public ApplicationObjectMapperFactory() {

    super();
    // register polymorphic mapping here - see https://github.com/oasp-forge/oasp4j-wiki/wiki/guide-json#json-and-inheritance
    getExtensionModule().addAbstractTypeMapping(CsrfToken.class, CsrfTokenImpl.class);
  }
}
