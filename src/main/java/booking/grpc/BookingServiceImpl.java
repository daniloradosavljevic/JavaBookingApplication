package booking.grpc;

import booking.core.Hotel;
import booking.socket.NotificationServer;
import io.grpc.stub.StreamObserver;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class BookingServiceImpl extends BookingServiceGrpc.BookingServiceImplBase {
    private final NotificationServer notificationServer;
    private final String csvPath = "booking/grpc/hoteli.csv"; 

    public BookingServiceImpl(NotificationServer notificationServer) {
        this.notificationServer = notificationServer;
    }

    private List<Hotel> loadHotelsFromCSV() {
        List<Hotel> hotels = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("booking-data/hoteli.csv"))) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) { firstLine = false; continue; }
                String[] parts = line.split(",");
                if (parts.length < 8) continue;
                String id = parts[0];
                String name = parts[1];
                int category = Integer.parseInt(parts[2]);
                String city = parts[3];
                double distance = Double.parseDouble(parts[4]);
                int capacity = Integer.parseInt(parts[5]);
                int freeRooms = Integer.parseInt(parts[6]);
                double price = Double.parseDouble(parts[7]);
                Hotel hotel = new Hotel(id, name, category, city, distance, capacity, price);
                hotel.setFreeRooms(freeRooms);
                hotels.add(hotel);
            }
        } catch (Exception e) {
            System.err.println("Greska pri ucitavanju hotela iz CSV: " + e.getMessage());
        }
        return hotels;
    }

    private void saveHotelsToCSV(List<Hotel> hotels) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("booking-data/hoteli.csv", false))) {
            writer.println("id,name,category,city,distanceFromCenter,capacity,freeRooms,currentPrice");
            for (Hotel h : hotels) {
                writer.println(String.join(",",
                    h.getId(),
                    h.getName(),
                    String.valueOf(h.getCategory()),
                    h.getCity(),
                    String.valueOf(h.getDistanceFromCenter()),
                    String.valueOf(h.getCapacity()),
                    String.valueOf(h.getFreeRooms()),
                    String.valueOf(h.getCurrentPrice())
                ));
            }
        } catch (Exception e) {
            System.err.println("Greska pri upisu hotela u CSV: " + e.getMessage());
        }
    }

    @Override
    public void searchHotels(SearchRequest request, StreamObserver<SearchResponse> responseObserver) {
        List<Hotel> hotels = loadHotelsFromCSV(); 
        NotificationServer.SearchParams params = new NotificationServer.SearchParams(
                request.getCity(), request.getMaxDistance(), request.getMinCategory()
        );

        List<Hotel> filteredHotels = hotels.stream()
                .filter(h -> request.getCity().isEmpty() || h.getCity().equalsIgnoreCase(request.getCity()))
                .filter(h -> h.getDistanceFromCenter() <= request.getMaxDistance())
                .filter(h -> h.getCategory() >= request.getMinCategory())
                .filter(h -> h.getFreeRooms() > 0)
                .collect(Collectors.toList());

        List<booking.grpc.Hotel> protoHotels = filteredHotels.stream()
                .map(h -> booking.grpc.Hotel.newBuilder()
                        .setName(h.getName())
                        .setCategory(h.getCategory())
                        .setCity(h.getCity())
                        .setDistanceFromCenter(h.getDistanceFromCenter())
                        .setAvailableRooms(h.getFreeRooms())
                        .setCurrentPrice(h.getCurrentPrice())
                        .build())
                .collect(Collectors.toList());

        SearchResponse response = SearchResponse.newBuilder()
                .addAllHotels(protoHotels)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    public void simulatePriceChange(String hotelName, double newPrice) {
        List<Hotel> hotels = loadHotelsFromCSV();
        Hotel changedHotel = null;
        for (Hotel h : hotels) {
            if (h.getName().equals(hotelName)) {
                h.setCurrentPrice(newPrice);
                changedHotel = h;
                break;
            }
        }
        if (changedHotel != null) {
            saveHotelsToCSV(hotels); 
            NotificationServer.SearchParams params = new NotificationServer.SearchParams(
                    changedHotel.getCity(), changedHotel.getDistanceFromCenter(), changedHotel.getCategory());
            notifyChange(changedHotel, "Promena cene", params);
        }
    }

    public void notifyChange(Hotel hotel, String type, NotificationServer.SearchParams params) {
        String msg = "Promena [" + type + "]: " + hotel.getName() +
                " | Grad: " + hotel.getCity() +
                " | Kategorija: " + hotel.getCategory() +
                " | Udaljenost: " + hotel.getDistanceFromCenter() +
                " | Slobodnih soba: " + hotel.getFreeRooms() +
                " | Cena: " + hotel.getCurrentPrice();
        notificationServer.broadcast(msg, params);
    }
}