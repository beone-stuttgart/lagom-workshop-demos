package com.beone.lagom.organize.api.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.annotation.concurrent.Immutable;
import java.time.LocalDate;

@Immutable
@JsonDeserialize
public class PresentCreateRequest {
  public final String organisatorName;
  public final String organisatorMail;
  public final String title;
  public final LocalDate deadline;
  public final String description;

  public PresentCreateRequest(final String organisator, final String mail, final String title, final LocalDate deadline, final String description) {
    this.organisatorName = organisator;
    this.organisatorMail = mail;
    this.title = title;
    this.deadline = deadline;
    this.description = description;
  }
}
