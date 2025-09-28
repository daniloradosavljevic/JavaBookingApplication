package booking.core;

import java.util.ArrayList;
import java.util.List;

public class Client {
    private final String id;
    private int rewardPoints;
    private final List<String> reservationIds = new ArrayList<>();

    public Client(String id) {
        this.id = id;
    }

    public String getId() { return id; }
    public int getRewardPoints() { return rewardPoints; }
    public void addRewardPoints(int points) { this.rewardPoints += points; }
    public void subtractRewardPoints(int points) { this.rewardPoints -= points; }
    public List<String> getReservationIds() { return reservationIds; }
    public void addReservationId(String reservationId) { this.reservationIds.add(reservationId); }
}