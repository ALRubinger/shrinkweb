import io.undertow.Undertow;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.shrinkweb.api.spec.ShrinkWebArchive;
import org.jboss.shrinkwrap.shrinkweb.impl.undertow.UndertowShrinkWebHandler;
import org.jboss.shrinkwrap.shrinkweb.test.ExternalLibraryDependencyEndpoint;
import org.jboss.shrinkwrap.shrinkweb.test.HelloEndpoint;

/**
 * A test class with a Main that soon will be ported
 * into a proper JUnit-ey thing
 */
public class Test {

    final Undertow server;
    final ShrinkWebArchive archive;

    /**
     * To use this: run main, then curl localhost:8080 on either:
     *
     * /api/hello
     * /api/libdep
     */

    Test(){

        // Construct an archive which describes our deployment
        this.archive = ShrinkWrap.create(ShrinkWebArchive.class,"webapp.jar")
                .addEndpoint(HelloEndpoint.class,"/api/hello")
                .addEndpoint(ExternalLibraryDependencyEndpoint.class,"/api/libdep")
                .addAsLibraries(
                        Maven.resolver().loadPomFromFile("testapps/pom.xml")
                                .resolve("org.apache.commons:commons-text")
                                .withTransitivity().asFile());
        System.out.println(this.archive.toString(true));

        // Make a server which has the archive deployed into it
        /*
         * TODO Build some mechanism so that the server has a
         * deployment mechanism whereby this does not need to be done manually
         */
        this.server = Undertow.builder()
                .addHttpListener(8080, "localhost")
                .setHandler(new UndertowShrinkWebHandler(archive)).build();
        this.server.start();
    }

    public static void main(final String... args){
        final Test test = new Test();
    }
}
