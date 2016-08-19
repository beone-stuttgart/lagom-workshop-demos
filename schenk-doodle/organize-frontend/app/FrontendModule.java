import com.beone.lagom.contribute.api.ContributeService;
import com.beone.lagom.mail.api.MailService;
import com.beone.lagom.organize.api.OrganizeService;
import com.beone.lagom.wishlist.api.WishlistService;
import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.client.ServiceClientGuiceSupport;


public class FrontendModule extends AbstractModule implements ServiceClientGuiceSupport {
    @Override
    protected void configure() {
        bindClient(MailService.class);
        bindClient(ContributeService.class);
        bindClient(WishlistService.class);
        bindClient(OrganizeService.class);
    }
}
