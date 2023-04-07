package lt.code.academy.data;
import static lt.code.academy.tools.Print.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Reader {
    public final static List<User> users = new ArrayList<>();
    public final static List<Task> tasks = new ArrayList<>();
    public final static List<Question> questions = new ArrayList<>();
    public final static List<Answer> answers = new ArrayList<>();
    public final static List<Exam> exams = new ArrayList<>();
        public final static List<Grade> grades = new ArrayList<>();

    public static void readQuestions(Connection c) {
        questions.clear();
        int id;
        int task_id;
        int serial_number;
        String body;

        String sql = "select * FROM \"Question\" order by \"SERIAL_NUMBER\";";
        PreparedStatement stmt;
        try {
            stmt = c.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                id = rs.getInt("ID");
                task_id = rs.getInt("TASK_ID");
                serial_number = rs.getInt("SERIAL_NUMBER");
                body = rs.getString("BODY");
                questions.add(new Question(id, task_id, serial_number, body));
            }
            stmt.close();
        } catch (SQLException e) {
            pError("Something's get wrong when reading questions: " + e.getMessage());
        }
    }
    public static void readAnswers(Connection c){
        answers.clear();
        int id;
        int question_id;
        String body;
        String variant;
        boolean is_correct;
        String sql = "select * FROM \"Answer\";";
        PreparedStatement stmt;
        try {
            stmt = c.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                id = rs.getInt("ID");
                question_id = rs.getInt("QUESTION_ID");
                body = rs.getString("BODY");
                variant = rs.getString("VARIANT");
                is_correct = rs.getBoolean("IS_CORRECT");
                answers.add(new Answer(id, question_id, body, variant, is_correct));
            }
            stmt.close();
        } catch (SQLException e) {
            pError("Something's get wrong when reading answers: " + e.getMessage());
        }
    }

    public static void readTasks(Connection c) {
        tasks.clear();
        int id;
        String name;
        String sql = "select * FROM \"Task\";";
        PreparedStatement stmt;
        try {
            stmt = c.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                id = rs.getInt("ID");
                name = rs.getString("NAME");
                tasks.add(new Task(id, name));
            }
            stmt.close();
        } catch (SQLException e) {
            pError("Something's get wrong when reading tasks: " + e.getMessage());
        }
    }

    public static void readUsers(Connection c) {
        users.clear();
        int id;
        String userName;
        String name;
        String surName;
        String password;
        boolean is_admin;
        String sql = "select * FROM \"User\";";
        PreparedStatement stmt;

        try {
            stmt = c.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                id = rs.getInt("ID");
                userName = rs.getString("USERNAME");
                name = rs.getString("NAME");
                surName = rs.getString("SURNAME");
                password = rs.getString("PASSWORD");
                is_admin = rs.getBoolean("IS_ADMIN");
                users.add(new User(id, userName, name, surName, password, is_admin));
            }
            stmt.close();
        } catch (SQLException e) {
            pError("Something's get wrong when reading users: " + e.getMessage());
        }
    }
    public static void readExams(Connection c){
        exams.clear();
        int id;
        int task_id;
        int user_id;
        int question_id;
        int answer_id;
        Timestamp timestamp;
        String sql = "select * FROM \"Exam\";";
        PreparedStatement stmt;
        try {
            stmt = c.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                id = rs.getInt("ID");
                task_id = rs.getInt("TASK_ID");
                user_id = rs.getInt("USER_ID");
                question_id = rs.getInt("QUESTION_ID");
                answer_id = rs.getInt("ANSWER_ID");
                timestamp = rs.getTimestamp("DATE_TIME");

               exams.add(new Exam(id, task_id, user_id, question_id, answer_id, timestamp));
            }
            stmt.close();
        } catch (SQLException e) {
            pError("Something's get wrong when reading users: " + e.getMessage());
        }
    }
    public static void readGrades(Connection c) {
        grades.clear();
        int task_id;
        int user_id;
        int grade;
        String sql = "select * FROM \"Grade\";";
        PreparedStatement stmt;
        try {
            stmt = c.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                task_id = rs.getInt("TASK_ID");
                user_id = rs.getInt("USER_ID");
                grade = rs.getInt("GRADE");
                grades.add(new Grade(task_id, user_id, grade));
            }
            stmt.close();
        } catch (SQLException e) {
            pError("Something's get wrong when reading grades: " + e.getMessage());
        }
    }

}
