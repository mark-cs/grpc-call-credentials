package uk.co.markcs.grpc;

import io.grpc.CallCredentials;
import io.grpc.Metadata;
import io.grpc.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;

public class AuthenticationCallCredentials extends CallCredentials {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationCallCredentials.class);
    private final String token;

    public AuthenticationCallCredentials(final String token) {
        this.token = token;
    }

    @Override
    public void applyRequestMetadata(
            final RequestInfo requestInfo,
            final Executor executor,
            final MetadataApplier metadataApplier) {
        executor.execute(() -> {
            try {
                logger.info("Applying token to RPC call");
                final Metadata headers = new Metadata();
                headers.put(AuthenticationConstants.META_DATA_KEY, "Bearer " + token);
                metadataApplier.apply(headers);
            } catch(final Throwable e) {
                logger.error("Unable to apply token to RPC call", e);
                metadataApplier.fail(Status.UNAUTHENTICATED.withCause(e));
            }
        });

    }

    @Override
    public void thisUsesUnstableApi() {
        // yes this is unstable :(
    }
}
