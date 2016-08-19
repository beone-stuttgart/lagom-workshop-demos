package models;

import play.data.validation.Constraints;

/**
 * Form model for sending presents.
 */
public class NewPresentFormModel {
  @Constraints.Required
  private String organisatorName;
  @Constraints.Required
  private String organisatorMail;
  @Constraints.Required
  private String deadline;
  @Constraints.Required
  private String title;
  @Constraints.Required
  private String description;
  private String wishlistUrl;

  public String getOrganisatorName() {
    return organisatorName;
  }

  public void setOrganisatorName(final String organisatorName) {
    this.organisatorName = organisatorName;
  }

  public String getOrganisatorMail() {
    return organisatorMail;
  }

  public void setOrganisatorMail(final String organisatorMail) {
    this.organisatorMail = organisatorMail;
  }

  public String getDeadline() {
    return deadline;
  }

  public void setDeadline(final String deadline) {
    this.deadline = deadline;
  }


  public String getTitle() {
    return title;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  public String getWishlistUrl() {
    return wishlistUrl;
  }

  public void setWishlistUrl(final String wishlistUrl) {
    this.wishlistUrl = wishlistUrl;
  }
}
