package com.beone.lagom.mail.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;

import javax.annotation.concurrent.Immutable;
import java.util.List;


@Immutable
@JsonDeserialize
public final class MailMessage {

    public final List<String> recipients;
    public final String subject;
    public final String contents;

    @JsonCreator
    public MailMessage(List<String> recipients, String subject, String contents) {
        this.recipients = ImmutableList.copyOf(recipients);
        this.subject = subject;
        this.contents = contents;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("MailMessage")
                .add("recipients", recipients)
                .add("subject", subject)
                .add("contents", contents)
                .toString();
    }
}
