package com.beone.lagom.mail.api;

import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;

import static com.lightbend.lagom.javadsl.api.Service.*;


public interface MailService extends Service {
    ServiceCall<MailMessage, MailResult> sendMail();

    @Override
    default Descriptor descriptor() {
        return named("mailservice").withCalls(
                pathCall("/api/send",  this::sendMail)
        ).withAutoAcl(true);
    }
}
