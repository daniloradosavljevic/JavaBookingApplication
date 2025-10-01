package booking.server;

import booking.grpc.BookingServiceGrpc;
import booking.grpc.SearchRequest;
import booking.grpc.SearchResponse;
import booking.grpc.ReservationRequest;
import booking.grpc.ReservationResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Random;

public class BotClientRunner {

    public static void startBotClients(int brojBotova) {
        for (int i = 0; i < brojBotova; i++) {
            int botId = i + 1;
            new Thread(() -> runBot(botId), "Bot-" + botId).start();
        }
    }

    private static void runBot(int botId) {
        ManagedChannel channel = null;
        try {
        	InetAddress ipv4Localhost = InetAddress.getByAddress(new byte[] {127,0,0,1});
        	channel = NettyChannelBuilder.forAddress(new InetSocketAddress(ipv4Localhost, 50051))
        	    .usePlaintext()
        	    .build();

            BookingServiceGrpc.BookingServiceBlockingStub stub = BookingServiceGrpc.newBlockingStub(channel);
            Random rand = new Random();
            while (true) {
                String[] gradovi = {"Beograd", "Novi Sad"};
                String grad = gradovi[rand.nextInt(gradovi.length)];
                int minCategory = 3 + rand.nextInt(3); 
                double maxDistance = 10 + rand.nextInt(10);

                SearchRequest request = SearchRequest.newBuilder()
                        .setCity(grad)
                        .setMaxDistance(maxDistance)
                        .setMinCategory(minCategory)
                        .build();

                SearchResponse response = stub.searchHotels(request);
                if (!response.getHotelsList().isEmpty()) {
                    booking.grpc.Hotel hotel = response.getHotelsList().get(rand.nextInt(response.getHotelsCount()));
                    String clientId = "BOT" + botId;
                    String startDate = "2025-12-" + (10 + rand.nextInt(10));
                    int days = 1 + rand.nextInt(5);

                    ReservationRequest req = ReservationRequest.newBuilder()
                            .setHotelName(hotel.getName())
                            .setClientId(clientId)
                            .setStartDate(startDate)
                            .setDays(days)
                            .build();
                    ReservationResponse resp = stub.makeReservation(req);
                    System.out.println("[BOT" + botId + "] Rezervacija za " + clientId + " u hotelu " +
                            hotel.getName() + ": " + resp.getMessage());
                } else {
                    System.out.println("[BOT" + botId + "] Nema hotela za grad " + grad + ", kat " + minCategory + ", dist " + maxDistance);
                }
                Thread.sleep(5000 + rand.nextInt(5000));
            }
        } catch (Exception e) {
            System.out.println("[BOT" + botId + "] Izuzetak: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (channel != null) channel.shutdownNow();
        }
    }
}