package lt.code.academy.tools;

import static lt.code.academy.tools.Print.*;

import com.github.javafaker.Faker;
import lt.code.academy.data.*;
import org.apache.commons.codec.digest.DigestUtils;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class Fill {
    private static final int NUMBER_OF_FAKE_USERS = 20;
    private static final int NUMBER_OF_FAKE_TASKS = 10;
    private static final int NUMBER_OF_FAKE_QUESTIONS = 20;
    private static final Faker faker = new Faker();

    public static void writeTasks(Connection c) {
        Reader.readTasks(c);
        String sql = "insert into \"Task\" (\"NAME\") VALUES (?);";
        PreparedStatement stmt;
        String taskName;
        List<String> taskNames = new ArrayList<>();
        for (Task t : Reader.tasks) {
            taskNames.add(t.getName());
        }

        try {
            stmt = c.prepareStatement(sql);
            c.setAutoCommit(false);
            for (int i = taskNames.size() + 1; i < NUMBER_OF_FAKE_TASKS + 1; i++) {
                do {
                    taskName = String.valueOf(faker.programmingLanguage().name());
                } while (taskNames.contains(taskName));
                taskNames.add(taskName);
                stmt.setString(1, taskName);
                stmt.addBatch();
            }
            stmt.executeBatch();
            c.commit();
            c.setAutoCommit(true);
            stmt.close();
            writeQuestions(c);
            pSuccess("Tasks, questions and answers successfully created to full table...");
        } catch (SQLException e) {
            pError("Something is going wrong when writing task names: " + e.getMessage());
        }
    }

    private static void writeQuestions(Connection c) {
        List<Question> taskQuestions = new ArrayList<>();
        Reader.readTasks(c);
        Reader.readQuestions(c);

        String sql = "insert into \"Question\" (\"TASK_ID\", \"SERIAL_NUMBER\", \"BODY\") VALUES (?, ?, ?);";
        PreparedStatement stmt;
        List<String> chuck = new ArrayList<>();
        String ch;

        try {
            stmt = c.prepareStatement(sql);
            c.setAutoCommit(false);

            for (int i = 1; i < Reader.tasks.size() + 1; i++) {
                for (Question q : Reader.questions) {
                    if (i == q.task_id()) {
                        taskQuestions.add(q);
                    }
                }
                for (int j = taskQuestions.size() + 1; j < NUMBER_OF_FAKE_QUESTIONS + 1; j++) {
                    stmt.setInt(1, i);
                    stmt.setInt(2, j);
                    do {
                        ch = faker.chuckNorris().fact() + ". Is it true?";
                        ch = ch.replace("Chuck Norris", faker.name().firstName() + " " + faker.name().lastName());
                    } while (chuck.contains(ch));
                    chuck.add(ch);
                    stmt.setString(3, ch);
                    stmt.addBatch();
                }
                taskQuestions.clear();
            }
            stmt.executeBatch();
            c.commit();
            c.setAutoCommit(true);
            stmt.close();
            chuck.clear();
            writeAnswers(c);
        } catch (SQLException e) {
            pError("Something is going wrong when writing questions: " + e.getMessage());
        }
    }

    private static void writeAnswers(Connection c) {
        Reader.readQuestions(c);
        Reader.readAnswers(c);
        List<Answer> questionAnswers = new ArrayList<>();
        String sql = "insert into \"Answer\" (\"QUESTION_ID\", \"BODY\", \"VARIANT\", \"IS_CORRECT\") VALUES (?, ?, ?, ?);";

        PreparedStatement stmt;

        try {
            stmt = c.prepareStatement(sql);
            c.setAutoCommit(false);

            for (int i = 1; i < Reader.questions.size() + 1; i++) {

                for (Answer a : Reader.answers) {
                    if (i == a.getQuestion_id()) {
                        questionAnswers.add(a);
                    }
                }
                if (questionAnswers.size() != 3) {
                    int corr = faker.number().numberBetween(1, 4);
                    for (int j = questionAnswers.size() + 1; j < 4; j++) {

                        boolean isTrue = false;
                        if (j == 1) {
                            if (corr == j) {
                                isTrue = true;
                            }
                            stmt.setInt(1, i);
                            stmt.setString(2, "Yes, " + faker.ancient().hero());
                            stmt.setString(3, "a");
                            stmt.setBoolean(4, isTrue);
                            stmt.addBatch();
                        }
                        if (j == 2) {
                            if (corr == j) {
                                isTrue = true;
                            }
                            stmt.setInt(1, i);
                            stmt.setString(2, "No, " + faker.ancient().hero());
                            stmt.setString(3, "b");
                            stmt.setBoolean(4, isTrue);
                            stmt.addBatch();
                        }
                        if (j == 3) {
                            if (corr == j) {
                                isTrue = true;
                            }
                            stmt.setInt(1, i);
                            stmt.setString(2, "I do not know...");
                            stmt.setString(3, "c");
                            stmt.setBoolean(4, isTrue);
                            stmt.addBatch();
                        }
                    }
                } else {
                    questionAnswers.clear();
                }
            }
            stmt.executeBatch();

            c.commit();
            c.setAutoCommit(true);
            stmt.close();
        } catch (SQLException e) {
            pError("Something is going wrong when writing answers: " + e.getMessage());
        }
    }

    public static void writeExam(Connection c) {
        String sql = "insert into \"Exam\" (\"TASK_ID\", \"USER_ID\", \"QUESTION_ID\", \"ANSWER_ID\", \"DATE_TIME\") VALUES (?, ?, ?, ?, ?);";
        String questionsSQL = "select q.\"ID\" from \"Question\" as q join \"Task\" as t on q.\"TASK_ID\"=t.\"ID\" where q.\"TASK_ID\" = ?;";
        String answersSQL = "select a.\"ID\" from \"Answer\" as a join \"Question\" as q on a.\"QUESTION_ID\" = q.\"ID\" where a.\"QUESTION_ID\" = ?;";
        PreparedStatement stmt;
        PreparedStatement questionsStatement;
        PreparedStatement answersStatement;
        ResultSet questionsRs;
        ResultSet answersRs;
        LocalDateTime ldt = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(ldt);
        boolean alreadyDone = false;
        boolean isAdmin = false;
        List<Integer> answerIDs = new ArrayList<>();

        Reader.readExams(c);
        Reader.readUsers(c);

        try {
            stmt = c.prepareStatement(sql);
            c.setAutoCommit(false);
            for (int task = 0; task < Reader.tasks.size(); task++) {
                int t = Reader.tasks.get(task).getId();
                for (int user = 0; user < Reader.users.size(); user++) {
                    int u = Reader.users.get(user).getId();
                    for (Exam e : Reader.exams) {
                        if (t == e.getTask_id() && u == e.getUser_id()) {
                            alreadyDone = true;
                            break;
                        }
                    }

                    for (User usr : Reader.users) {
                        if (usr.getId() == u && usr.isIs_admin()) {
                            isAdmin = true;
                            break;
                        }
                    }

                    if (!alreadyDone && !isAdmin) {
                        questionsStatement = c.prepareStatement(questionsSQL);
                        questionsStatement.setInt(1, t);
                        questionsRs = questionsStatement.executeQuery();
                        while (questionsRs.next()) {

                            answersStatement = c.prepareStatement(answersSQL);
                            answersStatement.setInt(1, questionsRs.getInt("ID"));
                            answersRs = answersStatement.executeQuery();
                            while (answersRs.next()) {
                                answerIDs.add(answersRs.getInt("ID"));
                            }
                            stmt.setInt(1, t);
                            stmt.setInt(2, u);
                            stmt.setInt(3, questionsRs.getInt("ID"));
                            stmt.setInt(4, answerIDs.get(faker.number().numberBetween(0, 3)));
                            stmt.setTimestamp(5, timestamp);
                            stmt.addBatch();
                            answersStatement.close();
                            answerIDs.clear();
                        }
                        questionsStatement.close();
                    }
                    answerIDs.clear();
                    alreadyDone = false;
                    isAdmin = false;
                }
            }
            stmt.executeBatch();

            c.commit();
            c.setAutoCommit(true);
            stmt.close();
            pSuccess("Exams successfully created.");
        } catch (SQLException e) {
            pError("Something is going wrong when writing exam lines: " + e.getMessage());
        }
    }

    public static void setGrade(Connection c) {
        Clear.clearGrades(c);
        String sqlSelect = "select e.\"USER_ID\", e.\"TASK_ID\", COUNT(a.\"IS_CORRECT\")/2 as \"GRADE\"" +
                "from \"Exam\" e join \"Answer\" a on e.\"ANSWER_ID\" = a.\"ID\" where a.\"IS_CORRECT\" = true" +
                " group by e.\"USER_ID\", e.\"TASK_ID\" order by e.\"USER_ID\", e.\"TASK_ID\";";
        String sqlInsert = "insert into \"Grade\" (\"TASK_ID\", \"USER_ID\", \"GRADE\") VALUES (?, ?, ?);";
        PreparedStatement stmt;
        PreparedStatement gradeStmt;

        ResultSet gradeRs;

        try {
            stmt = c.prepareStatement(sqlSelect);
            gradeStmt = c.prepareStatement(sqlInsert);
            gradeRs = stmt.executeQuery();
            c.setAutoCommit(false);
            while (gradeRs.next()) {
                gradeStmt.setInt(1, gradeRs.getInt("TASK_ID"));
                gradeStmt.setInt(2, gradeRs.getInt("USER_ID"));
                gradeStmt.setInt(3, gradeRs.getInt("GRADE"));
                gradeStmt.addBatch();
            }
            gradeStmt.executeBatch();

            c.commit();
            c.setAutoCommit(true);
            stmt.close();
            gradeStmt.close();
            pSuccess("Grades successfully set.");
        } catch (SQLException e) {
            pError("Something is going wrong when writing grades: " + e.getMessage());
        }
    }

    public static void writeUsers(Connection c) {
        Reader.readUsers(c);
        String sql = "insert into \"User\" (\"USERNAME\", \"NAME\", \"SURNAME\", \"PASSWORD\", \"IS_ADMIN\") VALUES (?, ?, ?, ?, ?);";
        PreparedStatement stmt;

        Set<String> usernames = new HashSet<>();
        String username;

        try {
            stmt = c.prepareStatement(sql);
            c.setAutoCommit(false);
            for (int i = Reader.users.size(); i < NUMBER_OF_FAKE_USERS; i++) {
                do {
                    username = String.valueOf(faker.ancient().hero());

                } while (usernames.contains(username));
                usernames.add(username);
                stmt.setString(1, username);
                stmt.setString(2, faker.name().firstName());
                stmt.setString(3, faker.name().lastName());
                stmt.setString(4, DigestUtils.sha256Hex("123123"));
                stmt.setBoolean(5, false);
                stmt.addBatch();
            }
            stmt.executeBatch();

            c.commit();
            c.setAutoCommit(true);
            stmt.close();
            usernames.clear();
            pSuccess("Users table filled up to " + NUMBER_OF_FAKE_USERS + " users.");
        } catch (SQLException e) {
            pError("Something is going wrong when writing users: " + e.getMessage());
        }
    }
}
