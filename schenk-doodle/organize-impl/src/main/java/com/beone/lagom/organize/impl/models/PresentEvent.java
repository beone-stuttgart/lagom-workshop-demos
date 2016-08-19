package com.beone.lagom.organize.impl.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import com.lightbend.lagom.serialization.Jsonable;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.time.LocalDate;

public interface PresentEvent extends Jsonable {

  /**
   * implementation for creation event for a present.
   */
  @Immutable
  @JsonDeserialize
  @SuppressWarnings("serial")
  class PresentCreatedEvent implements PresentEvent {
    public final String id;
    public final String securityToken;
    public final String organisatorName;
    public final String organisatorMail;
    public final LocalDate deadline;
    public final String title;
    public final String description;

    public PresentCreatedEvent(final String id, final String securityToken, final String organisatorName, final String organisatorMail,
            final LocalDate deadline, final String title, final String description) {
      this.id = id;
      this.securityToken = securityToken;
      this.organisatorName = organisatorName;
      this.organisatorMail = organisatorMail;
      this.deadline = deadline;
      this.title = title;
      this.description = description;
    }

    @Override
    public boolean equals(@Nullable Object another) {
      if (this == another)
        return true;
      return another instanceof PresentCreatedEvent && equalTo((PresentCreatedEvent) another);
    }

    private boolean equalTo(PresentCreatedEvent another) {
      return organisatorName.equals(another.organisatorName)
              && organisatorMail.equals(another.organisatorMail)
              && deadline.equals(another.deadline)
              && title.equals(another.title)
              && description.equals(another.description);
    }

    @Override
    public int hashCode() {
      int h = 31;
      h = h * 17 + organisatorName.hashCode();
      h = h * 17 + organisatorMail.hashCode();
      h = h * 17 + deadline.hashCode();
      h = h * 17 + title.hashCode();
      h = h * 17 + description.hashCode();
      return h;
    }

    @Override
    public String toString() {
      return MoreObjects.toStringHelper("PresentCreatedEvent")
              .add("organisatorName", organisatorName)
              .add("organisatorMail", organisatorMail)
              .add("deadline", deadline)
              .add("title", title)
              .add("description", description)
              .toString();
    }
  }

}
