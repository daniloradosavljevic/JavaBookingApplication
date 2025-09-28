package booking.client;

import booking.grpc.BookingServiceGrpc;
import booking.grpc.SearchRequest;
import booking.grpc.SearchResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Scanner;

public class BookingClient {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        BookingServiceGrpc.BookingServiceBlockingStub stub = BookingServiceGrpc.newBlockingStub(channel);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("=== Booking Client Menu ===");
            System.out.println("1. Search hotels");
            System.out.println("0. Exit");
            System.out.print("Choose option: ");
            String choiceStr = scanner.nextLine().trim();
            int choice = choiceStr.isEmpty() ? -1 : Integer.parseInt(choiceStr);

            if (choice == 0) {
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
            } else {
                System.out.println("Nepoznata opcija, pokusajte ponovo.\n");
            }
        }

        channel.shutdown();
        System.out.println("Kraj programa.");
    }
}