package com.beone.helloworld.impl;

import static com.lightbend.lagom.javadsl.testkit.ServiceTest.defaultSetup;
import static com.lightbend.lagom.javadsl.testkit.ServiceTest.withServer;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.*;

import com.beone.helloworld.api.GreetingData;
import com.beone.helloworld.api.Greetings;
import org.junit.Test;

import com.beone.helloworld.api.HelloService;


public class HelloServiceTest {
    @Test
    public void shouldReturnStandardGreeting() throws Exception {
        withServer(defaultSetup().withCluster(false).withPersistence(false), server -> {
            HelloService service = server.client(HelloService.class);

            String greeting1 = service.sayHello("World").invoke().toCompletableFuture().get(5, SECONDS);
            assertEquals("Hello World!", greeting1);

            String greeting2 = service.sayHello("Hugo").invoke().toCompletableFuture().get(5, SECONDS);
            assertEquals("Hello Hugo!", greeting2);
        });
    }

    @Test
    public void shouldReturnTranslations() throws Exception {
        withServer(defaultSetup().withCluster(false).withPersistence(false), server -> {
            HelloService service = server.client(HelloService.class);

            GreetingData data = GreetingData.builder()
                    .firstName("Peter")
                    .lastName("Parker")
                    .build();

            Greetings greetings = service.getTranslatedGreetings().invoke(data)
                    .toCompletableFuture().get(5, SECONDS);

            assertTrue(greetings.getGreetings().keySet().contains("de"));
        });
    }
}
