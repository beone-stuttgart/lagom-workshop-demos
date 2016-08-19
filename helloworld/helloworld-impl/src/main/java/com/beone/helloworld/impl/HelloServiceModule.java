package com.beone.helloworld.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import com.beone.helloworld.api.HelloService;


public class HelloServiceModule extends AbstractModule implements ServiceGuiceSupport {
  @Override
  protected void configure() {
    bindServices(serviceBinding(HelloService.class, HelloServiceImpl.class));
  }
}
