package net.interfax.rest.client.config;

import org.apache.commons.lang3.text.StrLookup;

public class EnvironmentVariableLookup extends StrLookup<Object> {

    @Override
    public String lookup(String key) {
        return System.getenv(key);
    }
}
