package booking.grpc;

import booking.core.Hotel;
import booking.socket.NotificationServer;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class HotelCsvWatcher extends Thread {
    private final String csvPath;
    private final NotificationServer notificationServer;
    private List<Hotel> lastHotels = new ArrayList<>();

    public HotelCsvWatcher(String csvPath, NotificationServer notificationServer) {
        this.csvPath = csvPath;
        this.notificationServer = notificationServer;
        this.lastHotels = loadHotels();
    }

    @Override
    public void run() {
        try {
            Path csvFile = Paths.get(csvPath).toAbsolutePath();
            Path dir = csvFile.getParent();
            WatchService watchService = FileSystems.getDefault().newWatchService();
            dir.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

            System.out.println("HotelCsvWatcher aktivan za: " + csvFile);

            while (true) {
                WatchKey key = watchService.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();
                    Path changed = (Path) event.context();
                    if (kind == StandardWatchEventKinds.ENTRY_MODIFY && changed.toString().equals(csvFile.getFileName().toString())) {
                        Thread.sleep(100); 
                        handleCsvChange();
                    }
                }
                key.reset();
            }
        } catch (Exception e) {
            System.err.println("Greska u HotelCsvWatcher: " + e.getMessage());
        }
    }

    private List<Hotel> loadHotels() {
        List<Hotel> hotels = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(csvPath))) {
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
            System.err.println("Greska pri ucitavanju hotela iz CSV (watcher): " + e.getMessage());
        }
        return hotels;
    }

    private void handleCsvChange() {
        List<Hotel> newHotels = loadHotels();
        Map<String, Hotel> oldMap = new HashMap<>();
        for (Hotel h : lastHotels) oldMap.put(h.getId(), h);

        for (Hotel newHotel : newHotels) {
            Hotel oldHotel = oldMap.get(newHotel.getId());
            if (oldHotel == null) continue;
            boolean changed = false;
            String type = "";
            if (oldHotel.getFreeRooms() != newHotel.getFreeRooms()) {
                changed = true;
                type = "Promena dostupnosti";
            }
            if (oldHotel.getCurrentPrice() != newHotel.getCurrentPrice()) {
                changed = true;
                type = "Promena cene";
            }
            if (changed) {
                NotificationServer.SearchParams params = new NotificationServer.SearchParams(
                        newHotel.getCity(), newHotel.getDistanceFromCenter(), newHotel.getCategory());
                String msg = "Promena [" + type + "]: " + newHotel.getName()
                        + " | Grad: " + newHotel.getCity()
                        + " | Kategorija: " + newHotel.getCategory()
                        + " | Udaljenost: " + newHotel.getDistanceFromCenter()
                        + " | Slobodnih soba: " + newHotel.getFreeRooms()
                        + " | Cena: " + newHotel.getCurrentPrice();
                notificationServer.broadcast(msg, params);
                System.out.println("[WATCH] " + msg);
            }
        }

        lastHotels = newHotels;
    }
}