package net.interfax.rest.client;

import net.interfax.rest.client.domain.APIResponse;

import java.io.File;

public interface InterFAXClient {

    public APIResponse sendFax(final String faxNumber, final File fileToSendAsFax);

    public APIResponse sendFax(final String faxNumber, final File[] fileToSendAsFax);

}
