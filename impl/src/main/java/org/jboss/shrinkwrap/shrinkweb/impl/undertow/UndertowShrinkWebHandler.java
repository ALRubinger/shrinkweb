package org.jboss.shrinkwrap.shrinkweb.impl.undertow;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import org.jboss.shrinkwrap.api.Node;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.classloader.ShrinkWrapClassLoader;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.shrinkweb.api.Endpoint;
import org.jboss.shrinkwrap.shrinkweb.api.spec.ShrinkWebArchive;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by alr on 2/17/18.
 */
public class UndertowShrinkWebHandler implements HttpHandler {

    final ShrinkWebArchive archive;

    public UndertowShrinkWebHandler(final ShrinkWebArchive archive) {
        if (archive == null) {
            throw new IllegalArgumentException("archive is required");
        }
        this.archive = archive;
    }

    @Override
    public void handleRequest(final HttpServerExchange exchange) throws Exception {
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");

        final String relativePath = exchange.getRelativePath();

        // Exists as an endpoint?
        if (!archive.contains(relativePath)) {
            exchange.setStatusCode(404).getResponseSender().send("HTTP 404: Not Found");
        }

        // Pull out the name of the class to be listening on the endpoint
        final Asset asset = archive.get(relativePath).getAsset();
        System.out.println(archive.toString(true));
        final String classNameFromArchive = ((StringAsset) asset).getSource();
        System.out.println(classNameFromArchive);

        // Grab the class as noted from the name in the endpoint
        final JavaArchive systemDeps = ShrinkWrap.create(JavaArchive.class).addClass(Endpoint.class);
        final ShrinkWrapClassLoader systemDepsCl = new ShrinkWrapClassLoader((ClassLoader) null, systemDeps);

        // Set a CL for the library dependencies
        final Set<Node> libNodes = this.archive.get("WEB-INF/lib").getChildren();
        final int numLibNodes = libNodes.size();
        final JavaArchive[] libraries = new JavaArchive[numLibNodes];
        final Iterator<Node> nodesIterator = libNodes.iterator();
        for (int i = 0; i < numLibNodes; i++) {
            final JavaArchive lib = ShrinkWrap.create(ZipImporter.class)
                    .importFrom(
                            nodesIterator.next().getAsset().openStream())
                    .as(JavaArchive.class);
            libraries[i] = lib;
        }
        final ShrinkWrapClassLoader libraryDepsCl = new ShrinkWrapClassLoader(systemDepsCl, libraries);

        // Construct a CL for the user application classes
        /*
         * This CL has parents of the library CL and ShrinkWeb system CL, and order
         * is parent first.  These CLs are all isolated from the Java system CL
         * and application classpath.
         */
        final ShrinkWrapClassLoader cl = new ShrinkWrapClassLoader(libraryDepsCl, "WEB-INF/classes", archive, systemDeps);

        // Instanciate the class and invoke upon it
        final Class<?> clazz = Class.forName(classNameFromArchive, false, cl);
        cl.close();
        final Object instance = clazz.newInstance();
        final Method method = clazz.getMethod("getResponse");
        final Object methodInvoke = method.invoke(instance);

        // Get the desired response
        final String response = (String) methodInvoke;

        // Send it
        exchange.getResponseSender().send(response);
    }
}
