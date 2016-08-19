package com.beone.lagom.contribute.impl.persistence;

import com.beone.lagom.contribute.api.ContributorInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.immutable.ImmutableStyle;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.Jsonable;
import org.immutables.value.Value;

import java.util.Collection;


public interface ContribCmd extends Jsonable {
    @Value.Immutable
    @ImmutableStyle
    @JsonDeserialize(as = AddContribution.class)
    interface AbstractAddContribution extends ContribCmd, PersistentEntity.ReplyType<Done> {
        String getName();
        String getMail();
        double getAmount();
    }

    @Value.Immutable
    @ImmutableStyle
    @JsonDeserialize(as = ConfirmContribution.class)
    interface AbstractConfirmContribution extends ContribCmd, PersistentEntity.ReplyType<Done> {
        String getMail();
    }

    @Value.Immutable
    @ImmutableStyle
    @JsonDeserialize(as = GetContributions.class)
    interface AbstractGetContributions extends ContribCmd, PersistentEntity.ReplyType<Collection<ContributorInfo>> {
    }
}
