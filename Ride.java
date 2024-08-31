//package com.RideShare;

public class Ride {
    private int id;
    private int driver_id;
    private String start_location;
    private String end_location;
    private String date_time;
    private int seatsAvailable;
    
    public Ride(int id, int driverId, String source, String destination, String date,int seat) {
        this.id = id;
        this.driver_id = driverId;
        this.start_location = source;
        this.end_location = destination;
        this.date_time = date;
        this.seatsAvailable=seat;
    }

    public int getId() {
        return id;
    }

    public int getdriverId() {
        return driver_id;
    }

    public String getsource() {
        return start_location;
    }

    public String getdestination() {
        return end_location;
    }

    public String getdate() {
        return date_time;
    }

    public int getSeatsAvailable() {
       return seatsAvailable;
    }
}
