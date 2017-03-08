# InterFAX Java Library

[![Build Status](https://travis-ci.org/interfax/interfax-java.svg?branch=master)](https://travis-ci.org/interfax/interfax-java) | [![Maven Central](https://maven-badges.herokuapp.com/maven-central/net.interfax/api-client/badge.svg)](https://maven-badges.herokuapp.com/maven-central/net.interfax/api-client)

[Installation](#installation) | [Getting Started](#getting-started) | [Usage](#usage) | [Contributing](#contributing) | [License](#license)

Send and receive faxes in Java with the [InterFAX](https://www.interfax.net/en/dev) REST API.

## Installation

Use of the library requires Java 8 or higher and can be done via one of the following approaches. 

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

To send a fax from a PDF file:

```java
java.io.File file = new File(absoluteFilePath);
InterFAX interFAX = new DefaultInterFAXClient("username", "password");
APIResponse apiResponse = interFAX.sendFax(faxNumber, file);
```

## Usage

[Client](#client) | [Account](#account) | [Outbound](#outbound) | [Inbound](#inbound) | [Documents](#documents)

## Client

The client follows the [12-factor](http://12factor.net/config) apps principle and can be either set directly or via 
environment variables.

```java
// Initialize using parameters
InterFAX interFAX = new DefaultInterFAXClient("username", "password");

// Alternative 1: Initialize using environment variables
// Ensure following env vars are initialized with values of your API credentials
// * INTERFAX_USERNAME
// * INTERFAX_PASSWORD
InterFAX interFAX = new DefaultInterFAXClient();

// Alternative 2: Initialize using yaml file
// Create a file called `interfax-api-credentials.yaml` with the following contents, replacing the value of `username`
// and `password` fields with those of your API credentials.
//   username: "api-username"
//   password: "api-password"
InterFAX interFAX = new DefaultInterFAXClient();
```

All connections are established over HTTPS.

## Account

### Credit balance

Determine the remaining faxing credits in your account

```java
InterFAX interFAX = new DefaultInterFAXClient();
Double balance = interFAX.getAccountCredits();
```    

**More:** [documentation](https://www.interfax.net/en/dev/rest/reference/3001)  

## Outbound

[Send](#send-fax) | [Get list](#get-outbound-fax-list) | [Get completed list](#get-completed-fax-list) | [Get record](#get-outbound-fax-record) | [Get image](#get-outbound-fax-image) | [Cancel fax](#cancel-a-fax) | [Search](#search-fax-list) | [Hide fax](#hide-fax)

### Send Fax

Submit a fax to a single destination number.

There are a few ways to send a fax. One way is to directly provide a file path or url.

```java
// with an absoluteFilePath
java.io.File file = new File(absoluteFilePath);
InterFAX interFAX = new DefaultInterFAXClient();
APIResponse apiResponse = interFAX.sendFax(faxNumber, file);

// with an inputstream
InputStream[] inputStreams = {inputStream};
String[] mediaTypes = {"application/pdf"};
InterFAX interFAX = new DefaultInterFAXClient();
APIResponse apiResponse = interFAX.sendFax(faxNumber, inputStreams, mediaTypes);

// with a URL
InterFAX interFAX = new DefaultInterFAXClient();
APIResponse apiResponse = interFAX.sendFax(faxNumber, "https://s3.aws.com/example/fax.html");
```

InterFAX supports over 20 file types including HTML, PDF, TXT, Word, and many more. For a full list see the 
[Supported File Types](https://www.interfax.net/en/help/supported_file_types) documentation.

The returned object is a [`APIResponse`](src/main/java/net/interfax/rest/client/domain/APIResponse.java) with the 
`statusCode` and `responseBody` of the request submitted to InterFAX.

To send multiple files just pass in an array of files or inputstreams
```java
// using Files
public APIResponse sendFax(final String faxNumber, 
                           final File[] filesToSendAsFax) 
                           throws IOException;
                           
// using Inputstreams
public APIResponse sendFax(final String faxNumber,
                           final InputStream[] streamsToSendAsFax,
                           final String mediaTypes[]) throws IOException;                           
```

All requests to send a fax can include the following **Options:** [`contact`, `postponeTime`, `retriesToPerform`, `csid`, `pageHeader`, `reference`, `pageSize`, `fitToPage`, `pageOrientation`, `resolution`, `rendering`](https://www.interfax.net/en/dev/rest/reference/2918)
set via [`SendFaxOptions`](src/main/java/net/interfax/rest/client/domain/SendFaxOptions.java) class                                                                                                                                                    

---

### Get Outbound Fax List
                              
Get a list of recent outbound faxes (which does not include batch faxes).

```java
InterFAX interFAX = new DefaultInterFAXClient();
OutboundFaxStructure[] outboundFaxStructures = interFAX.getFaxList();
```

Using additional **Options:** [`limit`, `lastId`, `sortOrder`, `userId`](https://www.interfax.net/en/dev/rest/reference/2920)

```java    
GetFaxListOptions getFaxListOptions = new GetFaxListOptions();
getFaxListOptions.setLimit(Optional.of(5));

InterFAX interFAX = new DefaultInterFAXClient();
OutboundFaxStructure[] outboundFaxStructures = interFAX.getFaxList(Optional.of(getFaxListOptions));
```
    
**More:** [documentation](https://www.interfax.net/en/dev/rest/reference/2920)

---

### Get Completed Fax List

Get details for a subset of completed faxes from a submitted list. (Submitted id's which have not completed are 
ignored).

```java
InterFAX interFAX = new DefaultInterFAXClient();
OutboundFaxStructure[] outboundFaxStructures = interFAX.getCompletedFaxList(new String[]{"667915751", "667915471"});
```
    
**More:** [documentation](https://www.interfax.net/en/dev/rest/reference/2972)
 
---
 
### Get Outbound Fax Record
    
Retrieves information regarding a previously-submitted fax, including its current status.

```java    
InterFAX interFAX = new DefaultInterFAXClient();
OutboundFaxStructure outboundFaxStructure = interFAX.getFaxRecord("667915751");
```

**More:** [documentation](https://www.interfax.net/en/dev/rest/reference/2921)    

---

### Get Outbound Fax Image
    
Retrieve the fax image (TIFF file) of a submitted fax.

```java    
InterFAX interFAX = new DefaultInterFAXClient();
byte[] faxImage = interFAX.getOuboundFaxImage("667915751");
```
    
**More:** [documentation](https://www.interfax.net/en/dev/rest/reference/2941)    

---

### Cancel a Fax
    
Cancel a fax in progress.
    
```java    
InterFAX interFAX = new DefaultInterFAXClient();
APIResponse apiResponse = interFAX.cancelFax("279499862");
```
        
**More:** [documentation](https://www.interfax.net/en/dev/rest/reference/2939)    

---

### Search Fax List

Search for outbound faxes.

```java
InterFAX interFAX = new DefaultInterFAXClient();
OutboundFaxStructure[] outboundFaxStructures = interFAX.searchFaxList();
```

Using additional **Options:** [`ids`, `reference`, `dateFrom`, `dateTo`, `status`, `userId`, `faxNumber`, `limit`, `offset`](https://www.interfax.net/en/dev/rest/reference/2959)

```java
SearchFaxOptions searchFaxOptions = new SearchFaxOptions();
searchFaxOptions.setLimit(Optional.of(3));
searchFaxOptions.setFaxNumber(Optional.of("+442084978672"));
InterFAX interFAX = new DefaultInterFAXClient();
OutboundFaxStructure[] outboundFaxStructures = interFAX.searchFaxList(Optional.of(searchFaxOptions));
```    

**More:** [documentation](https://www.interfax.net/en/dev/rest/reference/2959)    

---

### Hide Fax

Hide a fax from listing in queries (there is no way to unhide a fax).

```java
InterFAX interFAX = new DefaultInterFAXClient();
APIResponse apiResponse = interFAX.hideFax("667915469");
```

**More:** [documentation](https://www.interfax.net/en/dev/rest/reference/2940)

## Inbound

[Get list](#get-inbound-fax-list) | [Get record](#get-inbound-fax-record) | [Get image](#get-inbound-fax-image) | [Get emails](#get-forwarding-emails) | [Mark as read](#mark-as-readunread) | [Resend to email](#resend-inbound-fax)

### Get Inbound Fax List

Retrieves a user's list of inbound faxes. (Sort order is always in descending ID).

```java
InterFAX interFAX = new DefaultInterFAXClient();
InboundFaxStructure[] inboundFaxStructures = interFAX.getInboundFaxList();
```
    
Using additional **Options:** [`unreadOnly`, `limit`, `lastId`, `allUsers`](https://www.interfax.net/en/dev/rest/reference/2935)
    
```java
GetInboundFaxListOptions getInboundFaxListOptions = new GetInboundFaxListOptions();
getInboundFaxListOptions.setAllUsers(Optional.of(true));
getInboundFaxListOptions.setUnreadOnly(Optional.of(true));
getInboundFaxListOptions.setLimit(Optional.of(3));
InterFAX interFAX = new DefaultInterFAXClient();
InboundFaxStructure[] inboundFaxStructures = interFAX.getInboundFaxList(Optional.of(getInboundFaxListOptions));
```

**More:** [documentation](https://www.interfax.net/en/dev/rest/reference/2935)

---

### Get Inbound Fax Record

Retrieves a single fax's metadata (receive time, sender number, etc.).

```java
InterFAX interFAX = new DefaultInterFAXClient();
InboundFaxStructure inboundFaxStructure = interFAX.getInboundFaxRecord("292626603");
```

**More:** [documentation](https://www.interfax.net/en/dev/rest/reference/2938)

---

### Get Inbound Fax Image

Retrieves a single fax's image.

```java
InterFAX interFAX = new DefaultInterFAXClient();
byte[] faxImage = interFAX.getInboundFaxImage("292626603");
```

**More:** [documentation](https://www.interfax.net/en/dev/rest/reference/2937)   

---

### Get Forwarding Emails
 
Retrieve the list of email addresses to which a fax was forwarded.

```java 
InterFAX interFAX = new DefaultInterFAXClient();
InboundFaxesEmailsStructure inboundFaxesEmailsStructure = interFAX.getInboundFaxForwardingEmails("1234567");
```

**More:** [documentation](https://www.interfax.net/en/dev/rest/reference/2930)

---

### Mark As Read/Unread

Mark a transaction as read/unread.
```java     
InterFAX interFAX = new DefaultInterFAXClient();
APIResponse apiResponse = interFAX.markInboundFax("292626603", Optional.of(true));
```

**More:** [documentation](https://www.interfax.net/en/dev/rest/reference/2936)

---

### Resend Inbound Fax

Resend an inbound fax to a specific email address.

```java
InterFAX interFAX = new DefaultInterFAXClient();
APIResponse apiResponse = interFAX.resendInboundFax("292626603", Optional.of("someone@example.com"));
```
    
**More:** [documentation](https://www.interfax.net/en/dev/rest/reference/2929)

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

```java
String absoluteFilePath = this.getClass().getClassLoader().getResource("A17_FlightPlan.pdf").getFile();
File file = new File(absoluteFilePath);

InterFAX interFAX = new DefaultInterFAXClient();
APIResponse apiResponse = interFAX.uploadDocument(file);
```
    
With additional options,

```java
String absoluteFilePath = this.getClass().getClassLoader().getResource("A17_FlightPlan.pdf").getFile();
File file = new File(absoluteFilePath);

InterFAX interFAX = new DefaultInterFAXClient();
DocumentUploadSessionOptions documentUploadSessionOptions = new DocumentUploadSessionOptions();
documentUploadSessionOptions.setName(Optional.of("overriddenname.pdf"));
documentUploadSessionOptions.setSize(Optional.of(Integer.toUnsignedLong(12345)));
documentUploadSessionOptions.setDisposition(Optional.of(Disposition.multiUse));
documentUploadSessionOptions.setSharing(Optional.of(Sharing.privateDoc));
APIResponse apiResponse = interFAX.uploadDocument(file, Optional.of(documentUploadSessionOptions));
```

**More:** [documentation - 1](https://www.interfax.net/en/dev/rest/reference/2967), [2](https://www.interfax.net/en/dev/rest/reference/2966)

---

### Get Document List

Get a list of previous document uploads which are currently available.

```java
InterFAX interFAX = new DefaultInterFAXClient();
UploadedDocumentStatus[] uploadedDocumentStatuses = interFAX.getUploadedDocumentsList();
```
    
With additional **Options:** [`limit`, `offset`](https://www.interfax.net/en/dev/rest/reference/2968)

```java    
InterFAX interFAX = new DefaultInterFAXClient();
GetUploadedDocumentsListOptions getUploadedDocumentsListOptions = new GetUploadedDocumentsListOptions();
getUploadedDocumentsListOptions.setLimit(Optional.of(5));
getUploadedDocumentsListOptions.setOffset(Optional.of(1));
UploadedDocumentStatus[] uploadedDocumentStatuses = interFAX.getUploadedDocumentsList(Optional.of(getUploadedDocumentsListOptions));    
```

**More:** [documentation](https://www.interfax.net/en/dev/rest/reference/2968)

---

### Get Upload Status

Get the current status of a specific document upload.

```java
InterFAX interFAX = new DefaultInterFAXClient();
UploadedDocumentStatus uploadedDocumentStatus = interFAX.getUploadedDocumentStatus("deca890355b44b42944970d9773962b5");
```

**More:** [documentation](https://www.interfax.net/en/dev/rest/reference/2965)    

---

### Cancel Document

Cancel a document upload and tear down the upload session, or delete a previous upload.

```java
InterFAX interFAX = new DefaultInterFAXClient();
APIResponse apiResponse = interFAX.cancelDocumentUploadSession("deca890355b44b42944970d9773962b5");
```

**More:** [documentation](https://www.interfax.net/en/dev/rest/reference/2964)       

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

```shell
$ mvn clean test
```
     
### Releasing
    
#### Versioning

The project uses [semver](http://semver.org/) for versioning. 

##### Minor Releases

If a change is backwards compatible, it can be committed and pushed straight to master. Versioning is handled 
automatically by incrementing the **minor version** by 1 and released automatically by travisCI, using the 
[release script](scripts/release.sh)

##### Major Releases 

For breaking changes / major releases, the version number needs to be manually updated in the [project pom](pom.xml). 
Simply **increment the major version by 1** and **drop the minor version to 0**. Example, if the version in the project
pom is as follows:

```xml
<version>0.34-SNAPSHOT</version>
```

A major change should update it to:

```xml
<version>1.0-SNAPSHOT</version>
```

Once updated, committed and pushed to master, travisCI handles releasing the version to maven central. 
    
## License

This library is released under the [MIT License](LICENCE).    
        
    
         
         
