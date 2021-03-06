package com.beone.lagom.contribute.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.immutable.ImmutableStyle;
import com.lightbend.lagom.serialization.Jsonable;
import org.immutables.value.Value;


@Value.Immutable
@ImmutableStyle
@JsonDeserialize(as = ConfirmationInfo.class)
public interface AbstractConfirmationInfo extends Jsonable {
    String getMail();
}
