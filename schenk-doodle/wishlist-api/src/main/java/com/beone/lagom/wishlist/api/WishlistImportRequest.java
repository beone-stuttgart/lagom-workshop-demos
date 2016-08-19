package com.beone.lagom.wishlist.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.annotation.concurrent.Immutable;

@Immutable
@JsonDeserialize
public class WishlistImportRequest {
  public final String presentId;
  public final String wishlistUrl;

  @JsonCreator
  public WishlistImportRequest(final String presentId, final String wishlistUrl) {
    this.presentId = presentId;
    this.wishlistUrl = wishlistUrl;
  }
}
