package utils;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import models.entities.Employee;
import models.enums.Privilege;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.text.ParseException;
import java.util.Date;


@Slf4j
public class JWTUtil {

    private static final ECKey ecKey;

    static {
        ECKey tempKey = null;
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("EC");
            ECGenParameterSpec specs = new ECGenParameterSpec("secp256r1");
            generator.initialize(specs);
            KeyPair keyPair = generator.generateKeyPair();
            tempKey = new ECKey.Builder(Curve.P_256, (ECPublicKey) keyPair.getPublic())
                    .privateKey((ECPrivateKey) keyPair.getPrivate())
                    .build();
        } catch (Exception e) {
            log.error("Error initializing ECKey", e);
        }
        ecKey = tempKey;
        try {
            System.out.println("Public key: " + ecKey.toPublicJWK().toJSONString());
            System.out.println("Private key: " + ecKey.toJSONString());
            System.out.println(ecKey.toPrivateKey().toString());
        } catch (JOSEException e) {
            log.error("Error initializing ECKey", e);
        }
    }


    public String generateSignedJWT(Employee employee) {
        try {
            JWTClaimsSet.Builder claimSetBuilder = new JWTClaimsSet.Builder()
                    .subject(employee.getUsername())
                    .audience("restful-service")
                    .issuer(getApplicationHost())
                    .jwtID(employee.getId().toString())
                    .issueTime(new Date())
                    .notBeforeTime(new Date())
                    .expirationTime(calculateExpirationTime())
                    .claim("email", employee.getEmail())
                    .claim("phone", employee.getPhone())
                    .claim("privileges", employee.getPrivilege().name());

            JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.ES256).type(JOSEObjectType.JWT).jwk(ecKey.toPublicJWK()).build();
            SignedJWT signedJWT = new SignedJWT(header, claimSetBuilder.build());
            signedJWT.sign(new ECDSASigner((ECPrivateKey) ecKey.toPrivateKey()));
            return signedJWT.serialize();

        } catch (JOSEException | UnknownHostException e) {
            log.error("Error generating JWT", e);
            throw new RuntimeException("Error generating JWT", e);
        }
    }

    public JWTClaimsSet verifyJWT(String token, Privilege privilege) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new ECDSAVerifier(ecKey.toECPublicKey());
            if (!signedJWT.verify(verifier)) {
                log.error("JWT signature verification failed");
                return null;
            }

            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
            if (!claimsSet.getAudience().contains("restful-service")) {
                log.error("JWT audience claim check failed");
                return null;
            }
            if (!claimsSet.getIssuer().equals(getApplicationHost())) {
                log.error("JWT issuer claim check failed");
                return null;
            }

            if (privilege.getLevel() > Privilege.valueOf(claimsSet.getClaim("privileges").toString()).getLevel()) {
                log.info("Token privilege: {}", Privilege.valueOf(claimsSet.getClaim("privileges").toString()));
                log.error("JWT privilege claim check failed");
                return null;
            }

            return claimsSet;

        } catch (ParseException | JOSEException e) {
            log.error("Error verifying JWT", e);
            throw new RuntimeException("Error verifying JWT", e);
        } catch (UnknownHostException e) {
            log.error("Error getting application host", e);
            throw new RuntimeException("Error getting application host", e);
        }
    }

    private String getApplicationHost() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostName();
    }

    private Date calculateExpirationTime() {
        return new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000);
    }
}