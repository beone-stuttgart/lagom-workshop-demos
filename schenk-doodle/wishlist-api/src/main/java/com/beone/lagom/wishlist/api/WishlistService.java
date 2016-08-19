package com.beone.lagom.wishlist.api;

import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;

public interface WishlistService extends Service {

  /**
   * imports a wichlist
   * @return true on success, false otherwise
   */
  ServiceCall<WishlistImportRequest, Done> importWishlist();

  /**
   * loads the imported wishlist for a present
   * @param id id of wishlist to get
   * @return wishlist for the account.
   */
  ServiceCall<NotUsed, WishlistResponse> wishlistForPresent(String id);


  @Override
  default Descriptor descriptor() {
    return Service.named("wishlistservice").withCalls(
            Service.pathCall("/wishlist/import", this::importWishlist),
            Service.pathCall("/wishlist/query/:id", this::wishlistForPresent)
    ).withAutoAcl(true);
  }
}
