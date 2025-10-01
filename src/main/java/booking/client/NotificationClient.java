package booking.client;

import java.io.*;
import java.net.Socket;
import java.util.Map;
import booking.client.HotelPriceHistory;

public class NotificationClient implements Runnable {
    private volatile boolean running = true;
    private final String city;
    private final double maxDistance;
    private final int minCategory;
    private final Map<String, HotelPriceHistory> hotelPriceHistory;

    public NotificationClient(String city, double maxDistance, int minCategory, Map<String, HotelPriceHistory> hotelPriceHistory) {
        this.city = city;
        this.maxDistance = maxDistance;
        this.minCategory = minCategory;
        this.hotelPriceHistory = hotelPriceHistory;
    }

    @Override
    public void run() {
        try (Socket socket = new Socket("localhost", 6000);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println(city + ";" + maxDistance + ";" + minCategory);

            while (running) {
                String msg = in.readLine();
                if (msg == null) break;
                if (matchesFilter(msg)) {
                    updatePriceHistoryFromNotification(msg);
                    System.out.println("[NOTIFIKACIJA] " + msg);
                    BookingClient.printMenuPrompt();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikaciona konekcija zatvorena.");
        }
    }

    private boolean matchesFilter(String msg) {
        try {
            String grad = extractField(msg, "Grad:");
            int kategorija = Integer.parseInt(extractField(msg, "Kategorija:"));
            double udaljenost = Double.parseDouble(extractField(msg, "Udaljenost:"));

            boolean gradOk = city.isEmpty() || grad.equalsIgnoreCase(city);
            boolean katOk = kategorija >= minCategory;
            boolean distOk = udaljenost <= maxDistance;

            return gradOk && katOk && distOk;
        } catch (Exception e) {
            return false;
        }
    }

    private String extractField(String msg, String label) {
        int idx = msg.indexOf(label);
        if (idx < 0) return "";
        int start = idx + label.length();
        int end = msg.indexOf("|", start);
        return msg.substring(start, end > 0 ? end : msg.length()).trim();
    }

    private void updatePriceHistoryFromNotification(String msg) {
        try {
            if (msg.contains("Promena [Promena cene]:")) {
                int start = msg.indexOf("]:") + 3;
                int end = msg.indexOf("| Grad");
                String hotelName = msg.substring(start, end).trim();

                int priceIdx = msg.lastIndexOf("Cena: ");
                double newPrice = Double.parseDouble(msg.substring(priceIdx + 6).trim());

                hotelPriceHistory
                        .computeIfAbsent(hotelName, k -> new HotelPriceHistory())
                        .update(newPrice);
            }
        } catch (Exception e) {
            // ignore parse errors
        }
    }

    public void stop() {
        running = false;
    }
}