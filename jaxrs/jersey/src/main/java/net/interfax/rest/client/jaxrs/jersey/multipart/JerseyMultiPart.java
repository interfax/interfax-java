package net.interfax.rest.client.jaxrs.jersey.multipart;

import net.interfax.rest.client.jaxrs.shared.FaxMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.glassfish.jersey.media.multipart.file.StreamDataBodyPart;

import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.InputStream;

public class JerseyMultiPart implements FaxMultiPart {

    private final MultiPart multiPart = new MultiPart();

    @Override
    public void add(String entityName, File file, MediaType mediaType) {
        multiPart.bodyPart(new FileDataBodyPart(entityName, file, mediaType));
    }

    @Override
    public void add(String entityName, InputStream inputStream, MediaType mediaType) {
        multiPart.bodyPart(new StreamDataBodyPart(entityName, inputStream, entityName, mediaType));
    }

    @Override
    public MediaType getMediaType() {
        return multiPart.getMediaType();
    }

    @Override
    public Object getMultiPart() {
        return multiPart;
    }

}
