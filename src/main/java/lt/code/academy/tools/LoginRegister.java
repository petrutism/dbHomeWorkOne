package lt.code.academy.tools;

import static lt.code.academy.tools.Print.*;

import lt.code.academy.SQLConnection;
import lt.code.academy.data.Reader;
import lt.code.academy.data.User;
import lt.code.academy.data.Writer;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.util.Scanner;

public class LoginRegister {
    public User loggedUser = new User();
    private AdminLogged adminLogged;
    private UserLogged userLogged;

    public LoginRegister() {
        Scanner sc = new Scanner(System.in);
        Connection c = SQLConnection.getInstance().getConnection();
        loginAction(sc, c);
    }

    private void loginAction(Scanner sc, Connection c) {
        String action;
        do {
            loginMenu();
            action = sc.nextLine();
            switch (action) {
                case "1" -> login(sc, c);
                case "2" -> register(sc, c);
                case "3" -> System.out.println("Finishing...");
                default -> pError("There is no such action...");
            }
        } while (!action.equals("3"));
    }

    private void login(Scanner sc, Connection c) {
        Reader.readUsers(c);

        if (Reader.users.size() == 0) {
            pAlert("There is no users registered. Please register...");
            return;
        }

        String userName;
        boolean userNameIsValid = false;
        String password;
        boolean passwordIsValid;
        int passwordCount = 0;

        System.out.println("Enter your username: ");
        userName = Utilities.inputValidString(sc);

        for (User u : Reader.users) {
            if (u.getUserName().equals(userName)) {
                userNameIsValid = true;
                loggedUser = u;
                break;
            }
        }
        if (!userNameIsValid) {
            pError("Username is not valid... Go home or register...");
            return;
        }

        do {
            //System.out.println("Enter correct password for user " + userName + ":");
            //password = Utilities.inputValidString(sc);
            password = readPassword("Enter correct password for user " + userName + ":");

            if (!loggedUser.getPassword().equals(DigestUtils.sha256Hex(password))) {
                passwordIsValid = false;
                passwordCount++;
                pAlert("Wrong password... ");
            } else {
                passwordIsValid = true;
            }
            if (passwordCount == 3) {
                pError("Maximum login attempts reached...");
                return;
            }
        } while (!passwordIsValid);

        if (loggedUser.isIs_admin()) {
            if (adminLogged == null) {
                adminLogged = new AdminLogged();
            }
            pSuccess("Successfully logged as administrator.");
            adminLogged.adminAction(sc, c);
            loggedUser = null;
        }
        if (loggedUser != null) {
            if (userLogged == null) {
                userLogged = new UserLogged();
            }
            pSuccess("Successfully logged as user.");
            userLogged.userAction(sc, c, loggedUser.getId());
            loggedUser = null;
        }
    }

    private void register(Scanner sc, Connection c) {
        Reader.readUsers(c);

        if (Reader.users.size() < 20) {
            String userName;
            String name;
            String surName;
            String password;
            String superPassword;
            boolean is_admin = false;
            boolean userNameIsInUse = false;

            do {
                if (userNameIsInUse) {
                    pAlert("Username is already used...");
                }
                System.out.println("Enter your username.");
                userName = Utilities.inputValidString(sc);

                userNameIsInUse = userNameInUse(userName);
            } while (userNameIsInUse);

            System.out.println("Enter your name.");
            name = Utilities.inputValidString(sc);
            System.out.println("Enter your surname.");
            surName = Utilities.inputValidString(sc);

            //System.out.print("Enter your password: ");
            //password = sc.nextLine();
            password = readPassword("Enter password: ");

            //System.out.print("Enter super password to set you as administrator: ");
            //superPassword = sc.nextLine();
            superPassword = readPassword("Enter super password to set you as administrator: ");

            if (DigestUtils.sha256Hex(superPassword).equals(DigestUtils.sha256Hex("31428571"))) {
                is_admin = true;
            }
            Writer.writeUser(userName, name, surName, password, is_admin, c);
        } else {
            pError("There already is 20 users registered...");
        }
    }

    private boolean userNameInUse(String userName) {
        for (User u : Reader.users) {
            if (u.getUserName().equals(userName)) {
                return true;
            }
        }
        return false;
    }

    private void loginMenu() {
        String text = """
                1 -> Login
                2 -> Register
                3 -> Finish
                """;
        System.out.println(text);
    }

    public static String readPassword(String prompt) {
        EraserThread et = new EraserThread(prompt);
        Thread mask = new Thread(et);
        mask.start();
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String password = "";
        try {
            password = in.readLine();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        et.stopMasking();
        return password;
    }
}



