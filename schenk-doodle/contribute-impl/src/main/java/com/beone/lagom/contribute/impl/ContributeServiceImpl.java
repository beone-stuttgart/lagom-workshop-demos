package com.beone.lagom.contribute.impl;

import akka.NotUsed;
import com.beone.lagom.contribute.api.ConfirmationInfo;
import com.beone.lagom.contribute.api.ContributeService;
import com.beone.lagom.contribute.api.ContributionInfo;
import com.beone.lagom.contribute.api.ContributorInfo;
import com.beone.lagom.contribute.impl.persistence.AddContribution;
import com.beone.lagom.contribute.impl.persistence.ConfirmContribution;
import com.beone.lagom.contribute.impl.persistence.Contributions;
import com.beone.lagom.contribute.impl.persistence.GetContributions;
import com.google.common.collect.ImmutableList;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.transport.NotFound;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import org.pcollections.PVector;
import org.pcollections.TreePVector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;


public class ContributeServiceImpl implements ContributeService {
    final static Logger logger = LoggerFactory.getLogger(ContributeServiceImpl.class);
    private final PersistentEntityRegistry persistentEntityRegistry;

    @Inject
    public ContributeServiceImpl(PersistentEntityRegistry persistentEntityRegistry) {
        this.persistentEntityRegistry = persistentEntityRegistry;
        persistentEntityRegistry.register(Contributions.class);
    }

    @Override
    public ServiceCall<NotUsed, ContributionInfo> getParticipants(String presentId) {
        /*
        return (notused) -> {
            if (!presentId.equals("123")) throw new NotFound("presentId not found");
            return CompletableFuture.completedFuture(new ContributionInfo(presentId, ImmutableList.<ContributorInfo>builder()
                    .add(ContributorInfo.builder()
                            .name("Hugo Hansel")
                            .mail("hugo.hansel@beone-group.com")
                            .amount(42.34)
                            .confirmed(true)
                            .build())
                    .build()));
        };
        */
        return (notused) -> {
            logger.info("getParticipants - " + presentId);
            return persistentEntityRegistry.refFor(Contributions.class, presentId)
                    .ask(GetContributions.builder().build())
                    .thenApply(response -> {
                        PVector<ContributorInfo> contributions = TreePVector.from((Collection<ContributorInfo>)response);
                        logger.info("Got " + contributions.size() + " contributions");
                        if (contributions.isEmpty()) {
                            throw new NotFound("Present " + presentId + " not found");
                        }
                        return ContributionInfo.builder()
                                .presentId(presentId)
                                .contributions(contributions)
                                .build();
                    });
        };
    }

    @Override
    public ServiceCall<ContributorInfo, NotUsed> addContribution(String presentId) {
        return (contributor) -> {
            logger.info("addContribution - " + presentId + ": " + contributor);
            return persistentEntityRegistry.refFor(Contributions.class, presentId)
                    .ask(AddContribution.builder()
                            .mail(contributor.getMail())
                            .name(contributor.getName())
                            .amount(contributor.getAmount())
                            .build())
                    .thenApply(response -> NotUsed.getInstance());
        };
    }

    @Override
    public ServiceCall<ConfirmationInfo, NotUsed> confirmContribution(String presentId) {
        return (confirmation) -> {
            logger.info("confirmContribution - " + presentId + ": " + confirmation);
            return persistentEntityRegistry.refFor(Contributions.class, presentId)
                    .ask(ConfirmContribution.builder()
                            .mail(confirmation.getMail())
                            .build())
                    .thenApply(response -> NotUsed.getInstance());
        };
    }
}
