package com.beone.lagom.contribute.impl.persistence;

import com.beone.lagom.contribute.api.ContributorInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.immutable.ImmutableStyle;
import com.lightbend.lagom.serialization.Jsonable;
import org.immutables.value.Value;
import org.pcollections.PMap;


@Value.Immutable
@ImmutableStyle
@JsonDeserialize(as = ContributionAdded.class)
public interface AbstractContribState extends Jsonable {
    PMap<String, ContributorInfo> getContributions();
}
