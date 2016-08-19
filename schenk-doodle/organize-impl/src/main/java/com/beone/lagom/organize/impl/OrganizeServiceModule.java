package com.beone.lagom.organize.impl;

import com.beone.lagom.organize.api.OrganizeService;
import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;


public class OrganizeServiceModule extends AbstractModule implements ServiceGuiceSupport {
    @Override
    protected void configure() {
        bindServices(serviceBinding(OrganizeService.class, OrganizeServiceImpl.class));
    }
}
