import java.sql.Timestamp;

public class Feedback {
    private int userId;
    private String userType;
    private int rideId;
    private String feedbackText;
    private Timestamp timestamp;

    // Constructor
    public Feedback(int userId, String userType, int rideId, String feedbackText, Timestamp timestamp) {
        this.userId = userId;
        this.userType = userType;
        this.rideId = rideId;
        this.feedbackText = feedbackText;
        this.timestamp = timestamp;
    }

    // Getters
    public int getUserId() {
        return userId;
    }

    public String getUserType() {
        return userType;
    }

    public int getRideId() {
        return rideId;
    }

    public String getFeedbackText() {
        return feedbackText;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    // Setters
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setRideId(int rideId) {
        this.rideId = rideId;
    }

    public void setFeedbackText(String feedbackText) {
        this.feedbackText = feedbackText;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "userId=" + userId +
                ", userType='" + userType + '\'' +
                ", rideId=" + rideId +
                ", feedbackText='" + feedbackText + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
