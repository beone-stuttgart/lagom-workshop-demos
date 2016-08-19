import com.beone.lagom.contribute.api.ContributeService;
import com.beone.lagom.mail.api.MailService;
import com.beone.lagom.organize.api.OrganizeService;
import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.client.ServiceClientGuiceSupport;


public class FrontendModule extends AbstractModule implements ServiceClientGuiceSupport {
    @Override
    protected void configure() {
        bindClient(ContributeService.class);
        bindClient(OrganizeService.class);
        bindClient(MailService.class);
    }
}
