package com.beone.lagom.organize.impl;

import akka.NotUsed;
import com.beone.lagom.organize.api.models.PresentAdminInfo;
import com.beone.lagom.organize.api.models.PresentBaseInformation;
import com.beone.lagom.organize.api.models.PresentCreateRequest;
import com.beone.lagom.organize.api.OrganizeService;
import com.beone.lagom.organize.api.models.PresentCreateResponse;
import com.beone.lagom.organize.impl.models.Present;
import com.beone.lagom.organize.impl.models.PresentCommand;
import com.beone.lagom.organize.impl.models.PresentCommand.CreatePresent;
import com.beone.lagom.organize.impl.models.PresentCommand.GetPresentData;
import com.beone.lagom.organize.impl.models.PresentWorldState;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import org.apache.xerces.impl.dv.util.Base64;

import javax.inject.Inject;
import java.util.UUID;

public class OrganizeServiceImpl implements OrganizeService {
  private final PersistentEntityRegistry persistentEntityRegistry;

  @Inject
  public OrganizeServiceImpl(PersistentEntityRegistry persistentEntityRegistry) {
    this.persistentEntityRegistry = persistentEntityRegistry;
    persistentEntityRegistry.register(Present.class);
  }

  @Override
  public ServiceCall<NotUsed, PresentBaseInformation> getBaseInfo(final String id) {
    return request ->
        persistentEntityRegistry.refFor(Present.class, id)
            .ask(GetPresentData.INSTANCE)
            .thenApplyAsync(x -> (PresentWorldState) x)
            .thenApplyAsync(x -> new PresentBaseInformation(x.organisatorName, x.title, x.description, x.deadline));
  }

  @Override
  public ServiceCall<NotUsed, PresentAdminInfo> getAdminInfo(final String id) {
    return request ->
        persistentEntityRegistry.refFor(Present.class, id)
            .ask(GetPresentData.INSTANCE)
            .thenApplyAsync(x -> (PresentWorldState) x)
            .thenApplyAsync(x -> new PresentAdminInfo(x.presentAdminSecret, x.organisatorName, x.title, x.description, x.deadline));
  }

  @Override
  public ServiceCall<PresentCreateRequest, PresentCreateResponse> organizePresent() {
    return request -> {
      // generate an id and a security token
      final String generatedId = Base64.encode(UUID.randomUUID().toString().getBytes());
      final String securityKey = Base64.encode(UUID.randomUUID().toString().getBytes());

      // get data from cassandra
      final PersistentEntityRef<PresentCommand> ref = persistentEntityRegistry.refFor(Present.class, generatedId);

      // create a command to generate a present
      return ref.ask(new CreatePresent(generatedId, securityKey, request))
          .thenApplyAsync(x -> (PresentWorldState) x)
          .thenApplyAsync(x -> new PresentCreateResponse(x.presentId, x.presentAdminSecret));
    };
  }
}
