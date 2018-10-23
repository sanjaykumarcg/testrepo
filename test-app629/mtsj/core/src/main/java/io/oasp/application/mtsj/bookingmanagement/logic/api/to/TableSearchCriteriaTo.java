package io.oasp.application.mtsj.bookingmanagement.logic.api.to;

import com.devonfw.module.jpa.common.api.to.SearchCriteriaTo;

import io.oasp.application.mtsj.general.common.api.to.AbstractSearchCriteriaTo;

/**
 * This is the {@link SearchCriteriaTo search criteria} {@link net.sf.mmm.util.transferobject.api.TransferObject TO}
 * used to find {@link io.oasp.application.mtsj.bookingmanagement.common.api.Table}s.
 */
// @JsonDeserialize(using = PageableJsonDeserializer.class)
public class TableSearchCriteriaTo extends AbstractSearchCriteriaTo {

  private static final long serialVersionUID = 1L;

  private Integer seatsNumber;

  /**
   * The constructor.
   */
  public TableSearchCriteriaTo() {

    super();
  }

  public Integer getSeatsNumber() {

    return this.seatsNumber;
  }

  public void setSeatsNumber(Integer seatsNumber) {

    this.seatsNumber = seatsNumber;
  }

}
