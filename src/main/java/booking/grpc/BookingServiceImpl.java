package booking.grpc;

import booking.core.Hotel;
import booking.core.Reservation;
import booking.socket.NotificationServer;
import io.grpc.stub.StreamObserver;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.*;

public class BookingServiceImpl extends BookingServiceGrpc.BookingServiceImplBase {
    private final NotificationServer notificationServer;
    private final String csvPath = "booking/grpc/hoteli.csv";
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);
    private final String reservationsCsv = "booking-data/rezervacije.csv";
    private final String finansijeCsv = "booking-data/finansije.csv";
    private final double commissionPercent = 0.10;
    private final int paymentTimeoutMinutes = 5;

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

    private List<Reservation> loadReservationsFromCSV() {
        List<Reservation> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(reservationsCsv))) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) { first = false; continue; }
                String[] p = line.split(",");
                Reservation.Status s = Reservation.Status.valueOf(p[5]);
                list.add(new Reservation(
                        p[0], p[1], p[2], LocalDate.parse(p[3]).atStartOfDay(), Integer.parseInt(p[4]),
                        s, LocalDateTime.parse(p[6]), Double.parseDouble(p[7])
                ));
            }
        } catch (Exception ignored) {}
        return list;
    }

    private void saveReservationsToCSV(List<Reservation> list) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(reservationsCsv, false))) {
            pw.println("reservationId,hotelName,clientId,startDate,days,status,paymentDeadline,price");
            for (Reservation r : list) {
                pw.println(String.join(",",
                        r.getId(), r.getHotelId(), r.getClientId(),
                        r.getFrom().toLocalDate().toString(),
                        String.valueOf(r.getNights()), r.getStatus().name(),
                        r.getPaymentDeadline().toString(), String.valueOf(r.getReservedPrice())
                ));
            }
        } catch (Exception ignored) {}
    }

    private Map<String, double[]> loadFinansije() {
        Map<String, double[]> map = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(finansijeCsv))) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) { first = false; continue; }
                String[] p = line.split(",");
                map.put(p[0], new double[]{Double.parseDouble(p[1]), Double.parseDouble(p[2])});
            }
        } catch (Exception ignored) {}
        return map;
    }

    private void saveFinansije(Map<String, double[]> map) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(finansijeCsv, false))) {
            pw.println("hotelId,ukupnoHotel,ukupnoProvizija");
            for (Map.Entry<String, double[]> e : map.entrySet()) {
                pw.println(e.getKey() + "," + e.getValue()[0] + "," + e.getValue()[1]);
            }
        } catch (Exception ignored) {}
    }

    private void azurirajFinansije(String hotelId, double hotelIncome, double provizija) {
        Map<String, double[]> map = loadFinansije();
        double[] vals = map.getOrDefault(hotelId, new double[]{0.0, 0.0});
        vals[0] += hotelIncome;
        vals[1] += provizija;
        map.put(hotelId, vals);
        saveFinansije(map);
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

    @Override
    public void makeReservation(booking.grpc.ReservationRequest req, StreamObserver<booking.grpc.ReservationResponse> respObserver) {
        executor.submit(() -> {
            List<Hotel> hotels = loadHotelsFromCSV();
            Hotel hotel = hotels.stream().filter(h -> h.getName().equals(req.getHotelName())).findFirst().orElse(null);

            if (hotel == null || hotel.getFreeRooms() <= 0) {
                respObserver.onNext(booking.grpc.ReservationResponse.newBuilder()
                        .setAccepted(false)
                        .setMessage("Nema slobodnih soba ili hotel ne postoji.")
                        .setPrice(0)
                        .build());
                respObserver.onCompleted();
                return;
            }

            hotel.decrementFreeRooms();
            saveHotelsToCSV(hotels);

            String reservationId = UUID.randomUUID().toString();
            LocalDate from = LocalDate.parse(req.getStartDate());
            LocalDateTime deadline = LocalDateTime.now().plusMinutes(paymentTimeoutMinutes);

            Reservation reservation = new Reservation(
                    reservationId, hotel.getName(), req.getClientId(), from.atStartOfDay(), req.getDays(),
                    Reservation.Status.RESERVED, deadline, hotel.getCurrentPrice()
            );
            List<Reservation> reservations = loadReservationsFromCSV();
            reservations.add(reservation);
            saveReservationsToCSV(reservations);

            executor.schedule(() -> checkAndExpireReservation(reservationId), paymentTimeoutMinutes, TimeUnit.MINUTES);
            
            System.out.println("[LOG] Rezervacija napravljena: reservationId=" + reservationId +
            	    ", hotel=" + hotel.getName() +
            	    ", clientId=" + req.getClientId() +
            	    ", startDate=" + req.getStartDate() +
            	    ", days=" + req.getDays() +
            	    ", cenaPoNoci=" + hotel.getCurrentPrice());
            
            respObserver.onNext(booking.grpc.ReservationResponse.newBuilder()
                    .setAccepted(true)
                    .setMessage("Rezervacija uspesna! Reservation ID: " + reservationId + ". Rok za placanje: " + deadline)
                    .setPrice(hotel.getCurrentPrice() * req.getDays())
                    .build());
            respObserver.onCompleted();
        });
    }

    @Override
    public void payForReservation(booking.grpc.PaymentRequest req, StreamObserver<booking.grpc.PaymentResponse> respObserver) {
        executor.submit(() -> {
            List<Reservation> reservations = loadReservationsFromCSV();
            Optional<Reservation> opt = reservations.stream().filter(r -> r.getId().equals(req.getReservationId())).findFirst();
            if (!opt.isPresent()) {
                respObserver.onNext(booking.grpc.PaymentResponse.newBuilder()
                        .setSuccess(false)
                        .setMessage("Rezervacija ne postoji.")
                        .build());
                respObserver.onCompleted();
                return;
            }
            Reservation r = opt.get();
            if (r.getStatus() != Reservation.Status.RESERVED || r.getPaymentDeadline().isBefore(LocalDateTime.now())) {
                respObserver.onNext(booking.grpc.PaymentResponse.newBuilder()
                        .setSuccess(false)
                        .setMessage("Rezervacija nije validna za placanje ili je istekao rok.")
                        .build());
                respObserver.onCompleted();
                return;
            }

            r.setStatus(Reservation.Status.PAID);
            saveReservationsToCSV(reservations);

            double total = r.getReservedPrice() * r.getNights();
            double commission = total * commissionPercent;
            double hotelIncome = total - commission;

            azurirajFinansije(r.getHotelId(), hotelIncome, commission);

            String paidMsg = "Rezervacija [" + r.getId() + "] za hotel [" + r.getHotelId() + "] je PLACENA od strane klijenta [" + r.getClientId() + "]";
            notificationServer.broadcast(paidMsg, new booking.socket.NotificationServer.SearchParams("", 9999.0, 0));
            
            System.out.println("[LOG] Placanje izvrseno: reservationId=" + r.getId() +
            	    ", hotel=" + r.getHotelId() +
            	    ", clientId=" + r.getClientId() +
            	    ", ukupno=" + total +
            	    ", hotelIncome=" + hotelIncome +
            	    ", provizija=" + commission);
            
            respObserver.onNext(booking.grpc.PaymentResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Placanje uspesno!")
                    .build());
            respObserver.onCompleted();
        });
    }

    private void checkAndExpireReservation(String reservationId) {
        List<Reservation> reservations = loadReservationsFromCSV();
        Optional<Reservation> opt = reservations.stream().filter(r -> r.getId().equals(reservationId)).findFirst();
        if (opt.isPresent()) {
            Reservation r = opt.get();
            if (r.getStatus() == Reservation.Status.RESERVED && r.getPaymentDeadline().isBefore(LocalDateTime.now())) {
                r.setStatus(Reservation.Status.EXPIRED);
                List<Hotel> hotels = loadHotelsFromCSV();
                for (Hotel h : hotels) {
                    if (h.getName().equals(r.getHotelId())) {
                        h.incrementFreeRooms();
                        break;
                    }
                }
                saveHotelsToCSV(hotels);
                saveReservationsToCSV(reservations);

                String expiredMsg = "Rezervacija [" + r.getId() + "] za hotel [" + r.getHotelId() + "] je OTKAZANA (istekao rok).";
                notificationServer.broadcast(expiredMsg, new booking.socket.NotificationServer.SearchParams("", 9999.0, 0));
                System.out.println("[LOG] Rezervacija automatski otkazana (istekao rok): reservationId=" + r.getId() +
                	    ", hotel=" + r.getHotelId() +
                	    ", clientId=" + r.getClientId());
            }
        }
    }

    @Override
    public void cancelReservation(booking.grpc.CancelRequest req, StreamObserver<booking.grpc.CancelResponse> respObserver) {
        executor.submit(() -> {
            List<Reservation> reservations = loadReservationsFromCSV();
            Optional<Reservation> opt = reservations.stream()
                    .filter(r -> r.getId().equals(req.getReservationId()) && r.getClientId().equals(req.getClientId()))
                    .findFirst();
            if (!opt.isPresent()) {
                respObserver.onNext(booking.grpc.CancelResponse.newBuilder()
                        .setSuccess(false)
                        .setMessage("Rezervacija ne postoji ili nije vasa.")
                        .build());
                respObserver.onCompleted();
                return;
            }
            Reservation r = opt.get();
            if (r.getStatus() != Reservation.Status.RESERVED) {
                respObserver.onNext(booking.grpc.CancelResponse.newBuilder()
                        .setSuccess(false)
                        .setMessage("Rezervacija nije moguce otkazati (nije u RESERVED statusu).")
                        .build());
                respObserver.onCompleted();
                return;
            }
            r.setStatus(Reservation.Status.CANCELLED);
            List<Hotel> hotels = loadHotelsFromCSV();
            for (Hotel h : hotels) {
                if (h.getName().equals(r.getHotelId())) {
                    h.incrementFreeRooms();
                    break;
                }
            }
            saveHotelsToCSV(hotels);
            saveReservationsToCSV(reservations);

            String msg = "Rezervacija [" + r.getId() + "] za hotel [" + r.getHotelId() + "] je OTKAZANA od strane korisnika.";
            notificationServer.broadcast(msg, new booking.socket.NotificationServer.SearchParams("", 9999.0, 0));
            
            System.out.println("[LOG] Rezervacija otkazana od strane korisnika: reservationId=" + r.getId() +
            	    ", hotel=" + r.getHotelId() +
            	    ", clientId=" + r.getClientId());
            
            respObserver.onNext(booking.grpc.CancelResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Rezervacija otkazana.")
                    .build());
            respObserver.onCompleted();
        });
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