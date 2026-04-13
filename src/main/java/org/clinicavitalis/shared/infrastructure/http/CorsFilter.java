package org.clinicavitalis.shared.infrastructure.http;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class CorsFilter implements ContainerResponseFilter {

    private static final String ALLOWED_ORIGINS = "http://localhost:5173,http://localhost:3001,http://localhost:8080";
    private static final String ALLOWED_METHODS = "GET,POST,PUT,DELETE,OPTIONS,PATCH";
    private static final String ALLOWED_HEADERS = "Content-Type,Authorization";

    @Override
    public void filter(ContainerRequestContext request, ContainerResponseContext response) throws IOException {
        String origin = request.getHeaderString("Origin");

        if (origin != null && isOriginAllowed(origin)) {
            response.getHeaders().add("Access-Control-Allow-Origin", origin);
        }

        MultivaluedMap<String, Object> headers = response.getHeaders();
        headers.add("Access-Control-Allow-Methods", ALLOWED_METHODS);
        headers.add("Access-Control-Allow-Headers", ALLOWED_HEADERS);
        headers.add("Access-Control-Expose-Headers", "Authorization");
        headers.add("Access-Control-Max-Age", "86400");
        headers.add("Access-Control-Allow-Credentials", "true");
    }

    private boolean isOriginAllowed(String origin) {
        return ALLOWED_ORIGINS.contains(origin);
    }
}
