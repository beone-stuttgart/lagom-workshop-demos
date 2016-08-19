package com.beone.lagom.wishlist.impl.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import com.lightbend.lagom.serialization.CompressedJsonable;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.util.List;

@SuppressWarnings("serial")
@Immutable
@JsonDeserialize
public class WishlistWorldState implements CompressedJsonable {
  public final String presentId;
  public final String wishlistUrl;
  public final List<WishlistItem> items;

  @JsonCreator
  public WishlistWorldState(final String presentId, final String wishlistUrl, final List<WishlistItem> items) {
    this.presentId = presentId;
    this.wishlistUrl = wishlistUrl;
    this.items = items;
  }

  @Override
  public boolean equals(@Nullable Object another) {
    if (this == another)
      return true;
    return another instanceof WishlistWorldState && equalTo((WishlistWorldState) another);
  }

  private boolean equalTo(WishlistWorldState another) {
    return presentId.equals(another.presentId)
            && wishlistUrl.equals(another.wishlistUrl)
            && items.equals(another.items);
  }

  @Override
  public int hashCode() {
    int h = 31;
    h = h * 17 + presentId.hashCode();
    h = h * 17 + wishlistUrl.hashCode();
    return h;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper("WishlistWorldState")
            .add("presentId", presentId)
            .add("wishlistUrl", wishlistUrl)
            .add("items", items)
            .toString();
  }
}
