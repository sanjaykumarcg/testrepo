package io.oasp.application.mtsj.usermanagement.logic.api.to;

import com.devonfw.module.basic.common.api.query.StringSearchConfigTo;
import com.devonfw.module.jpa.common.api.to.SearchCriteriaTo;

import io.oasp.application.mtsj.general.common.api.to.AbstractSearchCriteriaTo;

/**
 * This is the {@link SearchCriteriaTo search criteria} {@link net.sf.mmm.util.transferobject.api.TransferObject TO}
 * used to find {@link io.oasp.application.mtsj.usermanagement.common.api.User}s.
 *
 */
// @JsonDeserialize(using = PageableJsonDeserializer.class)
public class UserSearchCriteriaTo extends AbstractSearchCriteriaTo {

  private static final long serialVersionUID = 1L;

  private String username;

  private String password;

  private String email;

  private Long userRoleId;

  private StringSearchConfigTo usernameOption;

  private StringSearchConfigTo passwordOption;

  private StringSearchConfigTo emailOption;

  /**
   * The constructor.
   */
  public UserSearchCriteriaTo() {

    super();
  }

  public String getUsername() {

    return this.username;
  }

  public void setUsername(String username) {

    this.username = username;
  }

  public String getPassword() {

    return this.password;
  }

  public void setPassword(String password) {

    this.password = password;
  }

  public String getEmail() {

    return this.email;
  }

  public void setEmail(String email) {

    this.email = email;
  }

  public Long getUserRoleId() {

    return this.userRoleId;
  }

  public void setUserRoleId(Long userRoleId) {

    this.userRoleId = userRoleId;
  }

  /**
   * @return usernameOption
   */
  public StringSearchConfigTo getUsernameOption() {

    return this.usernameOption;
  }

  /**
   * @param usernameOption new value of {@link #getusernameOption}.
   */
  public void setUsernameOption(StringSearchConfigTo usernameOption) {

    this.usernameOption = usernameOption;
  }

  /**
   * @return passwordOption
   */
  public StringSearchConfigTo getPasswordOption() {

    return this.passwordOption;
  }

  /**
   * @param passwordOption new value of {@link #getpasswordOption}.
   */
  public void setPasswordOption(StringSearchConfigTo passwordOption) {

    this.passwordOption = passwordOption;
  }

  /**
   * @return emailOption
   */
  public StringSearchConfigTo getEmailOption() {

    return this.emailOption;
  }

  /**
   * @param emailOption new value of {@link #getemailOption}.
   */
  public void setEmailOption(StringSearchConfigTo emailOption) {

    this.emailOption = emailOption;
  }

}
