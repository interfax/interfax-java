package net.interfax.rest.client.impl;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import net.interfax.rest.client.InterFAXClient;
import net.interfax.rest.client.domain.APIResponse;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;


public class InterFAXJerseyClientTest {

    private String faxNumber = "+442084978672";

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);

    @Test
    public void testSendFax() throws Exception {

        String absoluteFilePath = this.getClass().getClassLoader().getResource("test.pdf").getFile();
        File file = new File(absoluteFilePath);

        InterFAXClient interFAXClient = new InterFAXJerseyClient();
        APIResponse apiResponse = interFAXClient.sendFax(faxNumber, file);
        Assert.assertEquals("[https://rest.interfax.net/outbound/faxes/666639902]", apiResponse.getHeaders().get("Location").toString());
        Assert.assertEquals(201, apiResponse.getStatusCode());
    }

    @Test
    public void testSendMultipleFilesAsFax() throws Exception {

        String absoluteFilePath = this.getClass().getClassLoader().getResource("test.pdf").getFile();
        File file1 = new File(absoluteFilePath);
        File file2 = new File(absoluteFilePath);

        File[] files = {file1, file2};

        InterFAXClient interFAXClient = new InterFAXJerseyClient();
        APIResponse apiResponse = interFAXClient.sendFax(faxNumber, files);
        Assert.assertEquals("[https://rest.interfax.net/outbound/faxes/667457707]", apiResponse.getHeaders().get("Location").toString());
        Assert.assertEquals(201, apiResponse.getStatusCode());
    }


    @Test
    public void testUploadDocument() throws Exception {

        String absoluteFilePath = this.getClass().getClassLoader().getResource("A17_FlightPlan.pdf").getFile();
        File file = new File(absoluteFilePath);

        InterFAXClient interFAXClient = new InterFAXJerseyClient();
        APIResponse apiResponse = interFAXClient.uploadDocument(file);
        Assert.assertEquals(200, apiResponse.getStatusCode());
    }
}