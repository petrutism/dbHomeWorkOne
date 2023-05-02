package lt.code.academy.tools;

import static lt.code.academy.tools.Print.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Clear {
    public static void clearTasks(Connection c) {
        String sql = "truncate table \"Task\", \"Question\", \"Answer\", \"Exam\", \"Grade\" restart identity;";
        PreparedStatement stmt;
        try {
            stmt = c.prepareStatement(sql);
            stmt.executeUpdate();
            stmt.close();
            pAlert("Tables Tasks, Questions, Answers, Exams and Grades successfully cleared...");
        } catch (SQLException e) {
            pError("Something is going wrong when doing big clean: " + e.getMessage());
        }
    }

    public static void clearGrades(Connection c) {
        String sql = "truncate table \"Grade\" restart identity;";
        PreparedStatement stmt;
        try {
            stmt = c.prepareStatement(sql);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Something is going wrong when clearing grades: " + e.getMessage());
        }
    }
}
