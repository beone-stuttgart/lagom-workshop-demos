package com.beone.lagom.organize.api.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.annotation.concurrent.Immutable;

@Immutable
@JsonDeserialize
public class PresentCreateResponse {
  public final String key;
  public final String securityToken;

  @JsonCreator
  public PresentCreateResponse(final String key, final String securityToken) {
    this.key = key;
    this.securityToken = securityToken;
  }
}
