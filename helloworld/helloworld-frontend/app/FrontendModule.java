import com.beone.helloworld.api.HelloService;
import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.client.ServiceClientGuiceSupport;


public class FrontendModule extends AbstractModule implements ServiceClientGuiceSupport {
    @Override
    protected void configure() {
        bindClient(HelloService.class);
    }
}
