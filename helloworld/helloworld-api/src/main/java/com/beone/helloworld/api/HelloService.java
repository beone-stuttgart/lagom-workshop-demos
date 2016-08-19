package com.beone.helloworld.api;

import static com.lightbend.lagom.javadsl.api.Service.*;
import static com.lightbend.lagom.javadsl.api.Service.restCall;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.transport.Method;


public interface HelloService extends Service {
    ServiceCall<NotUsed, String> sayHello(String name);

    ServiceCall<GreetingData, Greetings> getTranslatedGreetings();

    @Override
    default Descriptor descriptor() {
        return named("helloservice").withCalls(
                restCall(Method.GET, "/api/hello/:id", this::sayHello),
                restCall(Method.GET, "/api/translatedgreeting/", this::getTranslatedGreetings)
        ).withAutoAcl(true);
    }
}
