package com.beone.lagom.contribute.impl.persistence;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.immutable.ImmutableStyle;
import com.lightbend.lagom.javadsl.persistence.AggregateEvent;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.serialization.Jsonable;
import org.immutables.value.Value;


public interface ContribEvent extends Jsonable, AggregateEvent<ContribEvent> {
    AggregateEventTag<ContribEvent> EVENT_TAG = AggregateEventTag.of(ContribEvent.class);

    @Override
    default AggregateEventTag<ContribEvent> aggregateTag() {
        return EVENT_TAG;
    }

    @Value.Immutable
    @ImmutableStyle
    @JsonDeserialize(as = ContributionAdded.class)
    interface AbstractContributionAdded extends ContribEvent {
        String getName();
        String getMail();
        double getAmount();
    }

    @Value.Immutable
    @ImmutableStyle
    @JsonDeserialize(as = ContributionConfirmed.class)
    interface AbstractContributionConfirmed extends ContribEvent {
        String getMail();
    }

    @Value.Immutable
    @ImmutableStyle
    @JsonDeserialize(as = Done.class)
    interface AbstractDone extends ContribEvent {
        Done DONE = Done.builder().build();
    }
}
