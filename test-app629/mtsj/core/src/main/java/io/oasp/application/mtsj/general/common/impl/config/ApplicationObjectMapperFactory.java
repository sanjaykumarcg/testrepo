package io.oasp.application.mtsj.general.common.impl.config;

import javax.inject.Named;

import org.springframework.data.domain.Pageable;
import org.springframework.security.web.csrf.CsrfToken;

import com.devonfw.module.json.common.base.ObjectMapperFactory;
import com.fasterxml.jackson.databind.module.SimpleModule;

import io.oasp.application.mtsj.general.common.api.to.PageableJsonDeserializer;

/**
 * The MappingFactory class to resolve polymorphic conflicts within the issue_6 application.
 */
@Named("ApplicationObjectMapperFactory")
public class ApplicationObjectMapperFactory extends ObjectMapperFactory {

  /**
   * The constructor.
   */
  public ApplicationObjectMapperFactory() {

    super();
    // register polymorphic mapping here - see
    // https://github.com/oasp-forge/oasp4j-wiki/wiki/guide-json#json-and-inheritance
    getExtensionModule().addAbstractTypeMapping(CsrfToken.class, CsrfTokenImpl.class);
    // register Pageable deserializer
    SimpleModule module = getExtensionModule();
    module.addDeserializer(Pageable.class, new PageableJsonDeserializer());
  }
}
