package net.interfax.rest.client.jaxrs.shared;

import net.interfax.rest.client.config.spi.ContentTypeDetector;
import org.apache.tika.Tika;

import java.io.File;
import java.io.IOException;

public class TikaContentTypeDetector implements ContentTypeDetector {

    private static final Tika DEFAULT_TIKA = new Tika();

    private final Tika tika;

    public TikaContentTypeDetector() {
        this(DEFAULT_TIKA);
    }

    public TikaContentTypeDetector(Tika tika) {
        this.tika = tika;
    }

    @Override
    public String detect(File file) throws IOException {
        return tika.detect(file);
    }

}
