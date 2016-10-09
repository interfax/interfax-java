package net.interfax.rest.client.impl;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import net.interfax.rest.client.InterFAXClient;
import net.interfax.rest.client.domain.APIResponse;
import net.interfax.rest.client.domain.DocumentUploadSessionOptions;
import net.interfax.rest.client.domain.GetFaxListOptions;
import net.interfax.rest.client.domain.GetInboundFaxListOptions;
import net.interfax.rest.client.domain.GetUploadedDocumentsListOptions;
import net.interfax.rest.client.domain.InboundFaxStructure;
import net.interfax.rest.client.domain.InboundFaxesEmailsStructure;
import net.interfax.rest.client.domain.OutboundFaxStructure;
import net.interfax.rest.client.domain.SearchFaxOptions;
import net.interfax.rest.client.domain.SendFaxOptions;
import net.interfax.rest.client.domain.UploadedDocumentStatus;
import net.interfax.rest.client.domain.enums.Disposition;
import net.interfax.rest.client.domain.enums.Sharing;
import net.interfax.rest.client.exception.UnsuccessfulStatusCodeException;
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
    public void testSendFaxWithOptions() throws Exception {

        String absoluteFilePath = this.getClass().getClassLoader().getResource("test.pdf").getFile();
        File file = new File(absoluteFilePath);

        SendFaxOptions sendFaxOptions = new SendFaxOptions();
        sendFaxOptions.setContact(Optional.of("testContactName"));

        InterFAXClient interFAXClient = new InterFAXJerseyClient();
        APIResponse apiResponse = interFAXClient.sendFax(faxNumber, file, Optional.of(sendFaxOptions));
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
    public void testSendMultipleFilesAsFaxWithOptions() throws Exception {

        String absoluteFilePath = this.getClass().getClassLoader().getResource("test.pdf").getFile();
        File file1 = new File(absoluteFilePath);
        File file2 = new File(absoluteFilePath);

        File[] files = {file1, file2};

        SendFaxOptions sendFaxOptions = new SendFaxOptions();
        sendFaxOptions.setPageSize(Optional.of("a4"));

        InterFAXClient interFAXClient = new InterFAXJerseyClient();
        APIResponse apiResponse = interFAXClient.sendFax(faxNumber, files, Optional.of(sendFaxOptions));
        Assert.assertEquals("[https://rest.interfax.net/outbound/faxes/667457707]", apiResponse.getHeaders().get("Location").toString());
        Assert.assertEquals(201, apiResponse.getStatusCode());

    }

    @Test
    public void testSendFaxUsingPreviouslyUploadedDocUrl() throws Exception {

        InterFAXClient interFAXClient = new InterFAXJerseyClient();
        APIResponse apiResponse = interFAXClient.sendFax(faxNumber, "https://rest.interfax.net/outbound/documents/90bd5477d5944c6d884c610171b75258");
        Assert.assertEquals(201, apiResponse.getStatusCode());
    }

    @Test
    public void testSendFaxUsingPreviouslyUploadedDocUrlWithOptions() throws Exception {

        SendFaxOptions sendFaxOptions = new SendFaxOptions();
        sendFaxOptions.setReplyAddress(Optional.of("reply@example.com"));

        InterFAXClient interFAXClient = new InterFAXJerseyClient();
        APIResponse apiResponse = interFAXClient.sendFax(
                                        faxNumber,
                                        "https://rest.interfax.net/outbound/documents/90bd5477d5944c6d884c610171b75258",
                                        Optional.of(sendFaxOptions));
        Assert.assertEquals(201, apiResponse.getStatusCode());
    }


    @Test
    public void testResendFax() throws Exception {

        InterFAXClient interFAXClient = new InterFAXJerseyClient();
        APIResponse apiResponse = interFAXClient.resendFax("667915471", Optional.empty());
        Assert.assertEquals(201, apiResponse.getStatusCode());
    }

    @Test
    public void testResendFaxWithOptions() throws Exception {

        InterFAXClient interFAXClient = new InterFAXJerseyClient();
        APIResponse apiResponse = interFAXClient.resendFax("667915471", Optional.of(faxNumber));
        Assert.assertEquals(201, apiResponse.getStatusCode());
    }

    @Test
    public void testGetFaxList() throws Exception {

        InterFAXClient interFAXClient = new InterFAXJerseyClient();
        OutboundFaxStructure[] outboundFaxStructures = interFAXClient.getFaxList();
        Assert.assertEquals(25, outboundFaxStructures.length);
    }

    @Test
    public void testGetFaxListWithOptions() throws Exception {

        GetFaxListOptions getFaxListOptions = new GetFaxListOptions();
        getFaxListOptions.setLimit(Optional.of(5));

        InterFAXClient interFAXClient = new InterFAXJerseyClient();
        OutboundFaxStructure[] outboundFaxStructures = interFAXClient.getFaxList(Optional.of(getFaxListOptions));
        Assert.assertEquals(5, outboundFaxStructures.length);
    }

    @Test
    public void testGetCompletedFaxList() throws Exception {

        InterFAXClient interFAXClient = new InterFAXJerseyClient();
        OutboundFaxStructure[] outboundFaxStructures = interFAXClient.getCompletedFaxList(new String[]{"667915751", "667915471"});
        Assert.assertEquals(2, outboundFaxStructures.length);
        Assert.assertEquals("667915751", outboundFaxStructures[1].getId());
    }

    @Test
    public void testGetFaxRecord() throws Exception {

        InterFAXClient interFAXClient = new InterFAXJerseyClient();
        OutboundFaxStructure outboundFaxStructure = interFAXClient.getFaxRecord("667915751");
        Assert.assertEquals("667915751", outboundFaxStructure.getId());
        Assert.assertEquals("2016-10-03T00:36:41", outboundFaxStructure.getSubmitTime());
    }

    @Test
    public void testGetOutboundFaxImage() throws Exception {

        InterFAXClient interFAXClient = new InterFAXJerseyClient();
        byte[] faxImage = interFAXClient.getOuboundFaxImage("667915751");
        Assert.assertEquals(30072, faxImage.length);
    }

    @Test(expected = UnsuccessfulStatusCodeException.class)
    public void testGetOutboundFaxImageWithInvalidId() throws Exception {

        try {
            InterFAXClient interFAXClient = new InterFAXJerseyClient();
            interFAXClient.getOuboundFaxImage("1234");
        } catch (UnsuccessfulStatusCodeException e) {
            Assert.assertEquals("Unsuccessful response from API", e.getMessage());
            Assert.assertEquals(404, e.getStatusCode());
            throw new UnsuccessfulStatusCodeException(e.getMessage());
        }
    }

    @Test
    public void testCancelFax() throws Exception {

        InterFAXClient interFAXClient = new InterFAXJerseyClient();
        APIResponse apiResponse = interFAXClient.cancelFax("279499862");
        Assert.assertEquals(404, apiResponse.getStatusCode());
    }

    @Test
    public void testSearchFaxList() throws Exception {

        InterFAXClient interFAXClient = new InterFAXJerseyClient();
        OutboundFaxStructure[] outboundFaxStructures = interFAXClient.searchFaxList();
        Assert.assertEquals(25, outboundFaxStructures.length);
        Assert.assertEquals("667915751", outboundFaxStructures[0].getId());
    }

    @Test
    public void testSearchFaxListWithOptions() throws Exception {

        SearchFaxOptions searchFaxOptions = new SearchFaxOptions();
        searchFaxOptions.setLimit(Optional.of(3));
        searchFaxOptions.setFaxNumber(Optional.of("+442084978672"));
        InterFAXClient interFAXClient = new InterFAXJerseyClient();
        OutboundFaxStructure[] outboundFaxStructures = interFAXClient.searchFaxList(Optional.of(searchFaxOptions));
        Assert.assertEquals(3, outboundFaxStructures.length);
        Assert.assertEquals("667915476", outboundFaxStructures[1].getId());
    }

    @Test
    public void testHideFax() throws Exception {

        InterFAXClient interFAXClient = new InterFAXJerseyClient();
        APIResponse apiResponse = interFAXClient.hideFax("667915469");
        Assert.assertEquals(200, apiResponse.getStatusCode());
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

        InterFAXClient interFAXClient = new InterFAXJerseyClient();
        UploadedDocumentStatus[] uploadedDocumentStatuses = interFAXClient.getUploadedDocumentsList();

        Assert.assertEquals(2, uploadedDocumentStatuses.length);
        Assert.assertEquals("sampledoc.pdf", uploadedDocumentStatuses[0].getFileName());
        Assert.assertEquals("A17_FlightPlan.pdf", uploadedDocumentStatuses[1].getFileName());
    }

    @Test
    public void testGetUploadedDocumentsListWithOptions() throws Exception {

        InterFAXClient interFAXClient = new InterFAXJerseyClient();
        GetUploadedDocumentsListOptions getUploadedDocumentsListOptions = new GetUploadedDocumentsListOptions();
        getUploadedDocumentsListOptions.setLimit(Optional.of(5));
        getUploadedDocumentsListOptions.setOffset(Optional.of(1));
        UploadedDocumentStatus[] uploadedDocumentStatuses
                = interFAXClient.getUploadedDocumentsList(Optional.of(getUploadedDocumentsListOptions));

        Assert.assertEquals(2, uploadedDocumentStatuses.length);
        Assert.assertEquals("sampledoc.pdf", uploadedDocumentStatuses[0].getFileName());
        Assert.assertEquals("A17_FlightPlan.pdf", uploadedDocumentStatuses[1].getFileName());
    }

    @Test
    public void testGetUploadedDocumentStatus() throws Exception {

        InterFAXClient interFAXClient = new InterFAXJerseyClient();
        UploadedDocumentStatus uploadedDocumentStatus = interFAXClient.getUploadedDocumentStatus("deca890355b44b42944970d9773962b5");

        Assert.assertEquals("A17_FlightPlan.pdf", uploadedDocumentStatus.getFileName());
    }

    @Test
    public void testCancelDocumentUploadSession() throws Exception {

        InterFAXClient interFAXClient = new InterFAXJerseyClient();
        APIResponse apiResponse = interFAXClient.cancelDocumentUploadSession("deca890355b44b42944970d9773962b5");

        Assert.assertEquals(200, apiResponse.getStatusCode());
    }

    @Test
    public void testGetAccountCredits() throws Exception {

        InterFAXClient interFAXClient = new InterFAXJerseyClient();
        Double balance = interFAXClient.getAccountCredits();
        Assert.assertEquals(Double.valueOf(3.8500), balance);
    }

    @Test
    public void testGetInboundFaxList() throws Exception {

        InterFAXClient interFAXClient = new InterFAXJerseyClient();
        InboundFaxStructure[] inboundFaxStructures = interFAXClient.getInboundFaxList();
        Assert.assertEquals(25, inboundFaxStructures.length);
        Assert.assertEquals(292957796, inboundFaxStructures[0].getMessageId());
    }

    @Test
    public void testGetInboundFaxListWithOptions() throws Exception {

        GetInboundFaxListOptions getInboundFaxListOptions = new GetInboundFaxListOptions();
        getInboundFaxListOptions.setAllUsers(Optional.of(true));
        getInboundFaxListOptions.setUnreadOnly(Optional.of(true));
        getInboundFaxListOptions.setLimit(Optional.of(3));
        InterFAXClient interFAXClient = new InterFAXJerseyClient();
        InboundFaxStructure[] inboundFaxStructures
                = interFAXClient.getInboundFaxList(Optional.of(getInboundFaxListOptions));
        Assert.assertEquals(3, inboundFaxStructures.length);
        Assert.assertEquals(292957783, inboundFaxStructures[2].getMessageId());
    }

    @Test
    public void testGetInboundFaxRecord() throws Exception {

        InterFAXClient interFAXClient = new InterFAXJerseyClient();
        InboundFaxStructure inboundFaxStructure = interFAXClient.getInboundFaxRecord("292626603");
        Assert.assertEquals(292626603, inboundFaxStructure.getMessageId());
        Assert.assertEquals(2, inboundFaxStructure.getPages());
    }

    @Test
    public void testGetInboundFaxImage() throws Exception {

        InterFAXClient interFAXClient = new InterFAXJerseyClient();
        byte[] faxImage = interFAXClient.getInboundFaxImage("292626603");
        Assert.assertEquals(37194, faxImage.length);
    }

    @Test
    public void testGetInboundFaxEmails() throws Exception {

        InterFAXClient interFAXClient = new InterFAXJerseyClient();
        InboundFaxesEmailsStructure inboundFaxesEmailsStructure = interFAXClient.getInboundFaxForwardingEmails("1234567");
        Assert.assertEquals("username@interfax.net", inboundFaxesEmailsStructure.getEmailAddress());
    }
}