package lt.code.academy.data;

public record Question(int id, int task_id, int serial_number, String body) {

    @Override
    public String toString() {
        return serial_number + " - " + body;
    }
}
