package booking.core;

import java.util.HashMap;
import java.util.Map;

public class BookingSystemState {
    private final Map<String, Hotel> hotels = new HashMap<>();
    private final Map<String, Reservation> reservations = new HashMap<>();
    private final Map<String, Client> clients = new HashMap<>();
    private final Map<String, HotelAccount> hotelAccounts = new HashMap<>();
    private double systemCommissionBalance = 0.0;

    public Map<String, Hotel> getHotels() { return hotels; }
    public Map<String, Reservation> getReservations() { return reservations; }
    public Map<String, Client> getClients() { return clients; }
    public Map<String, HotelAccount> getHotelAccounts() { return hotelAccounts; }

    public double getSystemCommissionBalance() { return systemCommissionBalance; }
    public void addSystemCommission(double commission) { this.systemCommissionBalance += commission; }
}