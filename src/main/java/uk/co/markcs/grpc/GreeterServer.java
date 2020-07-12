package uk.co.markcs.grpc;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GreeterServer extends GreeterGrpc.GreeterImplBase {

    private static final Logger logger = LoggerFactory.getLogger(GreeterServer.class);

    @Override
    public void greet(
            final GreetRequest request,
            final StreamObserver<GreetResponse> responseObserver) {
        final String username = AuthenticationConstants.CONTEXT_USERNAME_KEY.get();
        logger.info("Received greet request");
        responseObserver.onNext(GreetResponse
                .newBuilder()
                .setMessage("Hello, " + request.getName() + ", authenticated as " + username)
                .build());
        responseObserver.onCompleted();
    }
}
