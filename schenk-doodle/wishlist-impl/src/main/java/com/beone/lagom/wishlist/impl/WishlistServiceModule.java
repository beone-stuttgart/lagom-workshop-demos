package com.beone.lagom.wishlist.impl;

import com.beone.lagom.wishlist.api.WishlistService;
import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;

public class WishlistServiceModule extends AbstractModule implements ServiceGuiceSupport {
    @Override
    protected void configure() {
        bindServices(serviceBinding(WishlistService.class, WishlistServiceImpl.class));
    }
}
