package net.interfax.rest.client.jaxrs.shared;

import net.interfax.rest.client.config.spi.UriEncoder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

public class JdkEncoder implements UriEncoder {
    @Override
    public String encode(String raw) {
        try {
            return URLEncoder.encode(raw, Charset.defaultCharset().name());
        } catch (UnsupportedEncodingException e) {
            return URLEncoder.encode(raw);
        }
    }
}
