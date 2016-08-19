package com.beone.lagom.organize.api.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.annotation.concurrent.Immutable;
import java.time.LocalDate;

@Immutable
@JsonDeserialize
public class PresentAdminInfo {
  public final String securityToken;
  public final String organisator;
  public final String title;
  public final String description;
  public final LocalDate deadline;

  @JsonCreator
  public PresentAdminInfo(final String securityToken, final String organisator, final String title, final String description, final LocalDate deadline) {
    this.securityToken = securityToken;
    this.organisator = organisator;
    this.title = title;
    this.description = description;
    this.deadline = deadline;
  }
}
