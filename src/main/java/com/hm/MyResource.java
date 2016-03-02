package com.hm;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("myresource")
public class MyResource {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        return "Hello, This is my sample application on Heroku to push fies from Salesforce to directories!";
    	// return System.getenv("SFTPHOST")+System.getenv("SFTPPORT")+System.getenv("SFTPUSER")+System.getenv("SFTPPASS")+System.getenv("SFTPWORKINGDIR");
    }
}
