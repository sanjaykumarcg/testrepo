package io.oasp.application.mtsj.bookingmanagement.logic.api.to;

import java.sql.Timestamp;

import com.devonfw.module.basic.common.api.query.StringSearchConfigTo;
import com.devonfw.module.jpa.common.api.to.SearchCriteriaTo;

import io.oasp.application.mtsj.general.common.api.to.AbstractSearchCriteriaTo;

/**
 * This is the {@link SearchCriteriaTo search criteria} {@link net.sf.mmm.util.transferobject.api.TransferObject TO}
 * used to find {@link io.oasp.application.mtsj.bookingmanagement.common.api.InvitedGuest}s.
 */
// @JsonDeserialize(using = PageableJsonDeserializer.class)
public class InvitedGuestSearchCriteriaTo extends AbstractSearchCriteriaTo {

  private static final long serialVersionUID = 1L;

  private Long bookingId;

  private String guestToken;

  private String email;

  private Boolean accepted;

  private Timestamp modificationDate;

  private StringSearchConfigTo guestTokenOption;

  private StringSearchConfigTo emailOption;

  /**
   * The constructor.
   */
  public InvitedGuestSearchCriteriaTo() {

    super();
  }

  public Long getBookingId() {

    return this.bookingId;
  }

  public void setBookingId(Long bookingId) {

    this.bookingId = bookingId;
  }

  public String getGuestToken() {

    return this.guestToken;
  }

  public void setGuestToken(String guestToken) {

    this.guestToken = guestToken;
  }

  public String getEmail() {

    return this.email;
  }

  public void setEmail(String email) {

    this.email = email;
  }

  public Boolean getAccepted() {

    return this.accepted;
  }

  public void setAccepted(Boolean accepted) {

    this.accepted = accepted;
  }

  public Timestamp getModificationDate() {

    return this.modificationDate;
  }

  public void setModificationDate(Timestamp modificationDate) {

    this.modificationDate = modificationDate;
  }

  /**
   * @return guestTokenOption
   */
  public StringSearchConfigTo getGuestTokenOption() {

    return this.guestTokenOption;
  }

  /**
   * @param guestTokenOption new value of {@link #getguestTokenOption}.
   */
  public void setGuestTokenOption(StringSearchConfigTo guestTokenOption) {

    this.guestTokenOption = guestTokenOption;
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
