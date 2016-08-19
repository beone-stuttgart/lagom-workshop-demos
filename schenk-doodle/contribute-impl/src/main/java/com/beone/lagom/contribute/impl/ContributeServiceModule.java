package com.beone.lagom.contribute.impl;

import com.beone.lagom.contribute.api.ContributeService;
import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;


public class ContributeServiceModule extends AbstractModule implements ServiceGuiceSupport {
    @Override
    protected void configure() {
        bindServices(serviceBinding(ContributeService.class, ContributeServiceImpl.class));
    }
}
