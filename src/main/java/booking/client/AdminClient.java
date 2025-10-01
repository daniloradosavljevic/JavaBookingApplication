package booking.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import booking.grpc.AdminServiceGrpc;
import booking.grpc.AdminEmpty;
import booking.grpc.HotelStatsResponse;
import booking.grpc.HotelStats;

public class AdminClient {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 7000)
                .usePlaintext()
                .build();
        AdminServiceGrpc.AdminServiceBlockingStub stub = AdminServiceGrpc.newBlockingStub(channel);

        HotelStatsResponse resp = stub.getHotelStats(AdminEmpty.newBuilder().build());
        System.out.println("=== ADMIN PREGLED HOTELA ===");
        for (HotelStats h : resp.getStatsList()) {
            System.out.println("-----------------------------------");
            System.out.println("Naziv: " + h.getHotelName());
            System.out.println("Grad: " + h.getCity());
            System.out.println("Kategorija: " + h.getCategory());
            System.out.println("Slobodnih soba: " + h.getFreeRooms());
            System.out.println("Broj rezervacija: " + h.getTotalReservations());
            System.out.println("Broj prodatih nocenja: " + h.getSoldNights());
            System.out.println("Ukupni prihod hotela: " + h.getHotelIncome());
            System.out.println("Ukupna provizija sistema: " + h.getSystemCommission());
            System.out.println("Trenutna cena: " + h.getCurrentPrice());
        }
        channel.shutdown();
    }
}