package com.beone.lagom.contribute.api;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.transport.Method;

import static com.lightbend.lagom.javadsl.api.Service.*;


public interface ContributeService extends Service {
    ServiceCall<NotUsed, ContributionInfo> getParticipants(String presentId);

    ServiceCall<ContributorInfo, NotUsed> addContribution(String presentId);

    ServiceCall<ConfirmationInfo, NotUsed> confirmContribution(String presentId);

    @Override
    default Descriptor descriptor() {
        return named("contributeservice").withCalls(
                restCall(Method.GET, "/contribute/api/:presentId/participants", this::getParticipants),
                restCall(Method.POST, "/contribute/api/:presentId", this::addContribution),
                restCall(Method.POST, "/contribute/api/:presentId/participants", this::confirmContribution)
        ).withAutoAcl(true);
    }
}
