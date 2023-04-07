package lt.code.academy.data;

import java.sql.Timestamp;

public class Exam {
    private int id;
    private int task_id;

    private int user_id;
    private int question_id;
    private int answer_id;
    private Timestamp date_time;

    public Exam() {
    }

    public Exam(int id, int task_id, int user_id, int question_id, int answer_id, Timestamp date_time) {
        this.id = id;
        this.task_id = task_id;
        this.user_id = user_id;
        this.question_id = question_id;
        this.answer_id = answer_id;
        this.date_time = date_time;
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

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public int getAnswer_id() {
        return answer_id;
    }

    public void setAnswer_id(int answer_id) {
        this.answer_id = answer_id;
    }

    public Timestamp getDate_time() {
        return date_time;
    }

    public void setDate_time(Timestamp date_time) {
        this.date_time = date_time;
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
