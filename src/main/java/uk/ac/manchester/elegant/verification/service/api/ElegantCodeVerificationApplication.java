package uk.ac.manchester.elegant.verification.service.api;

import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

@ApplicationPath("/api")
public class ElegantCodeVerificationApplication extends ResourceConfig {

    public ElegantCodeVerificationApplication() {
        super(ElegantCodeVerificationService.class, MultiPartFeature.class);
    }
}