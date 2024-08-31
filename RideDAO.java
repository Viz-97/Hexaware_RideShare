//package com.RideShare;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RideDAO {

    public void createRide(int driverId, String startLocation, String endLocation, String dateTime, int seatsAvailable) {
        try (Connection conn = Database.getConnection()) {
            String sql = "INSERT INTO rides (driver_id, start_location, end_location, date_time, seats_available) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, driverId);
            stmt.setString(2, startLocation);
            stmt.setString(3, endLocation);
            stmt.setString(4, dateTime);
            stmt.setInt(5, seatsAvailable);
            stmt.executeUpdate();
            System.out.println("Ride created successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Ride> getRidesByDriver(int driverId) {
        List<Ride> rides = new ArrayList<>();
        String sql = "SELECT * FROM rides WHERE driver_id = ?";
        
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, driverId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                int id = rs.getInt("id");
                String source = rs.getString("start_location");
                String destination = rs.getString("end_location");
                String date = rs.getString("date_time");
                int seatsAvailable = rs.getInt("seats_Available");
                rides.add(new Ride(id, driverId, source, destination, date,seatsAvailable));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rides;
    }
    
    public List<Ride> getAvailableRides() {
        List<Ride> rides = new ArrayList<>();
        try (Connection conn = Database.getConnection();
        		PreparedStatement stmt = conn.prepareStatement("SELECT * FROM rides WHERE seats_Available > 0")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                int driverId = rs.getInt("driver_id");
                String source = rs.getString("start_location");
                String destination = rs.getString("end_location");
                String date = rs.getString("date_time");
                int availableSeats = rs.getInt("seats_Available"); // Fetch available seats
                Ride ride = new Ride(id, driverId, source, destination, date, availableSeats);
                rides.add(ride);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rides;
    }

    public boolean bookRide(int rideId) {
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE rides SET status = 'BOOKED' WHERE id = ?")) {
            stmt.setInt(1, rideId);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
