package booking.core;

import java.util.ArrayList;
import java.util.List;

public class Hotel {
    private final String id;
    private final String name;
    private final int category; 
    private final String city;
    private final double distanceFromCenter;
    private final int capacity;
    private int freeRooms;
    private double currentPrice;
    private final List<Double> priceHistory = new ArrayList<>();

    public Hotel(String id, String name, int category, String city, double distanceFromCenter, int capacity, double startingPrice) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.city = city;
        this.distanceFromCenter = distanceFromCenter;
        this.capacity = capacity;
        this.freeRooms = capacity;
        this.currentPrice = startingPrice;
        this.priceHistory.add(startingPrice);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getCategory() { return category; }
    public String getCity() { return city; }
    public double getDistanceFromCenter() { return distanceFromCenter; }
    public int getCapacity() { return capacity; }
    public int getFreeRooms() { return freeRooms; }
    public double getCurrentPrice() { return currentPrice; }
    public List<Double> getPriceHistory() { return priceHistory; }

    public void setFreeRooms(int freeRooms) { this.freeRooms = freeRooms; }
    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
        this.priceHistory.add(currentPrice);
    }

    public void incrementFreeRooms() {
        if (freeRooms < capacity) freeRooms++;
    }

    public void decrementFreeRooms() {
        if (freeRooms > 0) freeRooms--;
    }

    public double getMinPrice() {
        return priceHistory.stream().min(Double::compare).orElse(currentPrice);
    }

    public double getMaxPrice() {
        return priceHistory.stream().max(Double::compare).orElse(currentPrice);
    }
    
}