package com.beone.lagom.organize.impl.models;

import com.beone.lagom.organize.impl.models.PresentCommand.CreatePresent;
import com.beone.lagom.organize.impl.models.PresentCommand.GetPresentData;
import com.beone.lagom.organize.impl.models.PresentEvent.PresentCreatedEvent;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;

import java.util.Optional;

@SuppressWarnings("unchecked")
public class Present extends PersistentEntity<PresentCommand, PresentEvent, PresentWorldState> {


  @Override
  public Behavior initialBehavior(final Optional<PresentWorldState> snapshotState) {
    final BehaviorBuilder builder = newBehaviorBuilder(snapshotState.orElse(new PresentWorldState(null, null, null, null, null, null, null)));

    // MESSAGE: create present handling
    builder.setCommandHandler(CreatePresent.class, (cmd, ctx) -> {
      final PresentCreatedEvent event = new PresentCreatedEvent(cmd.id, cmd.securityToken, cmd.organisatorName,
            cmd.organisatorMail, cmd.deadline, cmd.title, cmd.description);
      return ctx.thenPersist(event, evt -> ctx.reply(state()));
    });
    builder.setEventHandler(PresentCreatedEvent.class, event -> new PresentWorldState(event.id, event.securityToken, event.organisatorName, event.organisatorMail, event.deadline, event.title, event.description));

    // MESSAGE: get present
    builder.setReadOnlyCommandHandler(GetPresentData.class, (cmd, ctx) -> ctx.reply(state()));

    return builder.build();
  }
}
