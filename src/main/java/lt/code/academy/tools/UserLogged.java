package lt.code.academy.tools;

import lt.code.academy.data.*;

import static lt.code.academy.tools.Print.*;

import java.sql.Connection;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserLogged {
    UserLogged() {
    }
    int logged_user_id;

    public void userAction(Scanner sc, Connection c, int user_id) {
        logged_user_id = user_id;
        String action;
        do {
            userMenu();
            action = sc.nextLine();
            switch (action) {
                case "1" -> doExam(sc, c);
                case "2" -> viewGradesByUser(c);
                case "3" -> pSuccess("Logged out from user tools...");
                default -> pError("There is no such action...");
            }
        } while (!action.equals("3"));
    }

    private void doExam(Scanner sc, Connection c) {
        List<Answer> questionAnswers = new ArrayList<>();
        String taskName;
        int taskId;
        int question_id;
        int answer_id = 0;
        String variant;
        LocalDateTime ldt;
        Timestamp timestamp;

        Reader.readExams(c);
        Utilities.printAllTasks(c);

        if(Reader.tasks.isEmpty()){
            return;
        }

        do {
            System.out.println("Enter name of task to process exam:");
            taskName = Utilities.inputValidString(sc);
        } while (!Utilities.taskNameInUse(taskName));
        taskId = Utilities.getTaskIdByName(taskName);
        for (Exam e : Reader.exams) {
            if (taskId == e.getTask_id() && logged_user_id == e.getUser_id()) {
                pError("Exam was proceed by you at " + e.getDate_time());
                return;
            }
        }
        Reader.readQuestions(c);
        Reader.readAnswers(c);
        for (Question q : Reader.questions) {
            if (taskId == q.task_id()) {
                question_id = q.id();
                System.out.println(q);
                for (Answer a : Reader.answers) {
                    if (question_id == a.getQuestion_id()) {
                        System.out.println(a);
                        questionAnswers.add(a);
                    }
                }
                do {
                    System.out.println("Please choose your answer.");
                    variant = Utilities.inputValidString(sc);
                } while (!Utilities.answerIsValid(variant));
                for (Answer a : questionAnswers) {
                    if (variant.equals(a.getVariant())) {
                        answer_id = a.getId();
                    }
                }

                ldt = LocalDateTime.now();
                timestamp = Timestamp.valueOf(ldt);
                Writer.writeExam(taskId, logged_user_id, question_id, answer_id, timestamp, c);
                questionAnswers.clear();
            }
        }
    }

    private void viewGradesByUser(Connection c) {
        String taskName;
        boolean noGrades = true;
        Reader.readGrades(c);
        Reader.readTasks(c);

        for (Grade g : Reader.grades) {
            if (logged_user_id == g.user_id()) {
                for (Task t : Reader.tasks) {
                    if (g.task_id() == t.getId()) {
                        taskName = t.getName();
                        System.out.println(taskName + " -> " + g.grade());
                        noGrades = false;
                    }
                }
            }
        }
        if (noGrades) {
            pError("You have no grades yet...");
        }

    }

    private void userMenu() {
        String text = """
                1 -> Process exam
                2 -> View my grades
                3 -> Logout user
                """;
        System.out.println(text);
    }
}
