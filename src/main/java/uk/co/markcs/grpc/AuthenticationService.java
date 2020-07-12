package uk.co.markcs.grpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;

/**
 * Generates and validates tokens from/to usernames.
 *
 * This is not a proper token generator, it merely base64 encodes/decodes.
 * It is only here as an example.
 */
public class AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
    private final String secret;

    public AuthenticationService(final String secret) {
        this.secret = secret;
    }

    public String generateToken(final String username) {
        logger.info("Encoding username as token");
        return Base64.getEncoder().encodeToString((secret + username).getBytes());
    }

    public String validateToken(final String token) throws AuthenticationException {
        logger.info("Decoding token to username");
        final String decodedToken = new String(Base64.getDecoder().decode(token));
        if (!decodedToken.startsWith(secret)) {
            throw new AuthenticationException("Invalid token, not generated using Authentication Service");
        } else {
            return decodedToken.substring(secret.length());
        }
    }


    public static class AuthenticationException extends Exception {

        public AuthenticationException(final String message) {
            super(message);
        }

    }
}
