package lt.code.academy.data;

import java.sql.Timestamp;

public class Exam {
    private final int id;
    private final int task_id;

    private final int user_id;
    private final int question_id;
    private final int answer_id;
    private final Timestamp date_time;

    public Exam(int id, int task_id, int user_id, int question_id, int answer_id, Timestamp date_time) {
        this.id = id;
        this.task_id = task_id;
        this.user_id = user_id;
        this.question_id = question_id;
        this.answer_id = answer_id;
        this.date_time = date_time;
    }

    public int getTask_id() {
        return task_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public Timestamp getDate_time() {
        return date_time;
    }

    @Override
    public String toString() {
        return "Exam{" +
                "id=" + id +
                ", task_id=" + task_id +
                ", user_id=" + user_id +
                ", question_id=" + question_id +
                ", answer_id=" + answer_id +
                ", date_time=" + date_time +
                '}';
    }
}
