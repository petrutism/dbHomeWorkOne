package lt.code.academy.tools;

import lt.code.academy.data.Reader;
import static lt.code.academy.tools.Print.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Stats {
    public static void abcTotal(Connection connection) {
        String sql = "select COUNT(\"ANSWER_ID\") from \"Exam\" as e join \"Answer\" as a on a.\"ID\" = e.\"ANSWER_ID\" where a.\"VARIANT\" = ?;";
        List<String> variant = List.of("a", "b", "c");
        PreparedStatement stmt;

        try {
            for (int i = 0; i < 3; i++) {
                stmt = connection.prepareStatement(sql);
                stmt.setString(1, variant.get(i));
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    pSuccess(variant.get(i) + ": " + rs.getInt("count"));
                }
            }
        } catch (SQLException e) {
            pError("Something went wrong when getting statistics: " + e.getMessage());
        }
    }

    public static void count(Connection c, String sql) {
        int task_id;
        String name;
        int count;
        Reader.readTasks(c);
        Reader.readExams(c);
        PreparedStatement stmt;

        try {
            stmt = c.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                task_id = rs.getInt("TASK_ID");
                name = rs.getString("NAME");
                count = rs.getInt("count");
                pSuccess(task_id + " " + name + " " + count);
            }
            stmt.close();
        } catch (SQLException e) {
            pError("Something's get wrong when getting statistic: " + e.getMessage());
        }
    }
    public static void avgCorrect(Connection c) {
        double avg;
        Reader.readAnswers(c);
        Reader.readExams(c);
        String sql = "select avg(noc.\"correct\") from (select e.\"USER_ID\", e.\"TASK_ID\", COUNT(a.\"IS_CORRECT\") as \"correct\" from \"Exam\" e join \"Answer\" a on e.\"ANSWER_ID\" = a.\"ID\" where a.\"IS_CORRECT\" = true group by e.\"USER_ID\", e.\"TASK_ID\" order by e.\"USER_ID\", e.\"TASK_ID\") as noc;";

        PreparedStatement stmt;

        try {
            stmt = c.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                avg = rs.getDouble("avg");
                pSuccess("Average number of correct answers is: " + avg);
            }
            stmt.close();
        } catch (SQLException e) {
            pError("Something's get wrong when getting statistic: " + e.getMessage());
        }
    }
}
