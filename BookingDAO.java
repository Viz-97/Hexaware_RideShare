//package com.RideShare;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BookingDAO {
	public void createBooking(int riderId, int rideId) {
        String sql = "INSERT INTO bookings (rider_id, ride_id) VALUES (?, ?)";
        
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, riderId);
            stmt.setInt(2, rideId);
            stmt.executeUpdate();
            System.out.println("Booking created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cancelBooking(int riderId, int rideId) {
        try (Connection conn = Database.getConnection()) {
            String sql = "DELETE FROM bookings WHERE rider_id = ? AND ride_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, riderId);
            stmt.setInt(2, rideId);
            stmt.executeUpdate();
            System.out.println("Booking canceled successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
