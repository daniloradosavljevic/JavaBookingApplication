package booking.client;

import booking.grpc.BookingServiceGrpc;
import booking.grpc.SearchRequest;
import booking.grpc.SearchResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class BookingClient {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        BookingServiceGrpc.BookingServiceBlockingStub stub = BookingServiceGrpc.newBlockingStub(channel);
        Scanner scanner = new Scanner(System.in);
        List<booking.grpc.Hotel> lastHotels = null;
        Map<String, HotelPriceHistory> hotelPriceHistory = new HashMap<>();

        NotificationClient notificationClient = null;
        Thread notificationThread = null;

        while (true) {
            printMenuPrompt();
            String choiceStr = scanner.nextLine().trim();
            int choice = choiceStr.isEmpty() ? -1 : Integer.parseInt(choiceStr);

            if (choice == 0) {
                if (notificationClient != null) notificationClient.stop();
                break;
            } else if (choice == 1) {
                System.out.print("Unesite grad (Enter za bilo koji): ");
                String city = scanner.nextLine().trim();
                System.out.print("Unesite maksimalnu udaljenost od centra (Enter za bilo koji): ");
                String maxDistStr = scanner.nextLine().trim();
                double maxDistance = maxDistStr.isEmpty() ? 9999.0 : Double.parseDouble(maxDistStr);
                System.out.print("Unesite minimalnu kategoriju (3-5, Enter za bilo koju): ");
                String minCatStr = scanner.nextLine().trim();
                int minCategory = minCatStr.isEmpty() ? 0 : Integer.parseInt(minCatStr);

                SearchRequest request = SearchRequest.newBuilder()
                        .setCity(city)
                        .setMaxDistance(maxDistance)
                        .setMinCategory(minCategory)
                        .build();

                SearchResponse response = stub.searchHotels(request);
                lastHotels = response.getHotelsList();
                if (lastHotels != null) {
                    for (booking.grpc.Hotel hotel : lastHotels) {
                        String hotelKey = hotel.getName();
                        hotelPriceHistory
                            .computeIfAbsent(hotelKey, k -> new HotelPriceHistory())
                            .update(hotel.getCurrentPrice());
                    }
                }


                System.out.println("\nBroj pronadjenih hotela: " + response.getHotelsCount());
                response.getHotelsList().forEach(hotel -> {
                    System.out.println("---------------------------------");
                    System.out.println("Naziv: " + hotel.getName());
                    System.out.println("Kategorija: " + hotel.getCategory());
                    System.out.println("Grad: " + hotel.getCity());
                    System.out.println("Udaljenost od centra: " + hotel.getDistanceFromCenter());
                    System.out.println("Slobodnih soba: " + hotel.getAvailableRooms());
                    System.out.println("Cena: " + hotel.getCurrentPrice());
                });
                System.out.println();

                if (notificationClient != null) notificationClient.stop();
                notificationClient = new NotificationClient(city, maxDistance, minCategory, hotelPriceHistory);
                notificationThread = new Thread(notificationClient);
                notificationThread.setDaemon(true);
                notificationThread.start();
            } else if (choice == 2) {
                System.out.print("Unesite naziv hotela: ");
                String hotelName = scanner.nextLine().trim();
                System.out.print("Unesite svoj client ID: ");
                String clientId = scanner.nextLine().trim();
                System.out.print("Unesite datum pocetka (yyyy-MM-dd): ");
                String startDate = scanner.nextLine().trim();
                System.out.print("Unesite broj dana: ");
                int days = Integer.parseInt(scanner.nextLine().trim());

                booking.grpc.ReservationRequest req = booking.grpc.ReservationRequest.newBuilder()
                        .setHotelName(hotelName)
                        .setClientId(clientId)
                        .setStartDate(startDate)
                        .setDays(days)
                        .build();
                booking.grpc.ReservationResponse resp = stub.makeReservation(req);
                System.out.println(resp.getMessage());
                if (resp.getAccepted()) {
                    System.out.println("Cena za boravak: " + resp.getPrice());
                    System.out.println("Sacuvajte reservation ID za placanje.");
                }
            } else if (choice == 3) {
                System.out.print("Unesite reservation ID: ");
                String reservationId = scanner.nextLine().trim();
                System.out.print("Unesite svoj client ID: ");
                String clientId = scanner.nextLine().trim();

                booking.grpc.PaymentRequest req = booking.grpc.PaymentRequest.newBuilder()
                        .setReservationId(reservationId)
                        .setClientId(clientId)
                        .build();
                booking.grpc.PaymentResponse resp = stub.payForReservation(req);
                System.out.println(resp.getMessage());
            }else if (choice == 4) {
                System.out.print("Unesi reservation ID za otkazivanje: ");
                String reservationId = scanner.nextLine().trim();
                System.out.print("Unesi svoj client ID: ");
                String clientId = scanner.nextLine().trim();

                booking.grpc.CancelRequest req = booking.grpc.CancelRequest.newBuilder()
                        .setReservationId(reservationId)
                        .setClientId(clientId)
                        .build();
                booking.grpc.CancelResponse resp = stub.cancelReservation(req);
                System.out.println(resp.getMessage());
            }else if (choice == 5) {
                System.out.println("=== Istorija cena hotela (za period pracenja) ===");
                for (Map.Entry<String, HotelPriceHistory> entry : hotelPriceHistory.entrySet()) {
                    HotelPriceHistory hist = entry.getValue();
                    if (hist.isInitialized()) {
                        System.out.println("Hotel: " + entry.getKey());
                        System.out.println("  Min cena: " + hist.getMinPrice());
                        System.out.println("  Max cena: " + hist.getMaxPrice());
                        System.out.println("  Poslednja cena: " + hist.getLastPrice());
                        System.out.println();
                    }
                }
            }else {
                System.out.println("Nepoznata opcija, pokusajte ponovo.\n");
            }
        }

        channel.shutdown();
        System.out.println("Kraj programa.");
    }

    public static void printMenuPrompt() {
        System.out.println("=== Booking Client Menu ===");
        System.out.println("1. Search hotels");
        System.out.println("2. Rezervisi hotel");
        System.out.println("3. Plati rezervaciju");
        System.out.println("4. Otkazi rezervaciju");
        System.out.println("5. Prikazi istoriju cena");
        System.out.println("0. Exit");
        System.out.print("Choose option: ");
    }
}