package com.beone.lagom.organize.api;

import akka.NotUsed;
import com.beone.lagom.organize.api.models.PresentAdminInfo;
import com.beone.lagom.organize.api.models.PresentBaseInformation;
import com.beone.lagom.organize.api.models.PresentCreateRequest;
import com.beone.lagom.organize.api.models.PresentCreateResponse;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;


public interface OrganizeService extends Service {

  /**
   * get base info on a present
   * @param id  id of present
   * @return base info for displaying.
   */
  ServiceCall<NotUsed, PresentBaseInformation> getBaseInfo(String id);

  /**
   * organizes a present and returns the id.
   * @return id of generated present
   */
  ServiceCall<PresentCreateRequest, PresentCreateResponse> organizePresent();

  /**
   * get admin info on a present.
   * @param id  id of present.
   * @return present data.
   */
  ServiceCall<NotUsed, PresentAdminInfo> getAdminInfo(String id);

  @Override
  default Descriptor descriptor() {
    return Service.named("organizeservice").withCalls(
        Service.pathCall("/api/organize/create-present",  this::organizePresent),
        Service.pathCall("/api/organize/admin/:id", this::getAdminInfo),
        Service.pathCall("/api/organize/get/:id", this::getBaseInfo)
      ).withAutoAcl(true);
  }
}
