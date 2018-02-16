package org.jboss.shrinkwrap.shrinkweb.test;

import org.apache.commons.text.AlphabetConverter;
import org.jboss.shrinkwrap.shrinkweb.api.Endpoint;

import java.io.UnsupportedEncodingException;

/**
 * This endpoint spits out nonsense, but it uses an external library dependency,
 * {@link AlphabetConverter} from Apache commons-text, to generate it.  The point
 * is to ensure that we can add libraries into WEB-INF/lib and they'll be honored
 * on the deployment {@link ClassLoader}
 */
public class ExternalLibraryDependencyEndpoint implements Endpoint {
    @Override
    public String getResponse() {

        final Character[] originals = {'a', 'b', 'c', 'd', 'e'};
        final Character[] encoding = {'h', 'e', 'l', 'l', 'o',};
        final AlphabetConverter ac = AlphabetConverter.createConverterFromChars(originals,
                encoding, new Character[]{});
        final String returnValue;
        try {
            returnValue = ac.encode("abcde");
        } catch (final UnsupportedEncodingException uee) {
            throw new RuntimeException(uee);
        }

        return returnValue;
    }
}
