package booking.socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class NotificationServer {
    private final int port = 6000;
    private final List<ClientHandler> clients = Collections.synchronizedList(new ArrayList<>());

    public void start() throws Exception {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Notification server started on port " + port);

        new Thread(() -> {
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    ClientHandler handler = new ClientHandler(socket);
                    clients.add(handler);
                    handler.start();
                    System.out.println("Klijent povezan na notifikacije.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void broadcast(String message, SearchParams params) {
        synchronized (clients) {
            Iterator<ClientHandler> iter = clients.iterator();
            while (iter.hasNext()) {
                ClientHandler client = iter.next();
                if (client.matches(params)) {
                    try {
                        client.send(message);
                    } catch (Exception e) {
                        iter.remove();
                    }
                }
            }
        }
    }
    
    public void setClientParams(ClientHandler client, SearchParams params) {
        client.setParams(params);
    }

    public static class SearchParams {
        public final String city;
        public final double maxDistance;
        public final int minCategory;

        public SearchParams(String city, double maxDistance, int minCategory) {
            this.city = city == null ? "" : city;
            this.maxDistance = maxDistance;
            this.minCategory = minCategory;
        }
    }

    public static class ClientHandler extends Thread {
        private final Socket socket;
        private PrintWriter out;
        private SearchParams params;

        public ClientHandler(Socket socket) throws Exception {
            this.socket = socket;
            this.out = new PrintWriter(socket.getOutputStream(), true);
        }

        public void setParams(SearchParams params) { this.params = params; }

        public boolean matches(SearchParams eventParams) {
            if (params == null) return false;
            boolean cityMatch = params.city.isEmpty() || eventParams.city.equalsIgnoreCase(params.city);
            boolean distMatch = params.maxDistance >= eventParams.maxDistance;
            boolean catMatch = params.minCategory <= eventParams.minCategory;
            return cityMatch && distMatch && catMatch;
        }

        public void send(String msg) {
            out.println(msg);
        }

        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String filterStr = in.readLine();
                if (filterStr != null) {
                    String[] parts = filterStr.split(";");
                    String city = parts.length > 0 ? parts[0] : "";
                    double maxDist = parts.length > 1 && !parts[1].isEmpty() ? Double.parseDouble(parts[1]) : 9999.0;
                    int minCat = parts.length > 2 && !parts[2].isEmpty() ? Integer.parseInt(parts[2]) : 0;
                    setParams(new SearchParams(city, maxDist, minCat));
                }
                while (true) {
                    if (in.readLine() == null) break; 
                }
            } catch (Exception ignored) {}
        }
    }
}