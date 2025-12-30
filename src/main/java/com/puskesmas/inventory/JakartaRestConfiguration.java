package com.puskesmas.inventory;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * Jakarta RESTful Web Services configuration.
 * Base path for all REST resources.
 */
@ApplicationPath("api")
public class JakartaRestConfiguration extends Application {
    // No additional configuration needed
}
