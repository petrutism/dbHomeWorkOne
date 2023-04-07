package lt.code.academy.data;

import org.apache.commons.codec.digest.DigestUtils;

import static lt.code.academy.tools.Print.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Writer {
    public static void writeUser(String userName, String name, String surName, String password, boolean is_admin, Connection c) {
        String sql = "insert into \"User\" (\"USERNAME\", \"NAME\", \"SURNAME\", \"PASSWORD\", \"IS_ADMIN\") VALUES (?, ?, ?, ?, ?);";
        PreparedStatement stmt;
        try {
            stmt = c.prepareStatement(sql);
            stmt.setString(1, userName);
            stmt.setString(2, name);
            stmt.setString(3, surName);
            stmt.setString(4, DigestUtils.sha256Hex(password));
            stmt.setBoolean(5, is_admin);
            stmt.executeUpdate();
            stmt.close();
            pSuccess("New user successfully added to database...");
        } catch (SQLException e) {
            pError("Something's get wrong when writing user: " + e.getMessage());
        }
        Reader.users.clear();
        Reader.readUsers(c);
    }

    public static void writeTask(String name, Connection c) {
        String sql = "insert into \"Task\" (\"NAME\") VALUES (?);";
        PreparedStatement stmt;
        try {
            stmt = c.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.executeUpdate();
            stmt.close();
            pSuccess("New task successfully added to database...");
        } catch (SQLException e) {
            pError("Something's get wrong when writing task: " + e.getMessage());
        }
        Reader.tasks.clear();
        Reader.readTasks(c);
    }

    public static void writeQuestion(int task_id, int serial_number, String body, Connection c) {
        String sql = "insert into \"Question\" (\"TASK_ID\", \"SERIAL_NUMBER\", \"BODY\") VALUES (?, ?, ?);";
        PreparedStatement stmt;
        try {
            stmt = c.prepareStatement(sql);
            stmt.setInt(1, task_id);
            stmt.setInt(2, serial_number);
            stmt.setString(3, body);
            stmt.executeUpdate();
            stmt.close();
            pSuccess("New question successfully added to database...");
        } catch (SQLException e) {
            pError("Something's get wrong when writing question: " + e.getMessage());
        }
    }

    public static void modifyQuestion(int task_id, int serial_number, String text, Connection c) {
        String sql = "update \"Question\" set \"BODY\" = ? where \"TASK_ID\"= ? and \"SERIAL_NUMBER\"= ?;";
        PreparedStatement stmt;
        try {
            stmt = c.prepareStatement(sql);
            stmt.setString(1, text);
            stmt.setInt(2, task_id);
            stmt.setInt(3, serial_number);
            stmt.executeUpdate();
            stmt.close();
            pSuccess("Question successfully updated...");
        } catch (SQLException e) {
            pError("Something's get wrong when updating question: " + e.getMessage());
        }
    }

    public static void writeAnswer(int question_id, String body, String variant, boolean is_correct, Connection c) {
        String sql = "insert into \"Answer\" (\"QUESTION_ID\", \"BODY\", \"VARIANT\", \"IS_CORRECT\") VALUES (?, ?, ?, ?);";
        PreparedStatement stmt;
        try {
            stmt = c.prepareStatement(sql);
            stmt.setInt(1, question_id);
            stmt.setString(2, body);
            stmt.setString(3, variant);
            stmt.setBoolean(4, is_correct);
            stmt.executeUpdate();
            stmt.close();
            pSuccess("New answer successfully added to database...");
        } catch (SQLException e) {
            pError("Something's get wrong when writing question: " + e.getMessage());
        }
    }

    public static void writeExam(int task_id, int user_id, int question_id, int answer_id, Timestamp timestamp, Connection c) {
        String sql = "insert into \"Exam\" (\"TASK_ID\", \"USER_ID\", \"QUESTION_ID\", \"ANSWER_ID\", \"DATE_TIME\") VALUES (?, ?, ?, ?, ?);";
        PreparedStatement stmt;
        try {
            stmt = c.prepareStatement(sql);
            stmt.setInt(1, task_id);
            stmt.setInt(2, user_id);
            stmt.setInt(3, question_id);
            stmt.setInt(4, answer_id);
            stmt.setTimestamp(5, timestamp);
            stmt.executeUpdate();
            stmt.close();
            pSuccess("New exam line successfully added to database...");
        } catch (SQLException e) {
            pError("Something's get wrong when writing exam line: " + e.getMessage());
        }
    }
}
