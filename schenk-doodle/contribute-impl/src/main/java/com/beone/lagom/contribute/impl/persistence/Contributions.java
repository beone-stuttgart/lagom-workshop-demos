package com.beone.lagom.contribute.impl.persistence;

import com.beone.lagom.contribute.api.ContributorInfo;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import org.pcollections.HashTreePMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;


public class Contributions extends PersistentEntity<ContribCmd, ContribEvent, ContribState> {
    final static Logger logger = LoggerFactory.getLogger(Contributions.class);

    @Override
    public Behavior initialBehavior(Optional<ContribState> snapshotState) {
        BehaviorBuilder b = newBehaviorBuilder(snapshotState.orElse(ContribState.builder()
                .contributions(HashTreePMap.empty())
                .build()));

        b.setCommandHandler(AddContribution.class, (cmd, ctx) -> {
            logger.info(cmd.toString());
            ContributionAdded event = ContributionAdded.builder()
                    .name(cmd.getName())
                    .mail(cmd.getMail())
                    .amount(cmd.getAmount())
                    .build();
            return  ctx.thenPersist(event, (notused) ->
                    ctx.reply(Done.DONE));
        });

        b.setCommandHandler(ConfirmContribution.class, (cmd, ctx) -> {
            logger.info(cmd.toString());
            ContributionConfirmed event = ContributionConfirmed.builder()
                    .mail(cmd.getMail())
                    .build();
            if (!state().getContributions().containsKey(cmd.getMail())) {
                ctx.invalidCommand("Mail address " + cmd.getMail() + " made no contribution");
                return ctx.done();
            }
            return ctx.thenPersist(event, (notused) ->
                    ctx.reply(Done.DONE));
        });

        b.setEventHandler(ContributionAdded.class, event -> {
            ContribState state = state();
            return state.withContributions(state.getContributions().plus(event.getMail(),
                    ContributorInfo.builder()
                    .mail(event.getMail())
                    .name(event.getName())
                    .amount(event.getAmount())
                    .confirmed(false)
                    .build()));
        });

        b.setEventHandler(ContributionConfirmed.class, event -> {
            ContribState state = state();
            return state.withContributions(state.getContributions().plus(event.getMail(),
                    state.getContributions().get(event.getMail())
                    .withConfirmed(true)));
        });

        b.setReadOnlyCommandHandler(GetContributions.class, (cmd, ctx) -> {
            logger.info(cmd.toString());
            ctx.reply(state().getContributions().values());
        });

        return b.build();
    }
}
