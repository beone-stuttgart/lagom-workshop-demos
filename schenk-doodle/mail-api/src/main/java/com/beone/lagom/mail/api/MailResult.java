package com.beone.lagom.mail.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;

import javax.annotation.concurrent.Immutable;


@Immutable
@JsonDeserialize
public final class MailResult {
    public final String message;
    public final boolean success;

    @JsonCreator
    public MailResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("MailResult")
                .add("message", message)
                .add("success", success)
                .toString();
    }
}
