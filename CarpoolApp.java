//package com.RideShare;

import java.util.*;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;


public class CarpoolApp extends UserDAO {
    private static Scanner scanner = new Scanner(System.in);
    private static User currentUser;
    UserDAO u = new UserDAO();

    public static void main(String[] args) {
        while (true) {
            System.out.println("Welcome to CarpoolApp");
            System.out.println("1. Login");
            System.out.println("2. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine(); 

            if (choice == 1) {
                login();
            } else if (choice == 2) {
                System.out.println("Exiting...");
                break;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void login() {
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        currentUser = User.login(email, password);
        if (currentUser != null) {
            System.out.println("Login successful!");
            showMenu();
        } else {
            System.out.println("Invalid email or password.");
        }
    }

    private static void showMenu() {
        while (true) {
            if (currentUser instanceof Driver) {
                showDriverMenu();
            } else if (currentUser instanceof Rider) {
                showRiderMenu();
            } else if (currentUser instanceof Admin) {
                showAdminMenu();
            } else {
                System.out.println("Unknown user role.");
                break;
            }
        }
    }

    private static void showDriverMenu() {
        System.out.println("Driver Menu");
        System.out.println("1. Create Ride");
        System.out.println("2. View My Rides");
        System.out.println("3. Give Feedback");
        System.out.println("4. Logout");
        int choice = scanner.nextInt();
        scanner.nextLine(); 

        switch (choice) {
            case 1:
                createRide();
                break;
            case 2:
                ((Driver) currentUser).viewMyRides();
                break;
            case 3:
                giveFeedback(); 
                break;
            case 4:
                currentUser = null;
                System.out.println("Logged out successfully.");
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
    }

    private static void createRide() {
        System.out.print("Enter start location: ");
        String startLocation = scanner.nextLine();
        System.out.print("Enter end location: ");
        String endLocation = scanner.nextLine();
        System.out.print("Enter date and time (yyyy-MM-dd HH:mm:ss): ");
        String dateTime = scanner.nextLine();
        System.out.print("Enter number of seats available: ");
        int seatsAvailable = scanner.nextInt();
        scanner.nextLine(); 

        ((Driver) currentUser).createRide(startLocation, endLocation, dateTime, seatsAvailable);
    }

    private static void showRiderMenu() {
        System.out.println("Rider Menu");
        System.out.println("1. Book Ride");
        System.out.println("2. View My Bookings");
        System.out.println("3. Cancel Booking");
        System.out.println("4. Give Feedback");
        System.out.println("5. Logout");
        int choice = scanner.nextInt();
        scanner.nextLine(); 

        switch (choice) {
            case 1:
                bookRide();
                break;
            case 2:
                ((Rider) currentUser).viewMyBookings();
                break;
            case 3:
                cancelBooking();
                break;
            case 4:
                giveFeedback(); 
                break;
            case 5:
                currentUser = null;
                System.out.println("Logged out successfully.");
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
    }

    public static void bookRide() {
        // Display available rides
        displayAvailableRides();

        // Prompt the user to enter the ride ID they want to book
        System.out.print("Enter Ride ID to book: ");
        int rideId = scanner.nextInt();
        scanner.nextLine(); 

        // Call the method to update the ride booking
        boolean success = updateRideBooking(rideId, currentUser.getId());

        if (success) {
            System.out.println("Ride booked successfully.");
        } else {
            System.out.println("Booking failed. No seats available or ride not found.");
        }
    }



    public static void displayAvailableRides() {
        RideDAO rideDAO = new RideDAO();
        List<Ride> rides = rideDAO.getAvailableRides();
        
        if (rides.isEmpty()) {
            System.out.println("No rides available.");
        } else {
            for (Ride ride : rides) {
                System.out.println("Ride ID: " + ride.getId() + ", Source: " + ride.getsource() + ", Destination: " + ride.getdestination() + ", Date: " + ride.getdate() + ", Available Seats: " + ride.getSeatsAvailable());
            }
        }
    }



    public static void cancelBooking() {
        ((Rider) currentUser).viewMyBookings();
        System.out.print("Enter ride ID to cancel: ");
        int rideIdToCancel = scanner.nextInt();
        scanner.nextLine(); 
        boolean success = UserDAO.canceLBooking(currentUser.getId(), rideIdToCancel);

        if (success) {
            System.out.println("Booking canceled successfully.");
        } else {
            System.out.println("Cancellation failed. No booking found or other issue.");
        }
    }

    private static void showAdminMenu() {
        System.out.println("Admin Menu");
        System.out.println("1. Add User");
        System.out.println("2. Remove User");
        System.out.println("3. View All Users");
        System.out.println("4. View Feedback");
        System.out.println("5. View All Feedback");
        System.out.println("6. Logout");
        int choice = scanner.nextInt();
        scanner.nextLine(); 

        switch (choice) {
            case 1:
                createUser(); 
                break;
            case 2:
                removeUser(); 
                break;
            case 3:
                ((Admin) currentUser).viewAllUsers();
                break;
            case 4:
                viewFeedback(); 
                break;
            case 5:
            	viewAllFeedback();
            	break;
            case 6:
                currentUser = null;
                System.out.println("Logged out successfully.");
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
    }

    private static void viewFeedback() {
        UserDAO userDAO = new UserDAO();
        userDAO.getFeedbackForRide();
    }

    private static void removeUser() {
        System.out.print("Enter user ID to remove: ");
        int userId = scanner.nextInt();
        scanner.nextLine(); 

        ((Admin) currentUser).removeUser(userId);
    }
}
