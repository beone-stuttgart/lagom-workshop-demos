package com.beone.lagom.organize.impl.models;

import com.beone.lagom.organize.api.models.PresentCreateRequest;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.Jsonable;

import java.time.LocalDate;

public interface PresentCommand extends Jsonable {

  /**
   * command for creating a present.
   */
  class CreatePresent implements PresentCommand, PersistentEntity.ReplyType<PresentWorldState> {
    public final String id;
    public final String securityToken;
    public final String organisatorName;
    public final String organisatorMail;
    public final LocalDate deadline;
    public final String title;
    public final String description;

    @JsonCreator
    public CreatePresent(final String id, final String securityToken, final String organisatorName, final String organisatorMail,
            final LocalDate deadline, final String title, final String description) {
      this.id = Preconditions.checkNotNull(id);
      this.securityToken = Preconditions.checkNotNull(securityToken);
      this.organisatorName = Preconditions.checkNotNull(organisatorName);
      this.organisatorMail = Preconditions.checkNotNull(organisatorMail);
      this.deadline = Preconditions.checkNotNull(deadline);
      this.title = Preconditions.checkNotNull(title);
      this.description = Preconditions.checkNotNull(description);
    }

    public CreatePresent(final String generatedId, final String securityKey, final PresentCreateRequest request) {
      this(generatedId, securityKey, request.organisatorName, request.organisatorMail, request.deadline, request.title, request.description);
    }

    @Override
    public boolean equals(final Object other) {
      return other == this || other instanceof CreatePresent && isEqualTo((CreatePresent) other);
    }

    private boolean isEqualTo(final CreatePresent other) {
      return organisatorMail.equals(other.organisatorMail) && organisatorName.equals(other.organisatorName)
              && deadline.equals(other.deadline) && title.equals(other.title) && description.equals(other.description);
    }

    @Override
    public int hashCode() {
      int result = 31;
      result = result * 17 + organisatorMail.hashCode();
      result = result * 17 + organisatorName.hashCode();
      result = result * 17 + deadline.hashCode();
      result = result * 17 + title.hashCode();
      result = result * 17 + description.hashCode();
      return result;
    }
  }

  /**
   * command for read access to a present
   */
  class GetPresentData implements PresentCommand, PersistentEntity.ReplyType<PresentWorldState> {
    public static GetPresentData INSTANCE = new GetPresentData();

    @Override
    public boolean equals(final Object other) {
      return other instanceof GetPresentData;
    }
  }

}
