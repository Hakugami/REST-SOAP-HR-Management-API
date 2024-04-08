package utils;

import com.nimbusds.jwt.JWTClaimsSet;
import jakarta.ws.rs.container.ContainerRequestContext;
import models.enums.Privilege;

import java.util.Set;

public class ApiUtil {

    private static final JWTUtil jwtUtil = new JWTUtil();

    private ApiUtil() {
        throw new IllegalStateException("Utility class cannot be instantiated");
    }

    public static Set<String> getFields(String fields) {
        return fields == null ? Set.of() : Set.of(fields.split(","));
    }

    public static JWTClaimsSet getClaimsSet(ContainerRequestContext requestContext, Privilege privilege) {
        String authorizationHeader = requestContext.getHeaderString("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return null;
        }

        String token = authorizationHeader.substring("Bearer ".length()).trim();
        return jwtUtil.verifyJWT(token, privilege);
    }
}