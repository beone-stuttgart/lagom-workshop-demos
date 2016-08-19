package com.beone.lagom.wishlist.impl.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;

import java.util.Objects;

@JsonDeserialize
public class WishlistItem {
  public final String itemName;
  public final String itemUrl;
  public final String itemImageUrl;
  public final String itemPrice;

  @JsonCreator
  public WishlistItem(final String itemName, final String itemUrl, final String itemImageUrl, final String itemPrice) {
    this.itemName = itemName;
    this.itemUrl = itemUrl;
    this.itemImageUrl = itemImageUrl;
    this.itemPrice = itemPrice;
  }

  @Override
  public boolean equals(final Object other) {
    return (other == this) || (other instanceof WishlistItem && equalTo((WishlistItem) other));
  }

  private boolean equalTo(final WishlistItem other) {
    return Objects.equals(other.itemImageUrl, itemImageUrl) && other.itemName.equals(itemName)
            && other.itemImageUrl.equals(itemImageUrl) && itemPrice.equals(other.itemPrice);
  }

  @Override
  public int hashCode() {
    int result = 13;
    result += 17 * itemName.hashCode();
    result += 17 * itemUrl.hashCode();
    result += 17 * (itemImageUrl == null ? 0 : itemImageUrl.hashCode());
    result += 17 * itemPrice.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper("WishlistItem")
            .add("itemName", itemName)
            .add("itemUrl", itemUrl)
            .add("itemPrice", itemPrice)
            .toString();
  }
}
