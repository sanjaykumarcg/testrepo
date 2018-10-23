package io.oasp.application.mtsj.dishmanagement.logic.api.to;

import com.devonfw.module.basic.common.api.query.StringSearchConfigTo;
import com.devonfw.module.jpa.common.api.to.SearchCriteriaTo;

import io.oasp.application.mtsj.general.common.api.to.AbstractSearchCriteriaTo;

/**
 * This is the {@link SearchCriteriaTo search criteria} {@link net.sf.mmm.util.transferobject.api.TransferObject TO}
 * used to find {@link io.oasp.application.mtsj.dishmanagement.common.api.Category}s.
 *
 */
// @JsonDeserialize(using = PageableJsonDeserializer.class)
public class CategorySearchCriteriaTo extends AbstractSearchCriteriaTo {

  private static final long serialVersionUID = 1L;

  private String name;

  private String description;

  private Integer showOrder;

  private StringSearchConfigTo nameOption;

  private StringSearchConfigTo descriptionOption;

  /**
   * The constructor.
   */
  public CategorySearchCriteriaTo() {

    super();
  }

  public String getName() {

    return this.name;
  }

  public void setName(String name) {

    this.name = name;
  }

  public String getDescription() {

    return this.description;
  }

  public void setDescription(String description) {

    this.description = description;
  }

  public Integer getShowOrder() {

    return this.showOrder;
  }

  public void setShowOrder(Integer showOrder) {

    this.showOrder = showOrder;
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

  /**
   * @return descriptionOption
   */
  public StringSearchConfigTo getDescriptionOption() {

    return this.descriptionOption;
  }

  /**
   * @param descriptionOption new value of {@link #getdescriptionOption}.
   */
  public void setDescriptionOption(StringSearchConfigTo descriptionOption) {

    this.descriptionOption = descriptionOption;
  }

}
