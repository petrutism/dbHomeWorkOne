package lt.code.academy.data;

public class Question {
    private int id;
    private int task_id;
    private int serial_number;
    private String body;

    public Question() {
    }

    public Question(int id, int task_id, int serial_number, String body) {
        this.id = id;
        this.task_id = task_id;
        this.serial_number = serial_number;
        this.body = body;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public int getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(int serial_number) {
        this.serial_number = serial_number;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return serial_number + " - " + body;
    }
}
