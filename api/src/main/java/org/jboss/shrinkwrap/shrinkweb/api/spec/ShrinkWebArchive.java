package org.jboss.shrinkwrap.shrinkweb.api.spec;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.container.LibraryContainer;
import org.jboss.shrinkwrap.shrinkweb.api.container.EndpointContainer;

/**
 * Created by alr on 2/16/18.
 */
public interface ShrinkWebArchive extends Archive<ShrinkWebArchive>,
        EndpointContainer<ShrinkWebArchive>,
        LibraryContainer<ShrinkWebArchive> {
}
