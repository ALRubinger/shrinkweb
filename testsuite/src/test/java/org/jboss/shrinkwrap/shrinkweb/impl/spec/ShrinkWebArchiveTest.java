package org.jboss.shrinkwrap.shrinkweb.impl.spec;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.classloader.ShrinkWrapClassLoader;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.shrinkweb.api.Endpoint;
import org.jboss.shrinkwrap.shrinkweb.api.spec.ShrinkWebArchive;
import org.jboss.shrinkwrap.shrinkweb.test.HelloEndpoint;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;

/**
 * Created by alr on 2/16/18.
 */
public class ShrinkWebArchiveTest {


    private final ShrinkWebArchive singleEndpointArchive = null;

    @Test
    public void shouldBeEmpty() {
        final ShrinkWebArchive archive = ShrinkWrap.create(ShrinkWebArchive.class);
        Assert.assertTrue("archive should be empty", archive.getContent().isEmpty());
    }

    @Test
    public void shouldContainInvokableEndpoint() throws Exception {

        // Put a response into an Endpoint and add it to the archive
        final String response = "Hello from ALR!";
        final String relativePath = "/api";
        final ShrinkWebArchive archive = ShrinkWrap.create(ShrinkWebArchive.class);
        archive.addEndpoint(HelloEndpoint.class, relativePath);
        System.out.println(archive.toString(true));

        // Pull out the name of the class to be listening on the endpoint
        final String classNameFromArchive = ((StringAsset) archive.get(relativePath).getAsset()).getSource();
        System.out.println("Got className " + classNameFromArchive + " to service endpoint " + relativePath);

        // Grab the class as noted from the name in the endpoint
        final JavaArchive libraryDependencies = ShrinkWrap.create(JavaArchive.class).addClass(Endpoint.class);
        ShrinkWrapClassLoader parentClForDependencies = new ShrinkWrapClassLoader((ClassLoader) null, libraryDependencies);
        ShrinkWrapClassLoader cl = new ShrinkWrapClassLoader(parentClForDependencies, "WEB-INF/classes", archive, libraryDependencies);
        final Class<?> clazz = Class.forName(classNameFromArchive, false, cl);
        cl.close();

        // Instanciate the class and invoke upon it
        final Object instance = clazz.newInstance();
        final Method method = clazz.getMethod("getResponse");
        System.out.println("method: " + method);
        final Object methodInvoke = method.invoke(instance);
        System.out.println("methodInvoke: " + methodInvoke);

        // Ensure we got the right response
        final String responseRoundtrip = (String) methodInvoke;
        Assert.assertEquals("response was not as expected", response, responseRoundtrip);
    }

}
