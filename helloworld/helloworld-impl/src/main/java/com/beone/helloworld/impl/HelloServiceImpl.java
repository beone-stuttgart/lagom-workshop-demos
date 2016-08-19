package com.beone.helloworld.impl;

import akka.NotUsed;
import com.beone.helloworld.api.GreetingData;
import com.beone.helloworld.api.Greetings;
import com.lightbend.lagom.javadsl.api.ServiceCall;

import java.util.concurrent.CompletableFuture;

import com.beone.helloworld.api.HelloService;
import org.pcollections.HashTreePMap;
import org.pcollections.PMap;


public class HelloServiceImpl implements HelloService {
    @Override
    public ServiceCall<NotUsed, String> sayHello(String name) {
        return (notused ->
                CompletableFuture.completedFuture("Hello " + name + "!"));
    }

    @Override
    public ServiceCall<GreetingData, Greetings> getTranslatedGreetings() {
        return (greetingData -> {
            PMap<String, String> greetings = HashTreePMap.<String, String>empty()
                    .plus("de", "Hallo " + greetingData.getFirstName() + " " + greetingData.getLastName())
                    .plus("en", "Hello " + greetingData.getFirstName())
                    .plus("fr", "Bonjour " + greetingData.getFirstName() + " " + greetingData.getLastName());
            return CompletableFuture.completedFuture(Greetings.builder()
                    .greetings(greetings)
                    .build());
        });
    }
}
