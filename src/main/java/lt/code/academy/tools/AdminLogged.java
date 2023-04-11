package lt.code.academy.tools;

import jdk.jshell.execution.Util;
import lt.code.academy.data.*;

import static lt.code.academy.tools.Print.*;

import java.sql.Connection;
import java.util.Scanner;

class AdminLogged {
    AdminLogged() {
    }

    public void adminAction(Scanner sc, Connection c) {
        String action;
        do {
            adminMenu();
            action = sc.nextLine();
            switch (action) {
                case "1" -> createTask(sc, c);
                case "2" -> createQuestion(sc, c);
                case "3" -> modifyTask(sc, c);
                case "4" -> Fill.writeUsers(c);
                case "5" -> deleteUser(sc, c);
                case "6" -> Fill.writeTasks(c);
                case "7" -> Fill.writeExam(c);
                case "8" -> Fill.setGrade(c);
                case "9" -> Clear.clearTasks(c);
                case "10" -> Stats.abcTotal(c);
                case "11" -> {
                    String sql = "select \"TASK_ID\", \"NAME\", COUNT(\"TASK_ID\") from (select distinct e.\"USER_ID\", e.\"TASK_ID\", t.\"NAME\" from \"Exam\" e join \"Task\" t on e.\"TASK_ID\" = t.\"ID\" group by \"TASK_ID\", \"USER_ID\", \"NAME\" order by \"TASK_ID\") as nt group by \"TASK_ID\", \"NAME\";";
                    Stats.count(c, sql);
                }
                case "12" -> {
                    String sql = "select t.\"ID\" as \"TASK_ID\", t.\"NAME\" , COUNT(a.\"IS_CORRECT\") from (\"Exam\" e join \"Answer\" a on e.\"ANSWER_ID\" = a.\"ID\") join \"Task\" t on e.\"TASK_ID\" = t.\"ID\"  where a.\"IS_CORRECT\" = true group by t.\"ID\" , t.\"NAME\" order by t.\"ID\";";
                    Stats.count(c, sql);
                }
                case "13" -> Stats.avgCorrect(c);
                case "X" -> pSuccess("Logged out from administrative tools...");
                default -> pError("There is no such action...");
            }
        } while (!action.equals("X"));
    }

    private void modifyTask(Scanner sc, Connection c) {
        int modifyTaskId;
        Utilities.printAllTasks(c);
        System.out.println("Enter task name to modify.");
        String modifyName = Utilities.inputValidString(sc);
        if (Utilities.taskNameInUse(modifyName)) {
            modifyTaskId = Utilities.getTaskIdByName(modifyName);

            System.out.println("Task " + modifyName + " haves questions:");
            Utilities.printTaskQuestions(modifyTaskId, c);

            System.out.println("Enter question number to modify.");
            int modifySerialNumber = Integer.parseInt(Utilities.inputValidString(sc));

            System.out.println("Input new question text.");
            String newText = Utilities.inputValidString(sc);

            if(Utilities.questionTextIsUnique(newText, c)) {
                Writer.modifyQuestion(modifyTaskId, modifySerialNumber, newText, c);
            } else {
                pError("We already have this question...");
            }

        } else {
            pError("You can modify only existing task...");
        }
    }

    private void createTask(Scanner sc, Connection c) {
        Utilities.printAllTasks(c);
        String taskName;
        boolean taskNameIsInUse = false;
        Utilities.printAllTasks(c);

        if (Reader.tasks.size() >= 10) {
            pError("You already have 10 tasks...");
            return;
        }
        do {
            if (taskNameIsInUse) {
                pError("Task name is already used...");
            }
            System.out.println("\nEnter name for new task.");
            taskName = Utilities.inputValidString(sc);
            taskNameIsInUse = Utilities.taskNameInUse(taskName);
        } while (taskNameIsInUse);
        Writer.writeTask(taskName, c);
    }

    private void createQuestion(Scanner sc, Connection c) {
        int taskId;
        String taskName;
        int numberOfQuestions = 0;
        int newSerialNumber;
        String newQuestionBody;
        int questionId = 0;

        Utilities.printAllTasks(c);

        if (Reader.tasks.isEmpty()) {
            pError("You cannot create questions for non existing tasks...");
            return;
        }

        System.out.println("Input task name to create questions for.");
        taskName = Utilities.inputValidString(sc);

        if (!Utilities.taskNameInUse(taskName)) {
            pError("Task name is not valid... Go think about your behavior...");
            return;
        }

        taskId = Utilities.getTaskIdByName(taskName);

        Reader.readQuestions(c);

        for (Question q : Reader.questions) {
            if (taskId == q.task_id()) {
                numberOfQuestions++;
            }
        }

        if (numberOfQuestions < 20) {
            newSerialNumber = numberOfQuestions + 1;
            System.out.println("Task " + taskName + " haves " + numberOfQuestions + " questions now.");

            Utilities.printTaskQuestions(taskId, c);

            System.out.println("Please add question number " + newSerialNumber + " text:");
            newQuestionBody = Utilities.inputValidString(sc);

            if (Utilities.questionTextIsUnique(newQuestionBody, c)) {
                Writer.writeQuestion(taskId, newSerialNumber, newQuestionBody, c);

                Reader.readQuestions(c);

                for (Question q : Reader.questions) {
                    if (taskId == q.task_id() && newSerialNumber == q.serial_number()) {
                        questionId = q.id();
                        break;
                    }
                }
                createAnswers(questionId, sc, c);

                pSuccess("Question and 3 answers was successfully created...");
            } else {
                System.out.println("Question must be unique...");
            }
        } else {
            pError("Task already have 20 questions, this is max number...");
        }
    }

    private void createAnswers(int question_id, Scanner sc, Connection c) {
        boolean correctIsSet = false;
        boolean is_correct = false;
        String answer;
        String isCorrect;
        String variants = "abc";
        String variant;
        for (int i = 0; i < 3; i++) {
            variant = String.valueOf(variants.charAt(i));
            System.out.println("Please input answer for variant " + variant + ".");
            answer = Utilities.inputValidString(sc);
            if (!correctIsSet) {
                System.out.println("Input Y if answer is correct, N if it is not.");
                isCorrect = Utilities.inputValidString(sc);
                if (isCorrect.equalsIgnoreCase("Y")) {
                    is_correct = true;
                    correctIsSet = true;
                }
            }
            if (i == 2 && !correctIsSet) {
                is_correct = true;
            }
            Writer.writeAnswer(question_id, answer, variant, is_correct, c);
            is_correct = false;
        }
        pSuccess("Answers successfully created...");
        Reader.readAnswers(c);
    }

    private void deleteUser(Scanner sc, Connection c) {
        Utilities.printAllUsers(c);
        System.out.println("Input username to delete.");
        String usernameToDelete = Utilities.inputValidString(sc);

        if (Utilities.userNameInUse(usernameToDelete)) {
            Writer.userDeletion(usernameToDelete, c);
        } else {
            pError("There is no such username...");
        }
    }



    private void adminMenu() {
        System.out.println("1 -> Create new task");
        System.out.println("2 -> Create new question");
        System.out.println("3 -> Modify task data");
        pAlert("4 -> ALERT: Register fake users to full table");
        pAlert("5 -> ALERT: Delete user");
        pAlert("6 -> ALERT: Create fake Tasks and Questions with Answers to full table");
        pAlert("7 -> ALERT: Take fake exams to full table");
        pAlert("8 -> ALERT: Set grades to full table");
        pAlert("9 -> ALERT: Clear Tasks, Questions, Answers, Exams and Grades");
        System.out.println("10 -> STATS: abcTotal");
        System.out.println("11 -> STATS: timesTotal");
        System.out.println("12 -> STATS: correctNumberByTaskID");
        System.out.println("13 -> STATS: avgCorrect");
        System.out.println("X -> Logout administrator");
    }
}

