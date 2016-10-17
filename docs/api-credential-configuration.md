# Alternate ways of passing the API credentials include:

## Pass credentials via a `yaml` file        
        
Create a file called `interfax-api-credentials.yaml` with the following
contents, replacing the value of `username` and `password` fields with
those of your API credentials.

    username: "api-username"
    password: "api-password"
    
Ensure the file is present in your applications classpath. Next, 
initialise a new `InterFAXClient` as shown below.

    InterFAXClient interFAXClient = new InterFAXJerseyClient();
    
## Pass credentials via environment variables

Copy the file [`interfax-api-credentials.yaml`](../src/test/resources/interfax-api-credentials.yaml)
into your applications' classpath. The file is pre-configured to use
values of the following environment variables as the API credentials.

    INTERFAX_USERNAME
    INTERFAX_PASSWORD

Make the api credentials available via the environment variables
mentioned above and initialise a new `InterFAXClient` as shown below. 
 
    InterFAXClient interFAXClient = new InterFAXJerseyClient();
    
    