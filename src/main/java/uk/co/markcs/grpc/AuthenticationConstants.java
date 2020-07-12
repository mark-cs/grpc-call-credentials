package uk.co.markcs.grpc;

import io.grpc.Context;
import io.grpc.Metadata;

public class AuthenticationConstants {

    public static final Metadata.Key<String> META_DATA_KEY = Metadata.Key.of("Authentication", Metadata.ASCII_STRING_MARSHALLER);

    public static final Context.Key<String> CONTEXT_USERNAME_KEY = Context.key("username");
}
