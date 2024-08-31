import java.util.*;
public class Admin extends User {

    public Admin(int id, String name, String email, String password) {
        super(id, name, email, password, "ADMIN");
    }

    public boolean removeUser(int userId) {
        UserDAO userDAO = new UserDAO();
        return userDAO.removeUser(userId);
    }

    public void viewAllUsers() {
        UserDAO userDAO = new UserDAO();
        List<User> users = userDAO.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("No users found.");
        } else {
            for (User user : users) {
                System.out.println("User ID: " + user.getId() + ", Name: " + user.getName() + ", Email: " + user.getEmail() + ", Role: " + user.getRole());
            }
        }
    }

    public void viewRideHistory() {
        UserDAO userDAO = new UserDAO();
        List<Ride> rides = userDAO.getAllRides();
        if (rides.isEmpty()) {
            System.out.println("No ride history found.");
        } else {
            for (Ride ride : rides) {
                System.out.println("Ride ID: " + ride.getId() + ", Source: " + ride.getsource() + ", Destination: " + ride.getdestination() + ", Date: " + ride.getdate() + ", Available Seats: " + ride.getSeatsAvailable());
            }
        }
    }
}
