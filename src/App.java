import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.*;

public class App {

    int totalComputers;
    int availableComputers;
    LocalTime startTime;
    SystemQueue systemQueue;
    List<LocalTime> nextAvailableTime;

    int taskCounter = 0;
    int servedCounter = 0;
    int totalWaitingTime = 0;

    FileWriter myWriter = new FileWriter("logs.txt");
    App(int total) throws IOException {
        this.totalComputers = total;
        this.systemQueue = new SystemQueue();
        this.startTime = LocalTime.now();
        this.nextAvailableTime = new ArrayList<>();
        this.availableComputers = 0;
        for(int i=0; i < this.totalComputers; i++){
            this.nextAvailableTime.add(this.startTime);
        }
    }

    public boolean shouldReceiveTask(){
        return (new Random().nextInt(1, 5) == 1);
    }

    public long calcElapsedTimeInSecs(LocalTime time) {
        return Duration.between(time, LocalTime.now()).toSeconds();
    }

    public void checkIfSomeComputerBecomesAvailable(){
        LocalTime now = LocalTime.now();
        int currentAvailable = 0;
        for (LocalTime time : nextAvailableTime) {
            int compareResult = time.compareTo(LocalTime.now());
            if (compareResult <= 0) {
                currentAvailable += 1;
            }
        }
        availableComputers = currentAvailable;
    }

    public void assignTaskToAvailableComputer(Task task) {
        LocalTime now = LocalTime.now();
        for(int i=0; i < nextAvailableTime.size(); i++){
            int compareResult = nextAvailableTime.get(i).compareTo(now);
            if(compareResult <= 0){
                nextAvailableTime.set(i,
                        LocalTime.now().plusSeconds(task.serviceTime)
                );
                break;
            }
        }
    }

    public Task createRandomTask() {
        return new Task(new Random().nextInt(1, 10));
    }

    public void start() throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.print("How much time you want your servers to run ? (in secs): ");
        int seconds = sc.nextInt();

        while(calcElapsedTimeInSecs(startTime) < seconds) {
            Thread.sleep(300);
            checkIfSomeComputerBecomesAvailable();
            myWriter.write("Current available computers: " + availableComputers + "\n");

            while(systemQueue.hasNextTask() && availableComputers > 0){
                Task task = systemQueue.fetchNextTask();
                myWriter.write("Take task out of queue " + task + "\n");
                assignTaskToAvailableComputer(task);
                availableComputers--;
                taskCounter++;
                servedCounter++;
                totalWaitingTime += calcElapsedTimeInSecs(task.receivedAt);
            }

            if(shouldReceiveTask()){
                Task task = createRandomTask();
                taskCounter++;
                myWriter.write("Received Task No. " + taskCounter + ", " + task + "\n");
                if(availableComputers > 0){
                    myWriter.write("Serve Coming Task..... \n");
                    assignTaskToAvailableComputer(task);
                    availableComputers--;
                    servedCounter++;
                } else {
                    task.setReceivedAt(LocalTime.now());
                    systemQueue.addTask(task);
                    myWriter.write("Added Task to Queue \n");
                }
            }
            myWriter.write("Next Available Time: " + nextAvailableTime.toString() + "\n\n");
        }

        myWriter.close();

        System.out.println("\nServed Tasks: " + servedCounter);
        System.out.println("Average Waiting Time: " + calcAverageWaitingTime());
        System.out.println("Check logs.txt for more details.");
    }

    private double calcAverageWaitingTime() {
        return 1.0 * totalWaitingTime / servedCounter;
    }

    public static void main(String[] args) throws Exception {
        App app = new App(2);
        app.start();
    }

}
