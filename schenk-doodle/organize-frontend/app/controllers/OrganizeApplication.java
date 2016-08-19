package controllers;

import com.beone.lagom.contribute.api.ContributeService;
import com.beone.lagom.contribute.api.ContributionInfo;
import com.beone.lagom.mail.api.MailMessage;
import com.beone.lagom.mail.api.MailService;
import com.beone.lagom.organize.api.OrganizeService;
import com.beone.lagom.organize.api.models.PresentCreateRequest;
import com.beone.lagom.wishlist.api.WishlistImportRequest;
import com.beone.lagom.wishlist.api.WishlistResponse;
import com.beone.lagom.wishlist.api.WishlistService;
import com.google.common.base.Strings;
import controllers.model.EditData;
import models.NewPresentFormModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import play.twirl.api.Html;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;


public class OrganizeApplication extends Controller {
    private static final Logger logger = LoggerFactory.getLogger(OrganizeApplication.class);
    private final HttpExecutionContext ec;
    private final OrganizeService organizeService;
    private final MailService mailService;
    private final ContributeService contributeService;
    private final FormFactory formFactory;
    private final WishlistService wishlistService;

    @Inject
    public OrganizeApplication(HttpExecutionContext ec, ContributeService contributeService, OrganizeService organizeService, MailService mailService, WishlistService wishlistService, FormFactory formFactory) {
      this.ec = ec;
      this.mailService = mailService;
      this.organizeService = organizeService;
      this.contributeService = contributeService;
      this.formFactory = formFactory;
      this.wishlistService = wishlistService;
    }

    public Result index() {
      return ok(views.html.main.render("Geschenk organisieren", Html.apply("Hello Geschenkeorganisator")));
    }

    /**
     * action for creating a present.
     * @return result with new present html rendered
     */
    public Result newPresent() {
        return ok(views.html.newPresent.render(formFactory.<NewPresentFormModel>form(NewPresentFormModel.class)));
    }

    /**
     * action for creating the present after form input
     * @return future for generation of the present, produces either the form on invalid input or the status page.
     */
    public CompletionStage<Result> submitNewPresent() {
      // get request data.
      final Form<NewPresentFormModel> form = formFactory.form(NewPresentFormModel.class).bindFromRequest();
      // check for invalid input
      if (form.hasErrors()) {
          return CompletableFuture.supplyAsync(() -> badRequest(views.html.newPresent.render(form)), ec.current());
      }

      // prepare a message for organizing the present
      return organizeService.organizePresent()
          .invoke(createPresentInformation(form))
          .thenApplyAsync(x -> {
              final String contributeUrl = "http://127.0.0.1:9000/contribute/" + x.key;
              final String adminUrl = "http://127.0.0.1:9000/organize/" + x.key + "/" + x.securityToken + "/edit.htm";

              // send a mail async.
              final MailMessage message = new MailMessage(
                  Collections.singletonList(form.get().getOrganisatorMail()),
                  "Neues Geschenk angelegt",
                  views.html.presentCreatedMail.render(contributeUrl, adminUrl).body());
              mailService.sendMail().invoke(message).exceptionally(ex -> {
                logger.error("Could not send mail: " + ex.getMessage(), ex);
                return null;
              });

              // crawl the wishlist.
              if (!Strings.isNullOrEmpty(form.get().getWishlistUrl())) {
                  final WishlistImportRequest request = new WishlistImportRequest(x.key, form.get().getWishlistUrl());
                  CompletableFuture.runAsync(() -> wishlistService.importWishlist().invoke(request));
              }

              // show a result page.
              return ok(views.html.presentCreated.render(form.get().getTitle(), contributeUrl, adminUrl));
          }, ec.current());
    }

    public CompletionStage<Result> editPresent(final String id, final String token) {
      logger.info("editPresent " + id);
      return organizeService.getAdminInfo(id)
        .invoke()
        .thenComposeAsync(x -> {
          if (x.securityToken == null || !x.securityToken.equals(token)) {
            return CompletableFuture.completedFuture(notFound());
          }

          // query contributions.
          final CompletableFuture<ContributionInfo> participantsFuture = contributeService.getParticipants(id)
                  .invoke()
                  .exceptionally(ex -> {
                      logger.warn("Exception in getParticipants " + id, ex);
                      return null;
                  }) // Service may return a 404, which causes an exception here
                  .toCompletableFuture();

          // query wishlist items.
          final CompletableFuture<WishlistResponse> wishlistFuture = wishlistService.wishlistForPresent(id)
                  .invoke()
                  .exceptionally(ex -> {
                      logger.warn("Exception in wishlistForPresent " + id, ex);
                      return null;
                  })
                  .toCompletableFuture();

          return CompletableFuture.allOf(participantsFuture, wishlistFuture).thenApplyAsync(notused -> {
              logger.debug("All futures fulfilled");
              WishlistResponse wishlistResponse = null;
              ContributionInfo contributions = null;
              try {
                  wishlistResponse = wishlistFuture.get();
              } catch (Exception ignored) {
                  logger.warn("Error getting wishlist", ignored);
              }
              try {
                  contributions = participantsFuture.get();
              } catch (Exception ignored) {
                  logger.warn("Error getting contributions", ignored);
              }
              final EditData editData = new EditData(x, wishlistResponse, contributions);
              return ok(views.html.presentAdmin.render(editData));
          }, ec.current());
        }, ec.current());
    }





    private static PresentCreateRequest createPresentInformation(final Form<NewPresentFormModel> form) {
        return new PresentCreateRequest(form.get().getOrganisatorName(),
                form.get().getOrganisatorMail(),
                form.get().getTitle(),
                LocalDate.parse(form.get().getDeadline()),
                form.get().getDescription());
    }
}
