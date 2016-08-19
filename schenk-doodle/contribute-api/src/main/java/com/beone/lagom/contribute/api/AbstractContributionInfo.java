package com.beone.lagom.contribute.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.lightbend.lagom.javadsl.immutable.ImmutableStyle;
import org.immutables.value.Value;
import org.pcollections.PVector;

import javax.annotation.concurrent.Immutable;
import java.util.List;


@Value.Immutable
@ImmutableStyle
@JsonDeserialize(as = ContributionInfo.class)
public interface AbstractContributionInfo {
    String getPresentId();
    PVector<ContributorInfo> getContributions();
}
