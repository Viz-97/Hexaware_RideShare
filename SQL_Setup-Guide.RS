
### How to Use

## Database Setup

Follow the instructions below to set up the SQL database for the Hexapool application:

### Step 1: Create the Database

Run the following command to create the database and insert initial data:

```bash
mysql -u yourusername -p < path/to/database_setup.sql


-- Create the database
CREATE DATABASE IF NOT EXISTS hexapool;


-- Switch to the hexapool database
USE hexapool;


-- Create Users table
CREATE TABLE IF NOT EXISTS users (
    userId INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    userType ENUM('DRIVER', 'RIDER', 'ADMIN') NOT NULL
);


-- Create Rides table
CREATE TABLE IF NOT EXISTS rides (
    rideId INT AUTO_INCREMENT PRIMARY KEY,
    driverId INT,
    destination VARCHAR(255) NOT NULL,
    availableSeats INT NOT NULL,
    FOREIGN KEY (driverId) REFERENCES users(userId)
);


-- Create Booking table
CREATE TABLE IF NOT EXISTS booking (
    bookingId INT AUTO_INCREMENT PRIMARY KEY,
    rideId INT,
    riderId INT,
    bookingDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (rideId) REFERENCES rides(rideId),
    FOREIGN KEY (riderId) REFERENCES users(userId)
);


-- Create Feedback table
CREATE TABLE IF NOT EXISTS feedback (
    feedbackId INT AUTO_INCREMENT PRIMARY KEY,
    userId INT,
    userType ENUM('DRIVER', 'RIDER') NOT NULL,
    rideId INT,
    feedbackText TEXT NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (userId) REFERENCES users(userId),
    FOREIGN KEY (rideId) REFERENCES rides(rideId)
);


-- Insert initial users
INSERT INTO users (name, email, password, userType) VALUES 
('Admin User', 'admin@example.com', 'adminpass', 'ADMIN'),
('John Doe', 'johndoe@example.com', 'johnpass', 'DRIVER'),
('Jane Smith', 'janesmith@example.com', 'janepass', 'RIDER');



This README file provides an overview of the project, instructions for installation, usage guidelines, and contribution details.


This setup guide provides a simple, minimal configuration for your SQL database, ensuring that the initial users, rides, bookings, and feedback are set up for your application's basic operation.
