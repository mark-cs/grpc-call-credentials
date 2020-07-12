package uk.co.markcs.grpc;

import com.google.common.base.Strings;
import io.grpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthenticationInterceptor implements ServerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationInterceptor.class);
    private final AuthenticationService authenticationService;

    public AuthenticationInterceptor(final AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            final ServerCall<ReqT, RespT> serverCall,
            final Metadata metadata,
            final ServerCallHandler<ReqT, RespT> serverCallHandler) {
        logger.info("Intercepting and validating token");
        final String header = metadata.get(AuthenticationConstants.META_DATA_KEY);

        if (Strings.isNullOrEmpty(header)) {
            logger.info("No Authentication header present, user unauthenticated");
            serverCall.close(Status.UNAUTHENTICATED.withDescription("No authentication header"), metadata);
        } else if (!header.startsWith("Bearer ")) {
            logger.info("Non Bearer Authentication header, user unauthenticated");
            serverCall.close(Status.UNAUTHENTICATED.withDescription("Non bearer token provided"), metadata);
        } else {
            final String token = header.substring("Bearer ".length());
            try {
                final String username = authenticationService.validateToken(token);
                logger.info("Authentication Service accepted token, user authenticated as " + username);
                final Context context = Context.current().withValue(AuthenticationConstants.CONTEXT_USERNAME_KEY, username);
                return Contexts.interceptCall(context, serverCall, metadata, serverCallHandler);
            } catch (final AuthenticationService.AuthenticationException e) {
                logger.info("TAS rejected token, user unauthenticated");
                serverCall.close(Status.UNAUTHENTICATED.withDescription("Rejected by Authentication Service"), metadata);
            }
        }
        return new ServerCall.Listener<ReqT>() {};
    }

}
