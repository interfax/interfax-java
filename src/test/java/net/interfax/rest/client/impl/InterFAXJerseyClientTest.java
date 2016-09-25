package net.interfax.rest.client.impl;

import net.interfax.rest.client.InterFAXClient;
import net.interfax.rest.client.domain.Response;
import org.junit.Test;

import java.io.File;

public class InterFAXJerseyClientTest {

    @Test
    public void testSendFax() throws Exception {

        String absoluteFilePath = this.getClass().getClassLoader().getResource("test.pdf").getFile();
        File file = new File(absoluteFilePath);

        InterFAXClient interFAXClient = new InterFAXJerseyClient();
        Response response = interFAXClient.sendFax("+442084978672", file);

        System.out.println(response.getStatusCode());
    }
}