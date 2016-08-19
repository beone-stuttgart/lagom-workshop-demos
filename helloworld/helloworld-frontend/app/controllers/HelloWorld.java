package controllers;

import com.beone.helloworld.api.GreetingData;
import com.beone.helloworld.api.HelloService;
import models.forms.Person;
import play.data.Form;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;


public class HelloWorld extends Controller {
    private final HttpExecutionContext ec;
    private final FormFactory formFactory;
    private final HelloService helloService;


    @Inject
    public HelloWorld(HttpExecutionContext ec, FormFactory formFactory, HelloService helloService) {
        this.ec = ec;
        this.formFactory = formFactory;
        this.helloService = helloService;
    }

    public Result index() {
        Form<Person> form = formFactory.form(Person.class);
        return ok(views.html.hello.render(form));
    }

    public CompletionStage<Result> submit() {
        Form<Person> form = formFactory.form(Person.class).bindFromRequest();

        if (form.hasErrors() || form.hasGlobalErrors()) {
            flash("error", "Fehler bei der Eingabe");
            return CompletableFuture.completedFuture(badRequest(views.html.hello.render(form)));
        }

        Person person = form.get();

        return helloService.getTranslatedGreetings()
                .invoke(GreetingData.builder()
                    .firstName(person.getFirstName())
                    .lastName(person.getLastName())
                    .build())
                .thenApplyAsync(greetings -> {
                    flash("success", "Gruß: " + greetings.getGreetings().get("de"));
                    return redirect(routes.HelloWorld.index());
                }, ec.current());
    }
}
