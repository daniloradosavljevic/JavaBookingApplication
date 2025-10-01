package booking.server;
import booking.grpc.BookingServiceImpl;
import booking.socket.NotificationServer;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.util.Scanner;

import booking.grpc.AdminServiceImpl;
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
        Server adminGrpc = ServerBuilder.forPort(7000)
                .addService(new AdminServiceImpl())
                .build()
                .start();
        System.out.println("Admin gRPC server started on port 7000");
        
        Scanner scanner = new Scanner(System.in);
        System.out.print("Da li zelis da pokrenes bot klijente? (da/ne): ");
        String odgovor = scanner.nextLine().trim().toLowerCase();
        if (odgovor.equals("da")) {
            System.out.print("Unesi broj botova: ");
            int brojBotova = Integer.parseInt(scanner.nextLine().trim());
            BotClientRunner.startBotClients(brojBotova);
        }
        
        server.awaitTermination();
        adminGrpc.awaitTermination();
    }
    
}