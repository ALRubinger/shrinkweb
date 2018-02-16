package org.jboss.shrinkwrap.shrinkweb.impl.spec;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.impl.base.container.ContainerBase;
import org.jboss.shrinkwrap.impl.base.path.BasicPath;
import org.jboss.shrinkwrap.shrinkweb.api.spec.ShrinkWebArchive;

/**
 * Created by alr on 2/16/18.
 */
public class ShrinkWebArchiveImpl extends ContainerBase<ShrinkWebArchive> implements ShrinkWebArchive {

    /**
     * Path to the manifests inside of the Archive.
     */
    private static final ArchivePath PATH_MANIFEST = new BasicPath("META-INF");
    private static final ArchivePath PATH_CLASSES = new BasicPath("WEB-INF/classes");
    private static final ArchivePath PATH_LIBRARY = new BasicPath("WEB-INF/lib");


    // -------------------------------------------------------------------------------------||
    // Constructor -------------------------------------------------------------------------||
    // -------------------------------------------------------------------------------------||

    /**
     * Create a new JavaArchive with any type storage engine as backing.
     *
     * @param delegate The storage backing.
     */
    public ShrinkWebArchiveImpl(final Archive<?> delegate) {
        super(ShrinkWebArchive.class, delegate);
    }

    // -------------------------------------------------------------------------------------||
    // Required Implementations ------------------------------------------------------------||
    // -------------------------------------------------------------------------------------||


    @Override
    protected ArchivePath getManifestPath() {
        return PATH_MANIFEST;
    }

    @Override
    protected ArchivePath getResourcePath() {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + " spec does not support resources");
    }

    @Override
    protected ArchivePath getClassesPath() {
        return PATH_CLASSES;
    }

    @Override
    protected ArchivePath getLibraryPath() {
        return PATH_LIBRARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ShrinkWebArchive addEndpoint(final Class clazz, final String relativePath) throws IllegalArgumentException {
        if (clazz == null) {
            throw new IllegalArgumentException("endpoint must be specified");
        }
        if (relativePath == null || relativePath.length() == 0) {
            throw new IllegalArgumentException("relative path must be specified");
        }
        this.addClass(clazz);
        final Asset asset = new StringAsset(clazz.getName());
        this.add(asset, relativePath);
        return covarientReturn();
    }
}
