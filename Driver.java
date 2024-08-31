//package com.RideShare;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class Driver extends User {
    public Driver(int id, String name, String email, String password) {
    	super(id, name, email, password, "DRIVER");
    }

    public void createRide(String startLocation, String endLocation, String dateTime, int seatsAvailable) {
        try (Connection conn = Database.getConnection()) {
            String sql = "INSERT INTO rides (start_location, end_location, date_time, seats_available, driver_id) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, startLocation);
            stmt.setString(2, endLocation);
            stmt.setString(3, dateTime);
            stmt.setInt(4, seatsAvailable);
            stmt.setInt(5,this.getId()); 
            stmt.executeUpdate();
            System.out.println("Ride created successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewMyRides() {
        RideDAO rideDAO = new RideDAO();
        List<Ride> rides = rideDAO.getRidesByDriver(this.getId());
        
        if (rides.isEmpty()) {
            System.out.println("No rides found.");
        } else {
            for (Ride ride : rides) {
                System.out.println("Ride ID: " + ride.getId() + ", Source: " + ride.getsource() + ", Destination: " + ride.getdestination() + ", Date: " + ride.getdate());
            }
 }
}
}
