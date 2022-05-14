import java.util.LinkedList;
import java.util.Queue;

public class SystemQueue {

    Queue<Task> queue;

    SystemQueue() {
        this.queue = new LinkedList<Task>();
    }

    void addTask(Task task){
        this.queue.add(task);
    }

    boolean hasNextTask() {
        return !(this.queue.isEmpty());
    }

    Task fetchNextTask() throws Exception{
        if(hasNextTask()) return this.queue.remove();
        throw new Exception("there is no more tasks");
    }

}
