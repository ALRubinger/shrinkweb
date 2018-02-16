package org.jboss.shrinkwrap.shrinkweb.api.container;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.shrinkweb.api.Endpoint;
import org.jboss.shrinkwrap.shrinkweb.api.spec.ShrinkWebArchive;

/**
 * Created by alr on 2/16/18.
 */
public interface EndpointContainer<A extends Archive<A>> {

    /**
     * Adds the {@link Endpoint} to the specified relativePath
     *
     * @param clazz The {@link Endpoint} to add
     * @param relativePath The relative path to which the {@link Endpoint} should be bound
     * @return This archive
     * @throws IllegalArgumentException
     *             If no class or relative path was specified
     */
    A addEndpoint(Class<? extends Endpoint> clazz, String relativePath) throws IllegalArgumentException;

}
