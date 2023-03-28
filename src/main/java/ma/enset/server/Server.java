package ma.enset.server;

import io.grpc.ServerBuilder;

import java.io.IOException;

public class Server {
    public static void main(String[] args) throws IOException, InterruptedException {
        io.grpc.Server server= ServerBuilder.forPort(2222)
                .addService(new chatServiceServer())
                .build();
        server.start();
        server.awaitTermination();
    }
}
