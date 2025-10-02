# JavaBookingApplication

**Master's coursework**  
Faculty of Engineering, University of Kragujevac

---

## About

This project is a hotel booking system implemented in Java using gRPC and CSV-based data storage.  
It was created as part of Master's studies at the Faculty of Engineering in Kragujevac.

---

## How to Run

### 1. Build

Make sure you have Java and Maven installed.

```sh
mvn clean install
```


### 2. Start the Servers


**Run Booking Server:**
```sh
java -jar target/raf-pds-grpc-1.0-jar-with-dependencies.jar
```
or
```sh
java -cp target/raf-pds-grpc-1.0.jar;target/lib/* booking.server.BookingServer
```
- The server will start NotificationServer, gRPC BookingService (port 50051), and AdminService (port 7000).
- You can edit number bot clients in BookingServer code (0 by default).

### 3. Start a Client

**Booking Client:**
```sh
java -cp target/raf-pds-grpc-1.0.jar;target/lib/* booking.client.BookingClient
```

**Admin Client:**
```sh
java -cp target/raf-pds-grpc-1.0.jar;target/lib/* booking.client.AdminClient
```

> **Note:** Use `:` instead of `;` as separator on Linux/Mac.

---

## Features

### User Functionality (BookingClient)
- **Hotel search** – search hotels by city, distance, and category.
- **Reservation** – book a hotel room for selected dates.
- **Payment** – pay for reservations before deadline.
- **Cancellation** – cancel reservations (room is marked free).
- **Price history** – view minimum, maximum, and latest prices for each hotel.
- **Live notifications** – get notified (in-app) about changes in hotel availability, price changes, and reservation status.

### Admin Functionality (AdminClient)
- **Hotel and system financial overview** – see stats for each hotel: income, commission, reservations, sold nights, current price, etc.

### Server Features
- **CSV-based storage** – hotels, reservations, and financial reports are kept as CSV files, simulating a simple backend database.
- **Automatic price adjustment** – if a hotel has ≤1 free room, price goes up by 30%; if it has ≥10, price drops by 30%. Price returns to base when rooms between 2 and 9.
- **Bot clients** – simulate automatic hotel reservations for load/testing.
- **Notification system** – clients get real-time updates via NotificationServer.

