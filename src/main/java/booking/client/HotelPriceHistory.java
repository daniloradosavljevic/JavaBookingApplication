package booking.client;

public class HotelPriceHistory {
    private double minPrice;
    private double maxPrice;
    private double lastPrice;
    private boolean initialized = false;

    public void update(double price) {
        if (!initialized) {
            minPrice = maxPrice = lastPrice = price;
            initialized = true;
        } else {
            if (price < minPrice) minPrice = price;
            if (price > maxPrice) maxPrice = price;
            lastPrice = price;
        }
    }

    public double getMinPrice() { return minPrice; }
    public double getMaxPrice() { return maxPrice; }
    public double getLastPrice() { return lastPrice; }
    public boolean isInitialized() { return initialized; }
}