package lt.code.academy.tools;

import static lt.code.academy.tools.Print.*;

import lt.code.academy.data.Question;
import lt.code.academy.data.Reader;
import lt.code.academy.data.Task;
import lt.code.academy.data.User;

import java.sql.Connection;
import java.util.Scanner;

public class Utilities {
    public static String inputValidString(Scanner sc) {
        String text;
        do {
            System.out.print("Input data: ");
            text = sc.nextLine();
            text = text.trim();
            if (text.equals("")) {
                pError("\nData cannot be empty...");
            }
        } while (text.equals(""));
        return text;
    }

    public static boolean taskNameInUse(String name) {
        for (Task t : Reader.tasks) {
            if (t.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static int getTaskIdByName(String taskName) {
        int taskId = 0;
        for (Task t : Reader.tasks) {
            if (taskName.equals(t.getName())) {
                taskId = t.getId();
                break;
            }
        }
        return taskId;
    }

    public static void printAllTasks(Connection c) {
        Reader.readTasks(c);
        if (!Reader.tasks.isEmpty()) {
            System.out.println("We have this tasks in the list (number -> name): ");
            for (Task t : Reader.tasks) {
                System.out.println(t);
            }
        } else {
            pAlert("Task list is empty for now...");
        }
    }

    public static void printTaskQuestions(int task_id, Connection c) {
        Reader.readQuestions(c);
        for (Question q : Reader.questions) {
            if (task_id == q.task_id()) {
                System.out.println(q);
            }
        }
    }

    public static boolean questionTextIsUnique(String text, Connection c) {
        Reader.readQuestions(c);
        for (Question q : Reader.questions)
            if (text.equals(q.body())) {
                return false;
            }
        return true;
    }

    public static boolean answerIsValid(String answer) {
        return (answer.equals("a") || answer.equals("b") || answer.equals("c"));
    }

    public static void printAllUsers(Connection c) {
        Reader.readUsers(c);
        for (User u : Reader.users) {
            System.out.println(u);
        }
    }
    public static boolean userNameInUse(String userName) {
        for (User u : Reader.users) {
            if (u.getUserName().equals(userName)) {
                return true;
            }
        }

        return false;
    }

}
