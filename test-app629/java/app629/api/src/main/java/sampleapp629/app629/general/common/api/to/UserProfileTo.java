package sampleapp629.app629.general.common.api.to;

import sampleapp629.app629.general.common.api.UserProfile;
import io.oasp.module.basic.common.api.to.AbstractTo;

/**
 * Implementation of {@link UserProfile} as {AbstractTo TO}.
 */
public class UserProfileTo extends AbstractTo implements UserProfile {

  private static final long serialVersionUID = 1L;

  private String login;

  /**
   * The constructor.
   */
  public UserProfileTo() {

    super();
  }

  @Override
  public String getLogin() {

    return this.login;
  }

  /**
   * @param login the new {@link #getLogin() login}.
   */
  public void setLogin(String login) {

    this.login = login;
  }

}
