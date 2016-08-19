package com.beone.lagom.wishlist.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;

import javax.annotation.concurrent.Immutable;
import java.util.List;

@Immutable
@JsonDeserialize
public class WishlistResponse {
  public final String presentId;
  public final boolean success;
  public final List<WishlistItem> items;

  @JsonCreator
  public WishlistResponse(final String presentId, final boolean success, final List<WishlistItem> items) {
    this.presentId = presentId;
    this.success = success;
    this.items = items;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper("WishlistResponse")
            .add("presentId", presentId)
            .add("recipients", success)
            .add("items", items)
            .toString();
  }
}
