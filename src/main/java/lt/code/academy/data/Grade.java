package lt.code.academy.data;

public class Grade {
    private int task_id;
    private int user_id;
    private int grade;

    public Grade() {
    }

    public Grade(int task_id, int user_id, int grade) {
        this.task_id = task_id;
        this.user_id = user_id;
        this.grade = grade;
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

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "task_id=" + task_id +
                ", user_id=" + user_id +
                ", grade=" + grade +
                '}';
    }
}
