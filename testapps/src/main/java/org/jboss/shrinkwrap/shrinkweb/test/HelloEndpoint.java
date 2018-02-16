package org.jboss.shrinkwrap.shrinkweb.test;

import org.jboss.shrinkwrap.shrinkweb.api.Endpoint;

/**
 * Created by alr on 2/16/18.
 */
public class HelloEndpoint implements Endpoint {
    @Override
    public String getResponse() {
        return "Hello from ALR!";
    }
}
