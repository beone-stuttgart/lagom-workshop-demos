package com.beone.lagom.wishlist.impl.entities;

import akka.Done;
import com.beone.lagom.wishlist.impl.entities.WishlistCommand.CreateWishlistCommand;
import com.beone.lagom.wishlist.impl.entities.WishlistCommand.GetWishlistCommand;
import com.beone.lagom.wishlist.impl.entities.WishlistEvent.WishlistChanged;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;

import java.util.Optional;

@SuppressWarnings("unchecked")
public class Wishlist extends PersistentEntity<WishlistCommand, WishlistEvent, WishlistWorldState> {

  @Override
  public Behavior initialBehavior(final Optional<WishlistWorldState> snapshotState) {
    final BehaviorBuilder b = newBehaviorBuilder(snapshotState.orElse(new WishlistWorldState(null, null, null)));

    // command handler für das erzeugen einer Wishlist.
    b.setCommandHandler(CreateWishlistCommand.class, (cmd, ctx) ->
      ctx.thenPersist(new WishlistChanged(cmd.presentId, cmd.wishlistUrl, cmd.items),
              evt -> ctx.reply(Done.getInstance())));

    // Event handler for the WishlistChanged event.
    b.setEventHandler(WishlistChanged.class, evt -> new WishlistWorldState(evt.presentId, evt.wishlistUrl, evt.items));

    // Command handler for the Hello command.
    b.setReadOnlyCommandHandler(GetWishlistCommand.class, (cmd, ctx) -> ctx.reply(state()));

    return b.build();
  }

}
