package net.interfax.rest.client.jaxrs.shared;

import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.InputStream;

public interface FaxMultiPart {
    void add(String entityName, File file, MediaType mediaType);
    void add(String entityName, InputStream inputStream, MediaType mediaType);

    MediaType getMediaType();

    Object getMultiPart();
}
