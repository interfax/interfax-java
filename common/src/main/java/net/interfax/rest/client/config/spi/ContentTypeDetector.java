package net.interfax.rest.client.config.spi;

import java.io.File;
import java.io.IOException;

public interface ContentTypeDetector {
    String detect(File file) throws IOException;
}
