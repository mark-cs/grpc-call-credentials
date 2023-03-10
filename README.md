# Example GRPC Call Credentials Usage

This repository is the supporting code for the [GRPC Call Credentials in Java Guide](https://mark-cs.co.uk/posts/2020/july/grpc-call-credentials-in-java/) from my [blog](https://mark-cs.co.uk).

This assumes you have a working Java GRPC environment and that you know the basics when it comes to GRPC.

To run the example:

```bash
mvn clean verify
mvn exec:java -Dexec.mainClass=uk.co.markcs.grpc.Demo
```

This will run the server.
The RPC is then invoked in a variety of ways to show different authentication scenarios.
