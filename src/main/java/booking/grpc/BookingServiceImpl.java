package booking.grpc;
import booking.core.Hotel;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import booking.grpc.BookingServiceGrpc;
import booking.grpc.SearchRequest;
import booking.grpc.SearchResponse;
import booking.grpc.ReservationRequest;
import booking.grpc.ReservationResponse;
import booking.grpc.PaymentRequest;
import booking.grpc.PaymentResponse;
import io.grpc.stub.StreamObserver;

public class BookingServiceImpl extends BookingServiceGrpc.BookingServiceImplBase {
	
	private static final List<Hotel> hotels = new ArrayList<>();

    static {
        hotels.add(new Hotel("hns1", "Hotel Novi Sad", 4, "Novi Sad", 1.2, 10, 50.0));
        hotels.add(new Hotel("hns2", "Hotel Centar", 5, "Novi Sad", 0.5, 2, 120.0));
        hotels.add(new Hotel("hbg1", "Hotel Beograd", 3, "Beograd", 0.9, 5, 70.0));
        hotels.add(new Hotel("hnis1", "Hotel Niš", 3, "Niš", 2.0, 8, 35.0));
    }
    @Override
    public void searchHotels(SearchRequest request, StreamObserver<SearchResponse> responseObserver) {
        List<Hotel> filteredHotels = hotels.stream()
                .filter(h -> h.getCity().equalsIgnoreCase(request.getCity()))
                .filter(h -> h.getDistanceFromCenter() <= request.getMaxDistance())
                .filter(h -> h.getCategory() >= request.getMinCategory())
                .filter(h -> h.getFreeRooms() > 0)
                .collect(Collectors.toList());

        List<booking.grpc.Hotel> protoHotels = filteredHotels.stream()
                .map(h -> booking.grpc.Hotel.newBuilder()
                        .setName(h.getName())
                        .setCategory(h.getCategory())
                        .setCity(h.getCity())
                        .setDistanceFromCenter(h.getDistanceFromCenter())
                        .setAvailableRooms(h.getFreeRooms())
                        .setCurrentPrice(h.getCurrentPrice())
                        .build())
                .collect(Collectors.toList());

        SearchResponse response = SearchResponse.newBuilder()
                .addAllHotels(protoHotels)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void makeReservation(ReservationRequest request, StreamObserver<ReservationResponse> responseObserver) {
        ReservationResponse response = ReservationResponse.newBuilder()
                .setAccepted(false)
                .setMessage("Not implemented yet")
                .setPrice(0)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void payForReservation(PaymentRequest request, StreamObserver<PaymentResponse> responseObserver) {
        PaymentResponse response = PaymentResponse.newBuilder()
                .setSuccess(false)
                .setMessage("Not implemented yet")
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}