import java.time.LocalTime;

public class Task {

    int serviceTime;
    LocalTime receivedAt;

    Task(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    public void setReceivedAt(LocalTime receivedAt) {
        this.receivedAt = receivedAt;
    }

    public String toString() {
        return "Task Service Time: " + this.serviceTime;
    }

}
