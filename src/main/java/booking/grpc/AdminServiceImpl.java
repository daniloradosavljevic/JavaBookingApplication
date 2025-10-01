package booking.grpc;

import booking.grpc.AdminServiceGrpc;
import booking.grpc.AdminEmpty;
import booking.grpc.HotelStatsResponse;
import booking.grpc.HotelStats;
import io.grpc.stub.StreamObserver;

import java.io.*;
import java.util.*;

public class AdminServiceImpl extends AdminServiceGrpc.AdminServiceImplBase {
    @Override
    public void getHotelStats(AdminEmpty req, StreamObserver<HotelStatsResponse> respObserver) {
        List<HotelInfo> hotels = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("booking-data/hoteli.csv"))) {
            String line; boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) { first = false; continue; }
                String[] p = line.split(",");
                HotelInfo info = new HotelInfo();
                info.id = p[0];
                info.name = p[1];
                info.category = Integer.parseInt(p[2]);
                info.city = p[3];
                info.freeRooms = Integer.parseInt(p[6]);
                info.currentPrice = Double.parseDouble(p[7]);
                hotels.add(info);
            }
        } catch (Exception ignored) {}

        try (BufferedReader br = new BufferedReader(new FileReader("booking-data/rezervacije.csv"))) {
            String line; boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) { first = false; continue; }
                String[] p = line.split(",");
                String hotelName = p[1];
                int nights = Integer.parseInt(p[4]);
                String status = p[5];
                for (HotelInfo info : hotels) {
                    if (info.name.equals(hotelName)) {
                        info.totalReservations++;
                        if ("PAID".equals(status)) info.soldNights += nights;
                    }
                }
            }
        } catch (Exception ignored) {}

        try (BufferedReader br = new BufferedReader(new FileReader("booking-data/finansije.csv"))) {
            String line; boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) { first = false; continue; }
                String[] p = line.split(",");
                String hotelId = p[0];
                double prihod = Double.parseDouble(p[1]);
                double provizija = Double.parseDouble(p[2]);
                for (HotelInfo info : hotels) {
                    if (info.id.equals(hotelId)) {
                        info.hotelIncome = prihod;
                        info.systemCommission = provizija;
                    }
                }
            }
        } catch (Exception ignored) {}

        HotelStatsResponse.Builder resp = HotelStatsResponse.newBuilder();
        for (HotelInfo h : hotels) {
            resp.addStats(HotelStats.newBuilder()
                    .setHotelName(h.name)
                    .setCity(h.city)
                    .setCategory(h.category)
                    .setFreeRooms(h.freeRooms)
                    .setTotalReservations(h.totalReservations)
                    .setSoldNights(h.soldNights)
                    .setHotelIncome(h.hotelIncome)
                    .setSystemCommission(h.systemCommission)
                    .setCurrentPrice(h.currentPrice)
                    .build());
        }
        respObserver.onNext(resp.build());
        respObserver.onCompleted();
    }

    static class HotelInfo {
        String id, name, city;
        int category, freeRooms, totalReservations = 0, soldNights = 0;
        double hotelIncome = 0, systemCommission = 0, currentPrice = 0;
    }
}