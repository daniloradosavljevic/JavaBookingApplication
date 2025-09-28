package booking.core;

public class HotelAccount {
    private final String hotelId;
    private double balance;
    private int totalNightsSold;
    private double totalCommissionPaid;

    public HotelAccount(String hotelId) {
        this.hotelId = hotelId;
        this.balance = 0.0;
        this.totalNightsSold = 0;
        this.totalCommissionPaid = 0.0;
    }

    public String getHotelId() { return hotelId; }
    public double getBalance() { return balance; }
    public void addToBalance(double amount) { this.balance += amount; }
    public void subtractFromBalance(double amount) { this.balance -= amount; }
    public int getTotalNightsSold() { return totalNightsSold; }
    public void addNightsSold(int nights) { this.totalNightsSold += nights; }
    public double getTotalCommissionPaid() { return totalCommissionPaid; }
    public void addCommission(double commission) { this.totalCommissionPaid += commission; }
}