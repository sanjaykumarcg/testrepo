package com.devonfw.application.mtsj.imagemanagement.common.api.datatype;

import com.devonfw.application.mtsj.imagemanagement.dataaccess.api.ImageEntity;

/**
 * Enum for the type of the content of the {@link ImageEntity}
 *
 */
public enum ContentType {
  /**
   * defines base64 content
   */
  Binary,

  /**
   * defines the content as a resource url
   */
  Url
}
