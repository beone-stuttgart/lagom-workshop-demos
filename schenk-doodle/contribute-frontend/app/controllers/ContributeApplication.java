package controllers;

import akka.NotUsed;
import com.beone.lagom.contribute.api.ConfirmationInfo;
import com.beone.lagom.contribute.api.ContributeService;
import com.beone.lagom.contribute.api.ContributorInfo;
import com.beone.lagom.mail.api.MailMessage;
import com.beone.lagom.mail.api.MailService;
import com.beone.lagom.organize.api.OrganizeService;
import com.google.common.collect.ImmutableList;
import models.forms.Contribution;
import play.Configuration;
import play.data.Form;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;


public class ContributeApplication extends Controller {
    private final HttpExecutionContext ec;
    private final FormFactory formFactory;
    private final MailService mailService;
    private final ContributeService contributeService;
    private final OrganizeService organizeService;
    private final Configuration configuration;

    @Inject
    public ContributeApplication(HttpExecutionContext ec, FormFactory formFactory, Configuration configuration,
                                 MailService mailService, OrganizeService organizeService, ContributeService contributeService) {
        this.ec = ec;
        this.formFactory = formFactory;
        this.mailService = mailService;
        this.configuration = configuration;
        this.contributeService = contributeService;
        this.organizeService = organizeService;
    }

    public CompletionStage<Result> contribute(String presentId) {
        Form<Contribution> form = formFactory.form(Contribution.class);
        return organizeService.getBaseInfo(presentId)
                .invoke()
                .exceptionally(error -> null)
                .thenApplyAsync(
                        info -> {
                            if (info!=null) {
                                return ok(views.html.contribute.render(presentId, info, form));
                            } else {
                                return notFound("Geschenk-ID nicht gefunden. Nicht angelegt?");
                            }
                        },
                        ec.current()); // ApplyAsync and execution context needed for flash in template
    }

    String getConfirmationHash(String presentId, String mail) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return Base64.getEncoder().encodeToString(md.digest((presentId + mail + configuration.getString("play.crypto.secret")).getBytes()));
        } catch (NoSuchAlgorithmException e) {
            return "whoops";
        }
    }

    String getConfirmationLink(Http.RequestHeader req, String presentId, String mail) {
        return routes.ContributeApplication.confirmLink(presentId, mail, getConfirmationHash(presentId, mail)).absoluteURL(req._underlyingHeader());
    }


    public CompletionStage<Result> submit(String presentId) {
        Form<Contribution> form = formFactory.form(Contribution.class).bindFromRequest();
        return organizeService.getBaseInfo(presentId)
                .invoke()
                .exceptionally(error -> null)
                .thenComposeAsync(info -> {
                    if (info!=null) {
                        if (form.hasErrors() || form.hasGlobalErrors()) {
                            return CompletableFuture.completedFuture(badRequest(views.html.contribute.render(presentId, info, form)));
                        }
                        Contribution contr = form.get();
                        return contributeService.addContribution(presentId)
                                .invoke(ContributorInfo.builder()
                                        .name(contr.getName())
                                        .mail(contr.getMail())
                                        .amount(contr.getAmount())
                                        .build())
                                .thenApplyAsync(notused -> {
                                    // Call mail service asynchonously
                                    MailMessage mail = new MailMessage(
                                            ImmutableList.<String>builder().add(contr.getMail()).build(),
                                            "Beitrag zum Geschenk " + info.title,
                                            "Zum Bestätigen des Beitrags bitte auf folgenden Link klicken: " + getConfirmationLink(request(), presentId, contr.getMail()));
                                    mailService.sendMail().invoke(mail);
                                    // Show success
                                    flash("success", "Vielen Dank! Bitte auf den zugeschickten Bestätigungslink klicken.");
                                    return redirect(routes.ContributeApplication.contribute(presentId));
                                },
                                ec.current());
                    }
                    return CompletableFuture.completedFuture(notFound("Geschenk-ID nicht gefunden. Nicht angelegt?"));
                },
                ec.current()); // ApplyAsync and execution context needed for flash in template
    }

    public CompletionStage<Result> confirmLink(String presentId, String mail, String hash) {
        return organizeService.getBaseInfo(presentId)
                .invoke()
                .exceptionally(error -> null)
                .thenComposeAsync(info -> {
                    if (info!=null) {
                        if (hash.equals(getConfirmationHash(presentId, mail))) {
                            return contributeService.confirmContribution(presentId)
                                    .invoke(ConfirmationInfo.builder().mail(mail).build())
                                    .thenApplyAsync(notused -> {
                                        flash("success", "Beitrag bestätigt, vielen Dank!");
                                        return redirect(routes.ContributeApplication.contribute(presentId));
                                    },
                                    ec.current());
                        } else {
                            flash("error", "Beitrag konnte nicht bestätigt werden. Ungültiger Link?");
                            return CompletableFuture.completedFuture(redirect(routes.ContributeApplication.contribute(presentId)));
                        }
                    } else {
                        return CompletableFuture.completedFuture(notFound("Geschenk-ID nicht gefunden. Nicht angelegt?"));
                    }
                },
                ec.current()); // ApplyAsync and execution context needed for flash in template
    }
}
