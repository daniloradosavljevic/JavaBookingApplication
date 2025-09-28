package booking.server;

import booking.grpc.BookingServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;

public class BookingServer {
    public static void main(String[] args) throws Exception {
        Server server = ServerBuilder.forPort(50051)
                .addService(new BookingServiceImpl())
                .build()
                .start();
        System.out.println("Booking gRPC server started on port 50051");
        server.awaitTermination();
    }
}