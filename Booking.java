//package com.RideShare;

public class Booking {
    private int id;
    private int userId;
    private int rideId;
    private String status;

    public Booking(int id, int userId, int rideId, String status) {
        this.id = id;
        this.userId = userId;
        this.rideId = rideId;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getRideId() {
        return rideId;
    }

    public String getStatus() {
        return status;
    }
}
