package net.interfax.rest.client.jaxrs.cxf.multipart;

import net.interfax.rest.client.jaxrs.shared.FaxMultiPart;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.ContentDisposition;
import org.apache.cxf.jaxrs.ext.multipart.InputStreamDataSource;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AttachmentMultiPart implements FaxMultiPart {

    private static final MediaType MULTIPART_MIXED_MEDIA_TYPE = new MediaType("multipart", "mixed");

    private final List<Attachment> attachments = new ArrayList<>();

    @Override
    public void add(String entityName, File file, MediaType mediaType) {
        String type = mediaType.toString();
        attachments.add(new Attachment(
            entityName, type, new DataHandler(new FileDataSource(file), type)
        ));
    }

    @Override
    public void add(String entityName, InputStream inputStream, MediaType mediaType) {
        String type = mediaType.toString();
        attachments.add(new Attachment(
            entityName, type, new DataHandler(new InputStreamDataSource(inputStream, type, entityName))
        ));
    }

    @Override
    public MediaType getMediaType() {
        return MULTIPART_MIXED_MEDIA_TYPE;
    }

    @Override
    public Object getMultiPart() {
        return new MultipartBody(attachments);
    }

}
