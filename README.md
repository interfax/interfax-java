# InterFAX Java Library

## Introduction

Send and receive faxes in Java with the [InterFAX REST API](https://www.interfax.net/en/dev/rest/reference).

## User Guide

### Getting started

#### Pre-requisite(s)

The pre-requisite to using the InterFAX api is to have API credentials,
which can be done by registering for a [developer account](https://secure.interfax.net/Default.aspx?Lang=en&Target=RegistrationService&Method=DisplayForm&BPCode=).

#### Initialise the client

Once you have the API credentials in the form of a username and 
password, the simplest way to get started is to initialise a new 
`InterFAXClient`, using the [Jersey](https://jersey.java.net/) based 
HTTP client implementation, passing the API username and password as 
constructor arguments as shown below.

    InterFAXClient interFAXClient = new InterFAXJerseyClient("api-username", "api-password");
        
For alternate ways of passing API credentials, including via a `yaml` 
file and environment variables, refer to: [api-credential-configuration.md](docs/api-credential-configuration.md)

### Sending Faxes

#### Send Fax

To send a fax, simply invoke the [`sendFax`](src/main/java/net/interfax/rest/client/InterFAXClient.java)
method of the `InterFAXClient`.

Example:

    java.io.File file = new File(absoluteFilePath);
    InterFAXClient interFAXClient = new InterFAXJerseyClient();
    APIResponse apiResponse = interFAXClient.sendFax(faxNumber, file);

Additional info: [REST API documentation](https://www.interfax.net/en/dev/rest/reference/2918)

## Contributor Guide

TODO