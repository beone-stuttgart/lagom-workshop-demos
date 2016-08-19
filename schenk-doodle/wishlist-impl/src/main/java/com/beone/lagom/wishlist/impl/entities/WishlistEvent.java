package com.beone.lagom.wishlist.impl.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.serialization.Jsonable;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.util.List;

public interface WishlistEvent extends Jsonable {


  /**
   * An event that represents a change in greeting message.
   */
  @SuppressWarnings("serial")
  @Immutable
  @JsonDeserialize
  final class WishlistChanged implements WishlistEvent {
    public final String presentId;
    public final String wishlistUrl;
    public final List<WishlistItem> items;

    @JsonCreator
    public WishlistChanged(final String presentId, final String wishlistUrl, final List<WishlistItem> items) {
      this.presentId = Preconditions.checkNotNull(presentId);
      this.wishlistUrl = Preconditions.checkNotNull(wishlistUrl);
      this.items = Preconditions.checkNotNull(items);
    }

    @Override
    public boolean equals(@Nullable Object another) {
      if (this == another)
        return true;
      return another instanceof WishlistChanged && equalTo((WishlistChanged) another);
    }

    private boolean equalTo(WishlistChanged another) {
      return presentId.equals(another.presentId) && wishlistUrl.equals(another.wishlistUrl) && items.equals(another.items);
    }

    @Override
    public int hashCode() {
      int h = 31;
      h = h * 17 + presentId.hashCode();
      h = h * 17 + wishlistUrl.hashCode();
      h = h * 17 + items.hashCode();
      return h;
    }

    @Override
    public String toString() {
      return MoreObjects.toStringHelper("WishlistChanged")
              .add("presentId", presentId)
              .add("wishlistUrl", wishlistUrl)
              .add("items", items)
              .toString();
    }
  }
}
