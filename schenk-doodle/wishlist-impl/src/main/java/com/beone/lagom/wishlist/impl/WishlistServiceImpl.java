package com.beone.lagom.wishlist.impl;

import akka.Done;
import akka.NotUsed;
import com.beone.lagom.wishlist.api.WishlistImportRequest;
import com.beone.lagom.wishlist.api.WishlistResponse;
import com.beone.lagom.wishlist.api.WishlistService;
import com.beone.lagom.wishlist.impl.entities.Wishlist;
import com.beone.lagom.wishlist.impl.entities.WishlistCommand;
import com.beone.lagom.wishlist.impl.entities.WishlistCommand.CreateWishlistCommand;
import com.beone.lagom.wishlist.impl.entities.WishlistCommand.GetWishlistCommand;
import com.beone.lagom.wishlist.impl.entities.WishlistItem;
import com.beone.lagom.wishlist.impl.entities.WishlistWorldState;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class WishlistServiceImpl implements WishlistService {
  private static final Logger logger = LoggerFactory.getLogger(WishlistServiceImpl.class);
  private final PersistentEntityRegistry persistentEntityRegistry;

  @Inject
  public WishlistServiceImpl(PersistentEntityRegistry persistentEntityRegistry) {
    this.persistentEntityRegistry = persistentEntityRegistry;
    persistentEntityRegistry.register(Wishlist.class);
  }

  @Override
  public ServiceCall<WishlistImportRequest, Done> importWishlist() {
    return req -> {
      final PersistentEntityRef<WishlistCommand> entity = persistentEntityRegistry.refFor(Wishlist.class, req.presentId);

      CompletableFuture.supplyAsync(() -> req.wishlistUrl)
              .thenApplyAsync(this::crawlWishlist)
              .thenApplyAsync(x -> entity.ask(new CreateWishlistCommand(req.presentId, req.wishlistUrl, x)));

      return CompletableFuture.supplyAsync(Done::getInstance);
    };
  }

  @Override
  public ServiceCall<NotUsed, WishlistResponse> wishlistForPresent(final String id) {
    logger.info("wishlistForPresent " + id);
    return request -> persistentEntityRegistry.refFor(Wishlist.class, id)
            .ask(new GetWishlistCommand())
            .thenApplyAsync(x -> createWishlistResponse((WishlistWorldState) x));
  }

  private List<WishlistItem> crawlWishlist(final String wishlistUrl) {
    final List<WishlistItem> result = new ArrayList<>();
    try {
      final Document doc = Jsoup.connect(wishlistUrl).get();
      final Elements wrapperEl = doc.select("div#item-page-wrapper div#g-items-atf");
      if (wrapperEl.isEmpty()) {
        return Collections.emptyList();
      }

      final Element wrapper = wrapperEl.get(0);
      final Elements cartContents = wrapper.select("div.a-fixed-left-grid");
      for (final Element element : cartContents) {
        final String id = element.id();
        final String itemId = id.substring("item_".length());

        final Element imageElement = element.select("#itemImage_" + itemId + " img").get(0);
        final Element nameElement = element.select("a#itemName_" + itemId).get(0);
        final Element priceElement = element.select("span#itemPrice_" + itemId).get(0);

        final String imageUrl = imageElement.attr("src");
        final String itemName = nameElement.text();
        final String itemUrl = nameElement.attr("href");
        final String price = priceElement.text();

        //erzeuge item.
        result.add(new WishlistItem(itemName, itemUrl, imageUrl, price));
      }

      return result;
    } catch (IOException e) {
      return null;
    }
  }


  private WishlistResponse createWishlistResponse(final WishlistWorldState state) {
    final String presentId = state.presentId;
    final boolean crawledSuccessfully = state.items != null && !state.items.isEmpty();
    final List<com.beone.lagom.wishlist.api.WishlistItem> items = state.items == null
            ? null
            : state.items.stream().map(WishlistServiceImpl::toWishlistItem).collect(Collectors.toList());
    logger.info("createWishlistResponse " + presentId + " crawled: " + crawledSuccessfully);
    return new WishlistResponse(presentId, crawledSuccessfully,items);
  }

  private static com.beone.lagom.wishlist.api.WishlistItem toWishlistItem(final com.beone.lagom.wishlist.impl.entities.WishlistItem x) {
    return new com.beone.lagom.wishlist.api.WishlistItem(x.itemImageUrl, x.itemName, x.itemPrice, x.itemUrl);
  }
}
