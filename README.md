# InterFAX Java Library

[![Build Status](https://travis-ci.org/interfax/interfax-java.svg?branch=master)](https://travis-ci.org/interfax/interfax-java) | [![Maven Central](https://maven-badges.herokuapp.com/maven-central/net.interfax/api-client/badge.svg)](https://maven-badges.herokuapp.com/maven-central/net.interfax/api-client)

[Installation](#installation) | [Getting Started](#getting-started) | [Usage](#usage) | [Contributing](#contributing) | [License](#license)

Send and receive faxes in Java with the [InterFAX REST API](https://www.interfax.net/en/dev/rest/reference).

## Installation

Use of the library requires Java 8 or higher and can be done via one of
the following approaches. 

### Include as maven dependency

```xml
<dependency>
  <groupId>net.interfax</groupId>
  <artifactId>api-client</artifactId>
  <version>${version-number}</version>
</dependency>
```
    
Replace `${version-number}` with your preferred version number   

### Download jar and include in application classpath

Download the latest jar from the [Maven central repository](http://search.maven.org/#search%7Cga%7C1%7Cnet.interfax.api-client)
and place it in your application classpath. 

## Getting started

### Pre-requisites

To use this library you will need a InterFAX [developer account](https://www.interfax.net/en/dev).

### Initialise the client

Once you have the API credentials in the form of a username and 
password, the simplest way to get started is to initialise a new 
`InterFAX` object, using the [Jersey](https://jersey.java.net/) based 
HTTP client implementation, passing the API username and password as 
constructor arguments as shown below.

    InterFAX interFAX = new DefaultInterFAXClient("api-username", "api-password");
        
For alternate ways of passing API credentials, including via a `yaml` 
file and environment variables, refer to: [api-credential-configuration.md](docs/api-credential-configuration.md)

## Usage

[Initialise](#initialise-the-client) | [Account](#account) | [Outbound](#outbound) | [Inbound](#inbound) | [Documents](#documents)

## Account

### Credit balance

Determine the remaining faxing credits in your account

    InterFAX interFAX = new DefaultInterFAXClient();
    Double balance = interFAX.getAccountCredits();
    
Additional info: 

* [REST API Documentation](https://www.interfax.net/en/dev/rest/reference/3001)
* [Usage example - DefaultInterFAXClientTest class, `testGetAccountCredits` method](src/test/java/net/interfax/rest/client/impl/DefaultInterFAXClientTest.java))  

## Outbound

[Send](#send-fax) | [Get list](#get-outbound-fax-list) | [Get completed list](#get-completed-fax-list) | [Get record](#get-outbound-fax-record) | [Get image](#get-outbound-fax-image) | [Cancel fax](#cancel-a-fax) | [Search](#search-fax-list) | [Hide fax](#hide-fax)

### Send Fax

To send a fax, simply invoke the [`sendFax`](src/main/java/net/interfax/rest/client/InterFAX.java)
method of the `InterFAX` class.

Example:

    java.io.File file = new File(absoluteFilePath);
    InterFAX interFAX = new DefaultInterFAXClient();
    APIResponse apiResponse = interFAX.sendFax(faxNumber, file);

For additional options, including sending an array of files as a fax and
sending an pre-uploaded document as a fax, the following methods may be
used

    public APIResponse sendFax(final String faxNumber,
                               final File fileToSendAsFax,
                               final Optional<SendFaxOptions> options) 
                               throws IOException;
                               
    public APIResponse sendFax(final String faxNumber, 
                               final File[] filesToSendAsFax) 
                               throws IOException;
                                                              
    public APIResponse sendFax(final String faxNumber,
                                   final File[] filesToSendAsFax,
                                   final Optional<SendFaxOptions> options) 
                                   throws IOException;
                                                                                                 
    public APIResponse sendFax(final String faxNumber, 
                               final String urlOfDoc);
                                                                                                                                
    public APIResponse sendFax(final String faxNumber, 
                               final String urlOfDoc, 
                               final Optional<SendFaxOptions> options);                                                                                                                                
                               
Refer the the accompanying javadocs in [InterFAX](src/main/java/net/interfax/rest/client/InterFAX.java)

Additional info: 

* [REST API Documentation](https://www.interfax.net/en/dev/rest/reference/2918)
* [Usage example - DefaultInterFAXClientTest class](src/test/java/net/interfax/rest/client/impl/DefaultInterFAXClientTest.java))
                              
### Get Outbound Fax List
                              
Get a list of recent outbound faxes (which does not include batch faxes).

    InterFAX interFAX = new DefaultInterFAXClient();
    OutboundFaxStructure[] outboundFaxStructures = interFAX.getFaxList();

Using additional options,
    
    GetFaxListOptions getFaxListOptions = new GetFaxListOptions();
    getFaxListOptions.setLimit(Optional.of(5));

    InterFAX interFAX = new DefaultInterFAXClient();
    OutboundFaxStructure[] outboundFaxStructures = interFAX.getFaxList(Optional.of(getFaxListOptions));    
    
Additional info: 

* [REST API Documentation](https://www.interfax.net/en/dev/rest/reference/2920)
* [Usage example - DefaultInterFAXClientTest class](src/test/java/net/interfax/rest/client/impl/DefaultInterFAXClientTest.java))    

### Get Completed Fax List

Get details for a subset of completed faxes from a submitted list. 
(Submitted id's which have not completed are ignored).

    InterFAX interFAX = new DefaultInterFAXClient();
    OutboundFaxStructure[] outboundFaxStructures = interFAX.getCompletedFaxList(new String[]{"667915751", "667915471"});
    
Additional info: 

* [REST API Documentation](https://www.interfax.net/en/dev/rest/reference/2972)
* [Usage example - DefaultInterFAXClientTest class](src/test/java/net/interfax/rest/client/impl/DefaultInterFAXClientTest.java))
    
### Get Outbound Fax Record
    
Retrieves information regarding a previously-submitted fax, including 
its current status.
    
    InterFAX interFAX = new DefaultInterFAXClient();
    OutboundFaxStructure outboundFaxStructure = interFAX.getFaxRecord("667915751");    
    
Additional info: 

* [REST API Documentation](https://www.interfax.net/en/dev/rest/reference/2921)
* [Usage example - DefaultInterFAXClientTest class](src/test/java/net/interfax/rest/client/impl/DefaultInterFAXClientTest.java))    
    
### Get Outbound Fax Image
    
Retrieve the fax image (TIFF file) of a submitted fax.
    
    InterFAX interFAX = new DefaultInterFAXClient();
    byte[] faxImage = interFAX.getOuboundFaxImage("667915751");
    
Additional info: 

* [REST API Documentation](https://www.interfax.net/en/dev/rest/reference/2941)
* [Usage example - DefaultInterFAXClientTest class](src/test/java/net/interfax/rest/client/impl/DefaultInterFAXClientTest.java))    
    
### Cancel a Fax
    
Cancel a fax in progress.
    
    InterFAX interFAX = new DefaultInterFAXClient();
    APIResponse apiResponse = interFAX.cancelFax("279499862");
        
Additional info: 

* [REST API Documentation](https://www.interfax.net/en/dev/rest/reference/2939)
* [Usage example - DefaultInterFAXClientTest class](src/test/java/net/interfax/rest/client/impl/DefaultInterFAXClientTest.java))        

### Search Fax List

Search for outbound faxes.

    InterFAX interFAX = new DefaultInterFAXClient();
    OutboundFaxStructure[] outboundFaxStructures = interFAX.searchFaxList();        

Using additional options,

    SearchFaxOptions searchFaxOptions = new SearchFaxOptions();
    searchFaxOptions.setLimit(Optional.of(3));
    searchFaxOptions.setFaxNumber(Optional.of("+442084978672"));
    InterFAX interFAX = new DefaultInterFAXClient();
    OutboundFaxStructure[] outboundFaxStructures = interFAX.searchFaxList(Optional.of(searchFaxOptions));    
    
Additional info: 

* [REST API Documentation](https://www.interfax.net/en/dev/rest/reference/2959)
* [Usage example - DefaultInterFAXClientTest class](src/test/java/net/interfax/rest/client/impl/DefaultInterFAXClientTest.java))    

### Hide Fax

Hide a fax from listing in queries (there is no way to unhide a fax).

    InterFAX interFAX = new DefaultInterFAXClient();
    APIResponse apiResponse = interFAX.hideFax("667915469");

Additional info: 

* [REST API Documentation](https://www.interfax.net/en/dev/rest/reference/2940)
* [Usage example - DefaultInterFAXClientTest class](src/test/java/net/interfax/rest/client/impl/DefaultInterFAXClientTest.java))

## Inbound

[Get list](#get-inbound-fax-list) | [Get record](#get-inbound-fax-record) | [Get image](#get-inbound-fax-image) | [Get emails](#get-forwarding-emails) | [Mark as read](#mark-as-readunread) | [Resend to email](#resend-inbound-fax)

### Get Inbound Fax List

Retrieves a user's list of inbound faxes. (Sort order is always in 
descending ID).

    InterFAX interFAX = new DefaultInterFAXClient();
    InboundFaxStructure[] inboundFaxStructures = interFAX.getInboundFaxList();
    
Using additional options,
    
    GetInboundFaxListOptions getInboundFaxListOptions = new GetInboundFaxListOptions();
    getInboundFaxListOptions.setAllUsers(Optional.of(true));
    getInboundFaxListOptions.setUnreadOnly(Optional.of(true));
    getInboundFaxListOptions.setLimit(Optional.of(3));
    InterFAX interFAX = new DefaultInterFAXClient();
    InboundFaxStructure[] inboundFaxStructures = interFAX.getInboundFaxList(Optional.of(getInboundFaxListOptions));    
    
Additional info: 

* [REST API Documentation](https://www.interfax.net/en/dev/rest/reference/2935)
* [Usage example - DefaultInterFAXClientTest class](src/test/java/net/interfax/rest/client/impl/DefaultInterFAXClientTest.java))

### Get Inbound Fax Record

Retrieves a single fax's metadata (receive time, sender number, etc.).

    InterFAX interFAX = new DefaultInterFAXClient();
    InboundFaxStructure inboundFaxStructure = interFAX.getInboundFaxRecord("292626603");

Additional info: 

* [REST API Documentation](https://www.interfax.net/en/dev/rest/reference/2938)
* [Usage example - DefaultInterFAXClientTest class](src/test/java/net/interfax/rest/client/impl/DefaultInterFAXClientTest.java))
    
### Get Inbound Fax Image

Retrieves a single fax's image.

    InterFAX interFAX = new DefaultInterFAXClient();
    byte[] faxImage = interFAX.getInboundFaxImage("292626603");
    
Additional info: 

* [REST API Documentation](https://www.interfax.net/en/dev/rest/reference/2937)
* [Usage example - DefaultInterFAXClientTest class](src/test/java/net/interfax/rest/client/impl/DefaultInterFAXClientTest.java))   
 
### Get Forwarding Emails
 
Retrieve the list of email addresses to which a fax was forwarded.
 
    InterFAX interFAX = new DefaultInterFAXClient();
    InboundFaxesEmailsStructure inboundFaxesEmailsStructure = interFAX.getInboundFaxForwardingEmails("1234567");
     
Additional info: 

* [REST API Documentation](https://www.interfax.net/en/dev/rest/reference/2930)
* [Usage example - DefaultInterFAXClientTest class](src/test/java/net/interfax/rest/client/impl/DefaultInterFAXClientTest.java))
     
### Mark As Read/Unread

Mark a transaction as read/unread.
     
    InterFAX interFAX = new DefaultInterFAXClient();
    APIResponse apiResponse = interFAX.markInboundFax("292626603", Optional.of(true));
    
Additional info: 

* [REST API Documentation](https://www.interfax.net/en/dev/rest/reference/2936)
* [Usage example - DefaultInterFAXClientTest class](src/test/java/net/interfax/rest/client/impl/DefaultInterFAXClientTest.java))
    
### Resend Inbound Fax

Resend an inbound fax to a specific email address.

    InterFAX interFAX = new DefaultInterFAXClient();
    APIResponse apiResponse = interFAX.resendInboundFax("292626603", Optional.of("someone@example.com"));
    
Additional info: 

* [REST API Documentation](https://www.interfax.net/en/dev/rest/reference/2929)
* [Usage example - DefaultInterFAXClientTest class](src/test/java/net/interfax/rest/client/impl/DefaultInterFAXClientTest.java))    

## Documents

[Upload Document](#upload-document) | [Get list](#get-document-list) | [Status](#get-document-status) | [Get Upload Status](Get-upload-status) | [Cancel](#cancel-document)

Document uploads are useful for several situations:

* When your documents are larger than our [System Limitations](https://www.interfax.net/en/help/limitations) 
allow and you want to submit them in chunks.
* When you plan to reuse a document for multiple faxes.
* When you want a document to be available for use by other users in 
your account.

### Upload Document

Upload a large file in 1 MB chunks

    String absoluteFilePath = this.getClass().getClassLoader().getResource("A17_FlightPlan.pdf").getFile();
    File file = new File(absoluteFilePath);
    
    InterFAX interFAX = new DefaultInterFAXClient();
    APIResponse apiResponse = interFAX.uploadDocument(file);
    
With additional options,
    
    String absoluteFilePath = this.getClass().getClassLoader().getResource("A17_FlightPlan.pdf").getFile();
    File file = new File(absoluteFilePath);
    
    InterFAX interFAX = new DefaultInterFAXClient();
    DocumentUploadSessionOptions documentUploadSessionOptions = new DocumentUploadSessionOptions();
    documentUploadSessionOptions.setName(Optional.of("overriddenname.pdf"));
    documentUploadSessionOptions.setSize(Optional.of(Integer.toUnsignedLong(12345)));
    documentUploadSessionOptions.setDisposition(Optional.of(Disposition.multiUse));
    documentUploadSessionOptions.setSharing(Optional.of(Sharing.privateDoc));
    APIResponse apiResponse = interFAX.uploadDocument(file, Optional.of(documentUploadSessionOptions));
        
Additional info: 

* [REST API Documentation - 1](https://www.interfax.net/en/dev/rest/reference/2967), [2](https://www.interfax.net/en/dev/rest/reference/2966) 
* [Usage example - DefaultInterFAXClientTest class](src/test/java/net/interfax/rest/client/impl/DefaultInterFAXClientTest.java))
        
### Get Document List

Get a list of previous document uploads which are currently available.

    InterFAX interFAX = new DefaultInterFAXClient();
    UploadedDocumentStatus[] uploadedDocumentStatuses = interFAX.getUploadedDocumentsList();
    
With additional options,
    
    InterFAX interFAX = new DefaultInterFAXClient();
    GetUploadedDocumentsListOptions getUploadedDocumentsListOptions = new GetUploadedDocumentsListOptions();
    getUploadedDocumentsListOptions.setLimit(Optional.of(5));
    getUploadedDocumentsListOptions.setOffset(Optional.of(1));
    UploadedDocumentStatus[] uploadedDocumentStatuses = interFAX.getUploadedDocumentsList(Optional.of(getUploadedDocumentsListOptions));    
        
Additional info: 

* [REST API Documentation](https://www.interfax.net/en/dev/rest/reference/2968)
* [Usage example - DefaultInterFAXClientTest class](src/test/java/net/interfax/rest/client/impl/DefaultInterFAXClientTest.java))

### Get Upload Status

Get the current status of a specific document upload.

    InterFAX interFAX = new DefaultInterFAXClient();
    UploadedDocumentStatus uploadedDocumentStatus = interFAX.getUploadedDocumentStatus("deca890355b44b42944970d9773962b5");
    
Additional info: 

* [REST API Documentation](https://www.interfax.net/en/dev/rest/reference/2965)
* [Usage example - DefaultInterFAXClientTest class](src/test/java/net/interfax/rest/client/impl/DefaultInterFAXClientTest.java))    

### Cancel Document

Cancel a document upload and tear down the upload session, or delete a 
previous upload.

    InterFAX interFAX = new DefaultInterFAXClient();
    APIResponse apiResponse = interFAX.cancelDocumentUploadSession("deca890355b44b42944970d9773962b5");

Additional info: 

* [REST API Documentation](https://www.interfax.net/en/dev/rest/reference/2964)
* [Usage example - DefaultInterFAXClientTest class](src/test/java/net/interfax/rest/client/impl/DefaultInterFAXClientTest.java))      
  

## Contributing

 1. **Fork** the repo on GitHub
 2. **Clone** the project to your own machine
 3. **Commit** changes to your own branch
 4. **Test** the changes you have made
 5. **Push** your work back up to your fork
 6. Submit a **Pull request** so that we can review your changes
   
### Testing

Before submitting a contribution please ensure all tests pass.
   
The project is setup using maven and tests can be run using the 
following command:

    mvn clean test
    
## License

This library is released under the [MIT License](LICENCE).    
        
    
         
         