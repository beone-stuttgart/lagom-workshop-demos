package com.beone.lagom.organize.impl.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import com.lightbend.lagom.serialization.CompressedJsonable;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.time.LocalDate;

@SuppressWarnings("serial")
@Immutable
@JsonDeserialize
public class PresentWorldState implements CompressedJsonable {
  public final String presentId;
  public final String presentAdminSecret;

  public final String organisatorName;
  public final String organisatorMail;
  public final LocalDate deadline;
  public final String title;
  public final String description;

  @JsonCreator
  public PresentWorldState(final String presentId, final String presentAdminSecret, final String organisatorName,
          final String organisatorMail, final LocalDate deadline, final String title, final String description) {
    this.presentId = presentId;
    this.presentAdminSecret = presentAdminSecret;
    this.organisatorName = organisatorName;
    this.organisatorMail = organisatorMail;
    this.deadline = deadline;
    this.title = title;
    this.description = description;
  }

  @Override
  public boolean equals(@Nullable Object another) {
    return this == another || another instanceof PresentWorldState && equalTo((PresentWorldState) another);
  }

  private boolean equalTo(PresentWorldState another) {
    return presentId.equals(another.presentId)
        && presentAdminSecret.equals(another.presentAdminSecret)
        && organisatorName.equals(another.organisatorName)
        && organisatorMail.equals(another.organisatorMail)
        && deadline.equals(another.deadline)
        && title.equals(another.title)
        && description.equals(another.description);
  }

  @Override
  public int hashCode() {
    int h = 31;
    h = h * 17 + presentId.hashCode();
    h = h * 17 + presentAdminSecret.hashCode();
    h = h * 17 + organisatorName.hashCode();
    h = h * 17 + organisatorMail.hashCode();
    h = h * 17 + deadline.hashCode();
    h = h * 17 + title.hashCode();
    h = h * 17 + description.hashCode();
    return h;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper("WishlistWorldState")
            .add("presentId", presentId)
            .add("presentAdminSecret", presentAdminSecret)
            .add("organisatorName", organisatorName)
            .add("organisatorMail", organisatorMail)
            .add("deadline", deadline)
            .add("title", title)
            .add("description", description)
            .toString();
  }
}
