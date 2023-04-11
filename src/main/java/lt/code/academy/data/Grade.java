package lt.code.academy.data;

public record Grade(int task_id, int user_id, int grade) {

    @Override
    public String toString() {
        return "Grade{" +
                "task_id=" + task_id +
                ", user_id=" + user_id +
                ", grade=" + grade +
                '}';
    }
}
