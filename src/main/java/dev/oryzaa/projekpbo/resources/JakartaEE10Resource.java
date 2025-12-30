package dev.oryzaa.projekpbo.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Health check REST resource.
 */
@Path("health")
public class JakartaEE10Resource {
    
    /**
     * Health check endpoint.
     * @return Status response
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response healthCheck() {
        return Response
                .ok("{\"status\":\"UP\",\"application\":\"Puskesmas Inventory System\"}")
                .build();
    }
}
