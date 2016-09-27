package net.interfax.rest.client;

import net.interfax.rest.client.domain.Response;

import java.io.File;

public interface InterFAXClient {

    String OUTBOUND_FAXES_PATH = "outbound/faxes?faxNumber=%s";

    public Response sendFax(final String faxNumber, final File fileToSendAsFax);

}
