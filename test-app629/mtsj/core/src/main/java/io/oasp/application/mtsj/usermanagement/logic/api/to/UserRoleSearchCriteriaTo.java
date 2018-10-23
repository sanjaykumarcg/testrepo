package io.oasp.application.mtsj.usermanagement.logic.api.to;

import com.devonfw.module.basic.common.api.query.StringSearchConfigTo;
import com.devonfw.module.jpa.common.api.to.SearchCriteriaTo;

import io.oasp.application.mtsj.general.common.api.to.AbstractSearchCriteriaTo;

/**
 * This is the {@link SearchCriteriaTo search criteria} {@link net.sf.mmm.util.transferobject.api.TransferObject TO}
 * used to find {@link io.oasp.application.mtsj.usermanagement.common.api.UserRole}s.
 */
// @JsonDeserialize(using = PageableJsonDeserializer.class)
public class UserRoleSearchCriteriaTo extends AbstractSearchCriteriaTo {

  private static final long serialVersionUID = 1L;

  private String name;

  private Boolean active;

  private StringSearchConfigTo nameOption;

  /**
   * The constructor.
   */
  public UserRoleSearchCriteriaTo() {

    super();
  }

  public String getName() {

    return this.name;
  }

  public void setName(String name) {

    this.name = name;
  }

  public Boolean getActive() {

    return this.active;
  }

  public void setActive(Boolean active) {

    this.active = active;
  }

  /**
   * @return nameOption
   */
  public StringSearchConfigTo getNameOption() {

    return this.nameOption;
  }

  /**
   * @param nameOption new value of {@link #getnameOption}.
   */
  public void setNameOption(StringSearchConfigTo nameOption) {

    this.nameOption = nameOption;
  }

}
