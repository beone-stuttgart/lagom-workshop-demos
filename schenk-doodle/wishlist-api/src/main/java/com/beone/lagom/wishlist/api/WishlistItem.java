package com.beone.lagom.wishlist.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;

import javax.annotation.concurrent.Immutable;

@Immutable
@JsonDeserialize
public class WishlistItem {
  public final String imageUrl;
  public final String name;
  public final String price;
  public final String link;

  @JsonCreator
  public WishlistItem(final String imageUrl, final String name, final String price, final String link) {
    this.imageUrl = imageUrl;
    this.name = name;
    this.price = price;
    this.link = link;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper("WishlistItem")
            .add("imageUrl", imageUrl)
            .add("name", name)
            .add("price", price)
            .add("link", link)
            .toString();
  }
}
