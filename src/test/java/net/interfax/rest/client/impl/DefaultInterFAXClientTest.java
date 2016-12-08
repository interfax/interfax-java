package net.interfax.rest.client.impl;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import net.interfax.rest.client.InterFAX;
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
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Optional;


public class DefaultInterFAXClientTest {

    private String faxNumber = "+442084978672";

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);

    @Test
    public void testSendFax() throws Exception {

        String absoluteFilePath = this.getClass().getClassLoader().getResource("test.pdf").getFile();
        File file = new File(absoluteFilePath);

        InterFAX interFAX = new DefaultInterFAXClient();
        APIResponse apiResponse = interFAX.sendFax(faxNumber, file);
        Assert.assertEquals("[https://rest.interfax.net/outbound/faxes/666639902]", apiResponse.getHeaders().get("Location").toString());
        Assert.assertEquals(201, apiResponse.getStatusCode());
    }

    @Test
    public void testSendFaxWithOptions() throws Exception {

        String absoluteFilePath = this.getClass().getClassLoader().getResource("test.pdf").getFile();
        File file = new File(absoluteFilePath);

        SendFaxOptions sendFaxOptions = new SendFaxOptions();
        sendFaxOptions.setContact(Optional.of("testContactName"));

        InterFAX interFAX = new DefaultInterFAXClient();
        APIResponse apiResponse = interFAX.sendFax(faxNumber, file, Optional.of(sendFaxOptions));
        Assert.assertEquals("[https://rest.interfax.net/outbound/faxes/666639902]", apiResponse.getHeaders().get("Location").toString());
        Assert.assertEquals(201, apiResponse.getStatusCode());
    }

    @Test
    public void testSendFaxWithPageHeaderTemplateOptions() throws Exception {

        String absoluteFilePath = this.getClass().getClassLoader().getResource("test.pdf").getFile();
        File file = new File(absoluteFilePath);

        SendFaxOptions sendFaxOptions = new SendFaxOptions();
        String pageHeader = "To: {To} From: {From} Pages: {TotalPages}";
        sendFaxOptions.setPageHeader(Optional.of(pageHeader));

        InterFAX interFAX = new DefaultInterFAXClient();
        APIResponse apiResponse = interFAX.sendFax(faxNumber, file, Optional.of(sendFaxOptions));
        Assert.assertEquals(201, apiResponse.getStatusCode());
    }

    @Test
    public void testSendMultipleFilesAsFax() throws Exception {

        String absoluteFilePath = this.getClass().getClassLoader().getResource("test.pdf").getFile();
        File file1 = new File(absoluteFilePath);
        File file2 = new File(absoluteFilePath);

        File[] files = {file1, file2};

        InterFAX interFAX = new DefaultInterFAXClient();
        APIResponse apiResponse = interFAX.sendFax(faxNumber, files);
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

        InterFAX interFAX = new DefaultInterFAXClient();
        APIResponse apiResponse = interFAX.sendFax(faxNumber, files, Optional.of(sendFaxOptions));
        Assert.assertEquals("[https://rest.interfax.net/outbound/faxes/667457707]", apiResponse.getHeaders().get("Location").toString());
        Assert.assertEquals(201, apiResponse.getStatusCode());

    }
    
    @Test
    public void testSendMultipleInputStreamsAsFax() throws Exception {

        String absoluteFilePath = this.getClass().getClassLoader().getResource("test.pdf").getFile();
        InputStream inputStream1 = new FileInputStream(absoluteFilePath);
        InputStream inputStream2 = new FileInputStream(absoluteFilePath);

        InputStream[] inputStreams = {inputStream1, inputStream2};
        String[] fileNames = {"test.pdf", "test.pdf"};

        InterFAX interFAX = new DefaultInterFAXClient();
        APIResponse apiResponse = interFAX.sendFax(faxNumber, inputStreams, fileNames);
        Assert.assertEquals("[https://rest.interfax.net/outbound/faxes/667457707]", apiResponse.getHeaders().get("Location").toString());
        Assert.assertEquals(201, apiResponse.getStatusCode());
    }

    @Test
    public void testSendMultipleInputStreamsAsFaxWithOptions() throws Exception {

        String absoluteFilePath = this.getClass().getClassLoader().getResource("test.pdf").getFile();
        InputStream inputStream1 = new FileInputStream(absoluteFilePath);
        InputStream inputStream2 = new FileInputStream(absoluteFilePath);

        InputStream[] inputStreams = {inputStream1, inputStream2};
        String[] fileNames = {"test.pdf", "test.pdf"};

        SendFaxOptions sendFaxOptions = new SendFaxOptions();
        sendFaxOptions.setPageSize(Optional.of("a4"));

        InterFAX interFAX = new DefaultInterFAXClient();
        APIResponse apiResponse = interFAX.sendFax(faxNumber, inputStreams, fileNames, Optional.of(sendFaxOptions));
        Assert.assertEquals("[https://rest.interfax.net/outbound/faxes/667457707]", apiResponse.getHeaders().get("Location").toString());
        Assert.assertEquals(201, apiResponse.getStatusCode());

    }

    @Test
    public void testSendFaxUsingPreviouslyUploadedDocUrl() throws Exception {

        InterFAX interFAX = new DefaultInterFAXClient();
        APIResponse apiResponse = interFAX.sendFax(faxNumber, "https://rest.interfax.net/outbound/documents/90bd5477d5944c6d884c610171b75258");
        Assert.assertEquals(201, apiResponse.getStatusCode());
    }

    @Test
    public void testSendFaxUsingPreviouslyUploadedDocUrlWithOptions() throws Exception {

        SendFaxOptions sendFaxOptions = new SendFaxOptions();
        sendFaxOptions.setReplyAddress(Optional.of("reply@example.com"));

        InterFAX interFAX = new DefaultInterFAXClient();
        APIResponse apiResponse = interFAX.sendFax(
                                        faxNumber,
                                        "https://rest.interfax.net/outbound/documents/90bd5477d5944c6d884c610171b75258",
                                        Optional.of(sendFaxOptions));
        Assert.assertEquals(201, apiResponse.getStatusCode());
    }


    @Test
    public void testResendFax() throws Exception {

        InterFAX interFAX = new DefaultInterFAXClient();
        APIResponse apiResponse = interFAX.resendFax("667915471", Optional.empty());
        Assert.assertEquals(201, apiResponse.getStatusCode());
    }

    @Test
    public void testResendFaxWithOptions() throws Exception {

        InterFAX interFAX = new DefaultInterFAXClient();
        APIResponse apiResponse = interFAX.resendFax("667915471", Optional.of(faxNumber));
        Assert.assertEquals(201, apiResponse.getStatusCode());
    }

    @Test
    public void testGetFaxList() throws Exception {

        InterFAX interFAX = new DefaultInterFAXClient();
        OutboundFaxStructure[] outboundFaxStructures = interFAX.getFaxList();
        Assert.assertEquals(25, outboundFaxStructures.length);
    }

    @Test
    public void testGetFaxListWithOptions() throws Exception {

        GetFaxListOptions getFaxListOptions = new GetFaxListOptions();
        getFaxListOptions.setLimit(Optional.of(5));

        InterFAX interFAX = new DefaultInterFAXClient();
        OutboundFaxStructure[] outboundFaxStructures = interFAX.getFaxList(Optional.of(getFaxListOptions));
        Assert.assertEquals(5, outboundFaxStructures.length);
    }

    @Test
    public void testGetCompletedFaxList() throws Exception {

        InterFAX interFAX = new DefaultInterFAXClient();
        OutboundFaxStructure[] outboundFaxStructures = interFAX.getCompletedFaxList(new String[]{"667915751", "667915471"});
        Assert.assertEquals(2, outboundFaxStructures.length);
        Assert.assertEquals("667915751", outboundFaxStructures[1].getId());
    }

    @Test
    public void testGetFaxRecord() throws Exception {

        InterFAX interFAX = new DefaultInterFAXClient();
        OutboundFaxStructure outboundFaxStructure = interFAX.getFaxRecord("667915751");
        Assert.assertEquals("667915751", outboundFaxStructure.getId());
        Assert.assertEquals("2016-10-03T00:36:41", outboundFaxStructure.getSubmitTime());
    }

    @Test
    public void testGetOutboundFaxImage() throws Exception {

        InterFAX interFAX = new DefaultInterFAXClient();
        byte[] faxImage = interFAX.getOuboundFaxImage("667915751");
        Assert.assertEquals(30072, faxImage.length);
    }

    @Test(expected = UnsuccessfulStatusCodeException.class)
    public void testGetOutboundFaxImageWithInvalidId() throws Exception {

        try {
            InterFAX interFAX = new DefaultInterFAXClient();
            interFAX.getOuboundFaxImage("1234");
        } catch (UnsuccessfulStatusCodeException e) {
            Assert.assertEquals("Unsuccessful response from API", e.getMessage());
            Assert.assertEquals(404, e.getStatusCode());
            throw new UnsuccessfulStatusCodeException(e.getMessage());
        }
    }

    @Test
    public void testCancelFax() throws Exception {

        InterFAX interFAX = new DefaultInterFAXClient();
        APIResponse apiResponse = interFAX.cancelFax("279499862");
        Assert.assertEquals(404, apiResponse.getStatusCode());
    }

    @Test
    public void testSearchFaxList() throws Exception {

        InterFAX interFAX = new DefaultInterFAXClient();
        OutboundFaxStructure[] outboundFaxStructures = interFAX.searchFaxList();
        Assert.assertEquals(25, outboundFaxStructures.length);
        Assert.assertEquals("667915751", outboundFaxStructures[0].getId());
    }

    @Test
    public void testSearchFaxListWithOptions() throws Exception {

        SearchFaxOptions searchFaxOptions = new SearchFaxOptions();
        searchFaxOptions.setLimit(Optional.of(3));
        searchFaxOptions.setFaxNumber(Optional.of("+442084978672"));
        InterFAX interFAX = new DefaultInterFAXClient();
        OutboundFaxStructure[] outboundFaxStructures = interFAX.searchFaxList(Optional.of(searchFaxOptions));
        Assert.assertEquals(3, outboundFaxStructures.length);
        Assert.assertEquals("667915476", outboundFaxStructures[1].getId());
    }

    @Test
    public void testHideFax() throws Exception {

        InterFAX interFAX = new DefaultInterFAXClient();
        APIResponse apiResponse = interFAX.hideFax("667915469");
        Assert.assertEquals(200, apiResponse.getStatusCode());
    }

    @Test
    public void testUploadDocument() throws Exception {

        String absoluteFilePath = this.getClass().getClassLoader().getResource("A17_FlightPlan.pdf").getFile();
        File file = new File(absoluteFilePath);

        InterFAX interFAX = new DefaultInterFAXClient();
        APIResponse apiResponse = interFAX.uploadDocument(file);
        Assert.assertEquals(200, apiResponse.getStatusCode());
    }

    @Test
    public void testUploadDocumentWithOptions() throws Exception {

        String absoluteFilePath = this.getClass().getClassLoader().getResource("A17_FlightPlan.pdf").getFile();
        File file = new File(absoluteFilePath);

        InterFAX interFAX = new DefaultInterFAXClient();
        DocumentUploadSessionOptions documentUploadSessionOptions = new DocumentUploadSessionOptions();
        documentUploadSessionOptions.setName(Optional.of("overriddenname.pdf"));
        documentUploadSessionOptions.setSize(Optional.of(Integer.toUnsignedLong(12345)));
        documentUploadSessionOptions.setDisposition(Optional.of(Disposition.multiUse));
        documentUploadSessionOptions.setSharing(Optional.of(Sharing.privateDoc));
        APIResponse apiResponse = interFAX.uploadDocument(file, Optional.of(documentUploadSessionOptions));
        Assert.assertEquals(200, apiResponse.getStatusCode());
    }

    @Test
    public void testGetUploadedDocumentsList() throws Exception {

        InterFAX interFAX = new DefaultInterFAXClient();
        UploadedDocumentStatus[] uploadedDocumentStatuses = interFAX.getUploadedDocumentsList();

        Assert.assertEquals(2, uploadedDocumentStatuses.length);
        Assert.assertEquals("sampledoc.pdf", uploadedDocumentStatuses[0].getFileName());
        Assert.assertEquals("A17_FlightPlan.pdf", uploadedDocumentStatuses[1].getFileName());
    }

    @Test
    public void testGetUploadedDocumentsListWithOptions() throws Exception {

        InterFAX interFAX = new DefaultInterFAXClient();
        GetUploadedDocumentsListOptions getUploadedDocumentsListOptions = new GetUploadedDocumentsListOptions();
        getUploadedDocumentsListOptions.setLimit(Optional.of(5));
        getUploadedDocumentsListOptions.setOffset(Optional.of(1));
        UploadedDocumentStatus[] uploadedDocumentStatuses
                = interFAX.getUploadedDocumentsList(Optional.of(getUploadedDocumentsListOptions));

        Assert.assertEquals(2, uploadedDocumentStatuses.length);
        Assert.assertEquals("sampledoc.pdf", uploadedDocumentStatuses[0].getFileName());
        Assert.assertEquals("A17_FlightPlan.pdf", uploadedDocumentStatuses[1].getFileName());
    }

    @Test
    public void testGetUploadedDocumentStatus() throws Exception {

        InterFAX interFAX = new DefaultInterFAXClient();
        UploadedDocumentStatus uploadedDocumentStatus = interFAX.getUploadedDocumentStatus("deca890355b44b42944970d9773962b5");

        Assert.assertEquals("A17_FlightPlan.pdf", uploadedDocumentStatus.getFileName());
    }

    @Test
    public void testCancelDocumentUploadSession() throws Exception {

        InterFAX interFAX = new DefaultInterFAXClient();
        APIResponse apiResponse = interFAX.cancelDocumentUploadSession("deca890355b44b42944970d9773962b5");

        Assert.assertEquals(200, apiResponse.getStatusCode());
    }

    @Test
    public void testGetAccountCredits() throws Exception {

        InterFAX interFAX = new DefaultInterFAXClient();
        Double balance = interFAX.getAccountCredits();
        Assert.assertEquals(Double.valueOf(3.8500), balance);
    }

    @Test
    public void testGetInboundFaxList() throws Exception {

        InterFAX interFAX = new DefaultInterFAXClient();
        InboundFaxStructure[] inboundFaxStructures = interFAX.getInboundFaxList();
        Assert.assertEquals(25, inboundFaxStructures.length);
        Assert.assertEquals(292957796, inboundFaxStructures[0].getMessageId());
    }

    @Test
    public void testGetInboundFaxListWithOptions() throws Exception {

        GetInboundFaxListOptions getInboundFaxListOptions = new GetInboundFaxListOptions();
        getInboundFaxListOptions.setAllUsers(Optional.of(true));
        getInboundFaxListOptions.setUnreadOnly(Optional.of(true));
        getInboundFaxListOptions.setLimit(Optional.of(3));
        InterFAX interFAX = new DefaultInterFAXClient();
        InboundFaxStructure[] inboundFaxStructures
                = interFAX.getInboundFaxList(Optional.of(getInboundFaxListOptions));
        Assert.assertEquals(3, inboundFaxStructures.length);
        Assert.assertEquals(292957783, inboundFaxStructures[2].getMessageId());
    }

    @Test
    public void testGetInboundFaxRecord() throws Exception {

        InterFAX interFAX = new DefaultInterFAXClient();
        InboundFaxStructure inboundFaxStructure = interFAX.getInboundFaxRecord("292626603");
        Assert.assertEquals(292626603, inboundFaxStructure.getMessageId());
        Assert.assertEquals(2, inboundFaxStructure.getPages());
    }

    @Test
    public void testGetInboundFaxImage() throws Exception {

        InterFAX interFAX = new DefaultInterFAXClient();
        byte[] faxImage = interFAX.getInboundFaxImage("292626603");
        Assert.assertEquals(37194, faxImage.length);
    }

    @Test
    public void testGetInboundFaxForwardingEmails() throws Exception {

        InterFAX interFAX = new DefaultInterFAXClient();
        InboundFaxesEmailsStructure inboundFaxesEmailsStructure = interFAX.getInboundFaxForwardingEmails("1234567");
        Assert.assertEquals("username@interfax.net", inboundFaxesEmailsStructure.getEmailAddress());
    }

    @Test
    public void testMarkInboundFax() throws Exception {

        InterFAX interFAX = new DefaultInterFAXClient();
        APIResponse apiResponse = interFAX.markInboundFax("292626603", Optional.empty());
        Assert.assertEquals(200, apiResponse.getStatusCode());
        Assert.assertEquals("true", apiResponse.getResponseBody());
    }

    @Test
    public void testResendInboundFax() throws Exception {

        InterFAX interFAX = new DefaultInterFAXClient();
        APIResponse apiResponse = interFAX.resendInboundFax("292626603", Optional.of("someone@example.com"));
        Assert.assertEquals(200, apiResponse.getStatusCode());
        Assert.assertEquals("true", apiResponse.getResponseBody());
    }
}