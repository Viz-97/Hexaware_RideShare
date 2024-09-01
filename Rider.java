//package com.RideShare;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Rider extends User {
    public Rider(int id, String name, String email, String password) {
    	super(id, name, email, password, "RIDER");
    }

    public void bookRide(int rideId) {
        BookingDAO bookingDAO = new BookingDAO();
        bookingDAO.createBooking(this.getId(), rideId);
    }

    public  void viewMyBookings() {
        try (
        		Connection conn = Database.getConnection()) {
            // Query to get all booked rides for the current rider
            String query = "SELECT r.id, r.start_location, r.end_location, r.date_time, r.seats_available, b.status " +
                           "FROM rides r " +
                           "JOIN bookings b ON r.id = b.ride_id " +
                           "WHERE b.rider_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1,getId()); 
            ResultSet rs = stmt.executeQuery();

            if (!rs.isBeforeFirst()) { 
                System.out.println("You have not booked any rides.");
            } else {
                System.out.println("Your booked rides:");
                while (rs.next()) {
                    int rideId = rs.getInt("id");
                    String source = rs.getString("start_location");
                    String destination = rs.getString("end_location");
                    String date = rs.getString("date_time");
                    int availableSeats = rs.getInt("seats_available");
                    String status = rs.getString("status");

                    System.out.println("Ride ID: " + rideId + ", Source: " + source +
                                       ", Destination: " + destination + ", Date: " + date +
                                       ", Available Seats: " + availableSeats + ", Status: " + status);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void cancelBooking(int rideId) {
        
    }
}
