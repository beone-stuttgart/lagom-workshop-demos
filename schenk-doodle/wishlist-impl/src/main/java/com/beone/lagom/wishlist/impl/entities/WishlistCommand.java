package com.beone.lagom.wishlist.impl.entities;

import akka.Done;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.CompressedJsonable;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.util.List;

public interface WishlistCommand {

  /**
   * command for reading the contents of a wishlist.
   */
  class GetWishlistCommand implements WishlistCommand, CompressedJsonable, PersistentEntity.ReplyType<WishlistWorldState> {
    @Override
    public String toString() {
      return MoreObjects.toStringHelper("GetWishlistCommand").toString();
    }
  }

  /**
   * Command for updating the contents of a wishlist.
   */
  @SuppressWarnings("serial")
  @Immutable
  @JsonDeserialize
  class CreateWishlistCommand implements WishlistCommand, CompressedJsonable, PersistentEntity.ReplyType<Done> {
    public final String presentId;
    public final String wishlistUrl;
    public final List<WishlistItem> items;

    @JsonCreator
    public CreateWishlistCommand(final String presentId, final String wishlistUrl, final List<WishlistItem> items) {
      this.presentId = Preconditions.checkNotNull(presentId);
      this.wishlistUrl = Preconditions.checkNotNull(wishlistUrl);
      this.items = Preconditions.checkNotNull(items);
    }

    @Override
    public boolean equals(@Nullable Object another) {
      if (this == another) {
        return true;
      }
      return another instanceof CreateWishlistCommand && equalTo((CreateWishlistCommand) another);
    }

    private boolean equalTo(CreateWishlistCommand another) {
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
      return MoreObjects.toStringHelper("GreetingMessageChanged")
              .add("presentId", presentId)
              .add("wishlistUrl", wishlistUrl)
              .add("items", items)
              .toString();
    }
  }
}
