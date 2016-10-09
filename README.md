# InterFAX Java Library

[![Build Status](https://travis-ci.org/interfax/interfax-java.svg?branch=master)](https://travis-ci.org/interfax/interfax-java)

[Installation](#installation) | [Getting Started](#getting-started) | [Usage](#usage) | [Contributing](#contributing) | [License](#license)

Send and receive faxes in Java with the [InterFAX REST API](https://www.interfax.net/en/dev/rest/reference).

## Installation

You can use the library using one of the following approaches 

### Include the library as a maven dependency

    <dependency>
      <groupId>net.interfax</groupId>
      <artifactId>api-client</artifactId>
      <version>0.1.0</version>
    </dependency>

### Download the jar file and include in your application's classpath

Download the latest version from the [OSS Sonatype InterFAX repository](https://oss.sonatype.org/service/local/repositories/releases/content/net/interfax/api-client/0.1.0/api-client-0.1.0.jar)

## Getting started

### Pre-requisite(s)

The pre-requisite to using the InterFAX API is to have API credentials,
which can be done by registering for a [developer account](https://secure.interfax.net/Default.aspx?Lang=en&Target=RegistrationService&Method=DisplayForm&BPCode=).

### Initialise the client

Once you have the API credentials in the form of a username and 
password, the simplest way to get started is to initialise a new 
`InterFAXClient`, using the [Jersey](https://jersey.java.net/) based 
HTTP client implementation, passing the API username and password as 
constructor arguments as shown below.

    InterFAXClient interFAXClient = new InterFAXJerseyClient("api-username", "api-password");
        
For alternate ways of passing API credentials, including via a `yaml` 
file and environment variables, refer to: [api-credential-configuration.md](docs/api-credential-configuration.md)

## Usage

[Initialise](#initialise-the-client) | [Account](#account) | [Outbound](#outbound) | [Inbound](#inbound) | [Documents](#documents)

## Account

### Credit balance

Determine the remaining faxing credits in your account

    InterFAXClient interFAXClient = new InterFAXJerseyClient();
    Double balance = interFAXClient.getAccountCredits();
    
Additional info: 

* [REST API Documentation](https://www.interfax.net/en/dev/rest/reference/3001)
* [Usage example - InterFAXJerseyClientTest class, `testGetAccountCredits` method](src/test/java/net/interfax/rest/client/impl/InterFAXJerseyClientTest.java))  

## Outbound

[Send](#send-fax) | [Get list](#get-outbound-fax-list) | [Get completed list](#get-completed-fax-list) | [Get record](#get-outbound-fax-record) | [Get image](#get-outbound-fax-image) | [Cancel fax](#cancel-a-fax) | [Search](#search-fax-list) | [Hide fax](#hide-fax)

### Send Fax

To send a fax, simply invoke the [`sendFax`](src/main/java/net/interfax/rest/client/InterFAXClient.java)
method of the `InterFAXClient`.

Example:

    java.io.File file = new File(absoluteFilePath);
    InterFAXClient interFAXClient = new InterFAXJerseyClient();
    APIResponse apiResponse = interFAXClient.sendFax(faxNumber, file);

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
                               
Refer the the accompanying javadocs in [InterFAXClient](src/main/java/net/interfax/rest/client/InterFAXClient.java)

Additional info: 

* [REST API Documentation](https://www.interfax.net/en/dev/rest/reference/2918)
* [Usage example - InterFAXJerseyClientTest class](src/test/java/net/interfax/rest/client/impl/InterFAXJerseyClientTest.java))
                              
### Get Outbound Fax List
                              
Get a list of recent outbound faxes (which does not include batch faxes).

    InterFAXClient interFAXClient = new InterFAXJerseyClient();
    OutboundFaxStructure[] outboundFaxStructures = interFAXClient.getFaxList();

Using additional options,
    
    GetFaxListOptions getFaxListOptions = new GetFaxListOptions();
    getFaxListOptions.setLimit(Optional.of(5));

    InterFAXClient interFAXClient = new InterFAXJerseyClient();
    OutboundFaxStructure[] outboundFaxStructures = interFAXClient.getFaxList(Optional.of(getFaxListOptions));    
    
Additional info: 

* [REST API Documentation](https://www.interfax.net/en/dev/rest/reference/2920)
* [Usage example - InterFAXJerseyClientTest class](src/test/java/net/interfax/rest/client/impl/InterFAXJerseyClientTest.java))    

### Get Completed Fax List

Get details for a subset of completed faxes from a submitted list. 
(Submitted id's which have not completed are ignored).

    InterFAXClient interFAXClient = new InterFAXJerseyClient();
    OutboundFaxStructure[] outboundFaxStructures = interFAXClient.getCompletedFaxList(new String[]{"667915751", "667915471"});
    
Additional info: 

* [REST API Documentation](https://www.interfax.net/en/dev/rest/reference/2972)
* [Usage example - InterFAXJerseyClientTest class](src/test/java/net/interfax/rest/client/impl/InterFAXJerseyClientTest.java))
    
### Get Outbound Fax Record
    
Retrieves information regarding a previously-submitted fax, including 
its current status.
    
    InterFAXClient interFAXClient = new InterFAXJerseyClient();
    OutboundFaxStructure outboundFaxStructure = interFAXClient.getFaxRecord("667915751");    
    
Additional info: 

* [REST API Documentation](https://www.interfax.net/en/dev/rest/reference/2921)
* [Usage example - InterFAXJerseyClientTest class](src/test/java/net/interfax/rest/client/impl/InterFAXJerseyClientTest.java))    
    
### Get Outbound Fax Image
    
Retrieve the fax image (TIFF file) of a submitted fax.
    
    InterFAXClient interFAXClient = new InterFAXJerseyClient();
    byte[] faxImage = interFAXClient.getOuboundFaxImage("667915751");
    
Additional info: 

* [REST API Documentation](https://www.interfax.net/en/dev/rest/reference/2941)
* [Usage example - InterFAXJerseyClientTest class](src/test/java/net/interfax/rest/client/impl/InterFAXJerseyClientTest.java))    
    
### Cancel a Fax
    
Cancel a fax in progress.
    
    InterFAXClient interFAXClient = new InterFAXJerseyClient();
    APIResponse apiResponse = interFAXClient.cancelFax("279499862");
        
Additional info: 

* [REST API Documentation](https://www.interfax.net/en/dev/rest/reference/2939)
* [Usage example - InterFAXJerseyClientTest class](src/test/java/net/interfax/rest/client/impl/InterFAXJerseyClientTest.java))        

### Search Fax List

Search for outbound faxes.

    InterFAXClient interFAXClient = new InterFAXJerseyClient();
    OutboundFaxStructure[] outboundFaxStructures = interFAXClient.searchFaxList();        

Using additional options,

    SearchFaxOptions searchFaxOptions = new SearchFaxOptions();
    searchFaxOptions.setLimit(Optional.of(3));
    searchFaxOptions.setFaxNumber(Optional.of("+442084978672"));
    InterFAXClient interFAXClient = new InterFAXJerseyClient();
    OutboundFaxStructure[] outboundFaxStructures = interFAXClient.searchFaxList(Optional.of(searchFaxOptions));    
    
Additional info: 

* [REST API Documentation](https://www.interfax.net/en/dev/rest/reference/2959)
* [Usage example - InterFAXJerseyClientTest class](src/test/java/net/interfax/rest/client/impl/InterFAXJerseyClientTest.java))    

### Hide Fax

Hide a fax from listing in queries (there is no way to unhide a fax).

    InterFAXClient interFAXClient = new InterFAXJerseyClient();
    APIResponse apiResponse = interFAXClient.hideFax("667915469");

Additional info: 

* [REST API Documentation](https://www.interfax.net/en/dev/rest/reference/2940)
* [Usage example - InterFAXJerseyClientTest class](src/test/java/net/interfax/rest/client/impl/InterFAXJerseyClientTest.java))

## Inbound

[Get list](#get-inbound-fax-list) | [Get record](#get-inbound-fax-record) | [Get image](#get-inbound-fax-image) | [Get emails](#get-forwarding-emails) | [Mark as read](#mark-as-readunread) | [Resend to email](#resend-inbound-fax)

### Get Inbound Fax List

Retrieves a user's list of inbound faxes. (Sort order is always in 
descending ID).

    InterFAXClient interFAXClient = new InterFAXJerseyClient();
    InboundFaxStructure[] inboundFaxStructures = interFAXClient.getInboundFaxList();
    
Using additional options,
    
    GetInboundFaxListOptions getInboundFaxListOptions = new GetInboundFaxListOptions();
    getInboundFaxListOptions.setAllUsers(Optional.of(true));
    getInboundFaxListOptions.setUnreadOnly(Optional.of(true));
    getInboundFaxListOptions.setLimit(Optional.of(3));
    InterFAXClient interFAXClient = new InterFAXJerseyClient();
    InboundFaxStructure[] inboundFaxStructures = interFAXClient.getInboundFaxList(Optional.of(getInboundFaxListOptions));    
    
Additional info: 

* [REST API Documentation](https://www.interfax.net/en/dev/rest/reference/2935)
* [Usage example - InterFAXJerseyClientTest class](src/test/java/net/interfax/rest/client/impl/InterFAXJerseyClientTest.java))

### Get Inbound Fax Record

Retrieves a single fax's metadata (receive time, sender number, etc.).

    InterFAXClient interFAXClient = new InterFAXJerseyClient();
    InboundFaxStructure inboundFaxStructure = interFAXClient.getInboundFaxRecord("292626603");

Additional info: 

* [REST API Documentation](https://www.interfax.net/en/dev/rest/reference/2938)
* [Usage example - InterFAXJerseyClientTest class](src/test/java/net/interfax/rest/client/impl/InterFAXJerseyClientTest.java))
    
### Get Inbound Fax Image

Retrieves a single fax's image.

    InterFAXClient interFAXClient = new InterFAXJerseyClient();
    byte[] faxImage = interFAXClient.getInboundFaxImage("292626603");
    
Additional info: 

* [REST API Documentation](https://www.interfax.net/en/dev/rest/reference/2937)
* [Usage example - InterFAXJerseyClientTest class](src/test/java/net/interfax/rest/client/impl/InterFAXJerseyClientTest.java))   
 
### Get Forwarding Emails
 
Retrieve the list of email addresses to which a fax was forwarded.
 
    InterFAXClient interFAXClient = new InterFAXJerseyClient();
    InboundFaxesEmailsStructure inboundFaxesEmailsStructure = interFAXClient.getInboundFaxForwardingEmails("1234567");
     
Additional info: 

* [REST API Documentation](https://www.interfax.net/en/dev/rest/reference/2930)
* [Usage example - InterFAXJerseyClientTest class](src/test/java/net/interfax/rest/client/impl/InterFAXJerseyClientTest.java))
     
### Mark As Read/Unread

Mark a transaction as read/unread.
     
    InterFAXClient interFAXClient = new InterFAXJerseyClient();
    APIResponse apiResponse = interFAXClient.markInboundFax("292626603", Optional.of(true));
    
Additional info: 

* [REST API Documentation](https://www.interfax.net/en/dev/rest/reference/2936)
* [Usage example - InterFAXJerseyClientTest class](src/test/java/net/interfax/rest/client/impl/InterFAXJerseyClientTest.java))
    
### Resend Inbound Fax

Resend an inbound fax to a specific email address.

    InterFAXClient interFAXClient = new InterFAXJerseyClient();
    APIResponse apiResponse = interFAXClient.resendInboundFax("292626603", Optional.of("someone@example.com"));
    
Additional info: 

* [REST API Documentation](https://www.interfax.net/en/dev/rest/reference/2929)
* [Usage example - InterFAXJerseyClientTest class](src/test/java/net/interfax/rest/client/impl/InterFAXJerseyClientTest.java))    
    
         
         