package net.interfax.rest.client.impl;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import net.interfax.rest.client.InterFAXClient;
import net.interfax.rest.client.domain.APIResponse;
import net.interfax.rest.client.domain.DocumentUploadSessionOptions;
import net.interfax.rest.client.domain.UploadedDocumentResponse;
import net.interfax.rest.client.domain.enums.Disposition;
import net.interfax.rest.client.domain.enums.Sharing;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.util.Optional;


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

    @Test
    public void testUploadDocumentWithOptions() throws Exception {

        String absoluteFilePath = this.getClass().getClassLoader().getResource("A17_FlightPlan.pdf").getFile();
        File file = new File(absoluteFilePath);

        InterFAXClient interFAXClient = new InterFAXJerseyClient();
        DocumentUploadSessionOptions documentUploadSessionOptions = new DocumentUploadSessionOptions();
        documentUploadSessionOptions.setName(Optional.of("overriddenname.pdf"));
        documentUploadSessionOptions.setSize(Optional.of(Integer.toUnsignedLong(12345)));
        documentUploadSessionOptions.setDisposition(Optional.of(Disposition.multiUse));
        documentUploadSessionOptions.setSharing(Optional.of(Sharing.privateDoc));
        APIResponse apiResponse = interFAXClient.uploadDocument(file, Optional.of(documentUploadSessionOptions));
        Assert.assertEquals(200, apiResponse.getStatusCode());
    }

    @Test
    public void testGetUploadedDocumentsList() throws Exception {

        InterFAXClient interFAXClient = new InterFAXJerseyClient("devexp-java", "ZAQ!zaq1");
        UploadedDocumentResponse[] uploadedDocumentResponses = interFAXClient.getUploadedDocumentsList();

        Assert.assertEquals(2, uploadedDocumentResponses.length);
        Assert.assertEquals("sampledoc.pdf", uploadedDocumentResponses[0].getFileName());
        Assert.assertEquals("A17_FlightPlan.pdf", uploadedDocumentResponses[1].getFileName());
    }
}