package com.beone.helloworld.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.immutable.ImmutableStyle;
import com.lightbend.lagom.serialization.Jsonable;
import org.immutables.value.Value;
import org.pcollections.PMap;


@Value.Immutable
@ImmutableStyle
@JsonDeserialize(as = Greetings.class)
public interface AbstractGreetings extends Jsonable {
    PMap<String, String> getGreetings();
}
