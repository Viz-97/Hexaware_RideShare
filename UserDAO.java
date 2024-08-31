import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.*;
import java.sql.Timestamp;

public class UserDAO {
	private static Scanner scanner = new Scanner(System.in);
    // Create a new user
	public static void createUser() {
	    System.out.print("Enter name: ");
	    String name = scanner.nextLine();

	    System.out.print("Enter email: ");
	    String email = scanner.nextLine();

	    System.out.print("Enter password: ");
	    String password = scanner.nextLine();

	    System.out.print("Enter role (DRIVER/RIDER/ADMIN): ");
	    String role = scanner.nextLine();

	    try (Connection conn = Database.getConnection()) {
	        String sql = "INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, ?)";
	        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
	            stmt.setString(1, name);
	            stmt.setString(2, email);
	            // Store the password as plain text (not recommended)
	            stmt.setString(3, password);
	            stmt.setString(4, role);
	            stmt.executeUpdate();
	            System.out.println("User created successfully");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

 

    // Retrieve user by email
    public User getUserByEmail(String email) {
        User user = null;
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE email = ?")) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String password = rs.getString("password");
                    String role = rs.getString("role");

                    switch (role) {
                        case "DRIVER":
                            user = new Driver(id, name, email, password);
                            break;
                        case "RIDER":
                            user = new Rider(id, name, email, password);
                            break;
                        case "ADMIN":
                            user = new Admin(id, name, email, password);
                            break;
                        default:
                            throw new IllegalArgumentException("Unknown role: " + role);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    // Check user credentials
    public boolean checkUserCredentials(String email, String password) {
        User user = getUserByEmail(email);
        return user != null && user.getPassword().equals(password); // Hash comparison needed
    }

    // Retrieve all users
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM users")) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String password = rs.getString("password");
                String role = rs.getString("role");

                switch (role) {
                    case "DRIVER":
                        users.add(new Driver(id, name, email, password));
                        break;
                    case "RIDER":
                        users.add(new Rider(id, name, email, password));
                        break;
                    case "ADMIN":
                        users.add(new Admin(id, name, email, password));
                        break;
                    default:
                        System.err.println("Unknown role: " + role);
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    // Remove a user by ID
    public boolean removeUser(int userId) {
        try (Connection conn = Database.getConnection()) {
            String sql = "DELETE FROM users WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, userId);
                int rowsAffected = stmt.executeUpdate();
                System.out.println("User Removed");
                return rowsAffected > 0; // Return true if a user was removed
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Update ride booking
    public static boolean updateRideBooking(int rideId, int riderId) {
        try (Connection conn = Database.getConnection()) {
            conn.setAutoCommit(false);

            // Check current available seats
            String checkSeatsQuery = "SELECT seats_Available FROM rides WHERE id = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSeatsQuery)) {
                checkStmt.setInt(1, rideId);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        int availableSeats = rs.getInt("seats_Available");

                        if (availableSeats > 0) {
                            // Update available seats
                            String updateSeatsQuery = "UPDATE rides SET seats_Available = seats_Available - 1 WHERE id = ?";
                            try (PreparedStatement updateStmt = conn.prepareStatement(updateSeatsQuery)) {
                                updateStmt.setInt(1, rideId);
                                updateStmt.executeUpdate();
                            }

                            // Insert into bookings table
                            String insertBookingQuery = "INSERT INTO bookings (ride_id, rider_id, status) VALUES (?, ?, 'BOOKED')";
                            try (PreparedStatement insertStmt = conn.prepareStatement(insertBookingQuery)) {
                                insertStmt.setInt(1, rideId);
                                insertStmt.setInt(2, riderId);
                                insertStmt.executeUpdate();
                            }

                            // Check if seats are now zero
                            if (availableSeats - 1 == 0) {
                                String updateRideStatusQuery = "UPDATE rides SET status = 'BOOKED' WHERE id = ?";
                                try (PreparedStatement updateStatusStmt = conn.prepareStatement(updateRideStatusQuery)) {
                                    updateStatusStmt.setInt(1, rideId);
                                    updateStatusStmt.executeUpdate();
                                }
                            }

                            conn.commit();
                            return true; // Booking successful
                        }
                    }
                }
            }
            conn.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Booking failed (no seats available or ride not found)
    }

    // Cancel booking
    public static boolean canceLBooking(int riderId, int rideIdToCancel) {
        try (Connection conn = Database.getConnection()) {
            conn.setAutoCommit(false);

            // Check if the user has a booking for the entered ride ID with status 'BOOKED'
            String checkBookingQuery = "SELECT id FROM bookings WHERE rider_id = ? AND ride_id = ? AND status = 'BOOKED'";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkBookingQuery)) {
                checkStmt.setInt(1, riderId);
                checkStmt.setInt(2, rideIdToCancel);
                try (ResultSet checkRs = checkStmt.executeQuery()) {
                    if (checkRs.next()) {
                        int bookingIdToCancel = checkRs.getInt("id");

                        // Update the booking status to 'AVAILABLE'
                        String updateBookingStatusQuery = "UPDATE bookings SET status = 'AVAILABLE' WHERE id = ?";
                        try (PreparedStatement updateStatusStmt = conn.prepareStatement(updateBookingStatusQuery)) {
                            updateStatusStmt.setInt(1, bookingIdToCancel);
                            int statusRowsUpdated = updateStatusStmt.executeUpdate();

                            if (statusRowsUpdated > 0) {
                                // Update available seats for the ride
                                String updateSeatsQuery = "UPDATE rides SET seats_Available = seats_Available + 1 WHERE id = ?";
                                try (PreparedStatement updateSeatsStmt = conn.prepareStatement(updateSeatsQuery)) {
                                    updateSeatsStmt.setInt(1, rideIdToCancel);
                                    int seatsRowsUpdated = updateSeatsStmt.executeUpdate();

                                    if (seatsRowsUpdated > 0) {
                                        conn.commit();
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            conn.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Retrieve all rides (Added method for Admin)
    public List<Ride> getAllRides() {
        List<Ride> rides = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM rides")) {
            while (rs.next()) {
                int id = rs.getInt("id");
                int driverId = rs.getInt("driver_id"); // Retrieve the driver ID
                String source = rs.getString("start_location");
                String destination = rs.getString("end_location");
                String date = rs.getString("date_time");
                int seatsAvailable = rs.getInt("seats_Available");
                
                // Create the Ride object with all necessary parameters
                rides.add(new Ride(id, driverId, source, destination, date, seatsAvailable));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rides;
    }
    
    public static void giveFeedback() {
        System.out.print("Enter your user ID: ");
        int userId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter your user type (DRIVER/RIDER): ");
        String userType = scanner.nextLine();

        System.out.print("Enter ride ID: ");
        int rideId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter your feedback: ");
        String feedbackText = scanner.nextLine();

        try (Connection conn = Database.getConnection()) {
            String sql = "INSERT INTO feedback (userId, userType, rideId, feedbackText) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, userId);
                stmt.setString(2, userType);
                stmt.setInt(3, rideId);
                stmt.setString(4, feedbackText);
                stmt.executeUpdate();
                System.out.println("Feedback submitted successfully");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
 // Retrieve feedback for a specific ride (parameter-free)
    public void getFeedbackForRide() {
        System.out.print("Enter ride ID to retrieve feedback: ");
        int rideId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        List<Feedback> feedbackList = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM feedback WHERE rideId = ?")) {
            stmt.setInt(1, rideId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int userId = rs.getInt("userId");
                    String userType = rs.getString("userType");
                    String feedbackText = rs.getString("feedbackText");
                    Timestamp timestamp = rs.getTimestamp("timestamp");  // Retrieve timestamp

                    // Add feedback to the list including the timestamp
                    feedbackList.add(new Feedback(userId, userType, rideId, feedbackText, timestamp));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Print retrieved feedback
        if (feedbackList.isEmpty()) {
            System.out.println("No feedback found for this ride.");
        } else {
            for (Feedback feedback : feedbackList) {
                System.out.println("User ID: " + feedback.getUserId());
                System.out.println("User Type: " + feedback.getUserType());
                System.out.println("Feedback: " + feedback.getFeedbackText());
                System.out.println("Timestamp: " + feedback.getTimestamp());  // Print timestamp
                System.out.println("--------------------------");
            }
        }
    }
    public static void viewAllFeedback() {
        List<Feedback> feedbackList = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM feedback")) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int userId = rs.getInt("userId");
                    String userType = rs.getString("userType");
                    int rideId = rs.getInt("rideId");
                    String feedbackText = rs.getString("feedbackText");
                    Timestamp timestamp = rs.getTimestamp("timestamp");

                    feedbackList.add(new Feedback(userId, userType, rideId, feedbackText, timestamp));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Print all feedback
        if (feedbackList.isEmpty()) {
            System.out.println("No feedback available.");
        } else {
            for (Feedback feedback : feedbackList) {
                System.out.println("User ID: " + feedback.getUserId());
                System.out.println("User Type: " + feedback.getUserType());
                System.out.println("Ride ID: " + feedback.getRideId());
                System.out.println("Feedback: " + feedback.getFeedbackText());
                System.out.println("Timestamp: " + feedback.getTimestamp());
                System.out.println("--------------------------");
            }
        }
    }

}
    
