package booking.core;

import java.time.LocalDateTime;

public class Reservation {
    public enum Status { RESERVED, PAID, CANCELLED, EXPIRED }

    private final String id;
    private final String hotelId;
    private final String clientId;
    private final LocalDateTime from;
    private final int nights;
    private Status status;
    private final LocalDateTime paymentDeadline;
    private final double reservedPrice;

    public Reservation(String id, String hotelId, String clientId, LocalDateTime from, int nights, Status status, LocalDateTime paymentDeadline, double reservedPrice) {
        this.id = id;
        this.hotelId = hotelId;
        this.clientId = clientId;
        this.from = from;
        this.nights = nights;
        this.status = status;
        this.paymentDeadline = paymentDeadline;
        this.reservedPrice = reservedPrice;
    }

    public String getId() { return id; }
    public String getHotelId() { return hotelId; }
    public String getClientId() { return clientId; }
    public LocalDateTime getFrom() { return from; }
    public int getNights() { return nights; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public LocalDateTime getPaymentDeadline() { return paymentDeadline; }
    public double getReservedPrice() { return reservedPrice; }
}