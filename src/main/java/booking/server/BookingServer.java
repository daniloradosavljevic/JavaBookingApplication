package booking.server;
import booking.grpc.BookingServiceImpl;
import booking.socket.NotificationServer;
import io.grpc.Server;
import io.grpc.ServerBuilder;

public class BookingServer {
    public static NotificationServer notificationServer;
    public static BookingServiceImpl bookingService;

    public static void main(String[] args) throws Exception {
        notificationServer = new NotificationServer();
        notificationServer.start();

        bookingService = new BookingServiceImpl(notificationServer);
        String csvPath = "booking-data/hoteli.csv"; 
        booking.grpc.HotelCsvWatcher watcher = new booking.grpc.HotelCsvWatcher(csvPath, notificationServer);
        watcher.setDaemon(true);
        watcher.start();
        Server server = ServerBuilder.forPort(50051)
                .addService(bookingService)
                .build()
                .start();
        System.out.println("Booking gRPC server started on port 50051");
        server.awaitTermination();
    }
}