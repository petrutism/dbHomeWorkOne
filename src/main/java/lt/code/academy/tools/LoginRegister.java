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
            //uzkomentuoti pass paslepimui
            System.out.println("Enter correct password for user " + userName + ":");
            password = Utilities.inputValidString(sc);

            //atkomentuoti pass paslepimui
            //password = readPassword("Enter correct password for user " + userName + ":");

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

                userNameIsInUse = Utilities.userNameInUse(userName);
            } while (userNameIsInUse);

            System.out.println("Enter your name.");
            name = Utilities.inputValidString(sc);
            System.out.println("Enter your surname.");
            surName = Utilities.inputValidString(sc);

            //uzkomentuoti pass paslepimui
            System.out.print("Enter your password: ");
            password = sc.nextLine();
            //atkomentuoti pass paslepimui
            //password = readPassword("Enter password: ");

            //uzkomentuoti pass paslepimui
            System.out.print("Enter super password to set you as administrator: ");
            superPassword = sc.nextLine();

            //atkomentuoti pass paslepimui
            //superPassword = readPassword("Enter super password to set you as administrator: ");

            if (DigestUtils.sha256Hex(superPassword).equals(DigestUtils.sha256Hex("31428571"))) {
                is_admin = true;
            }
            Writer.writeUser(userName, name, surName, password, is_admin, c);
        } else {
            pError("There already is 20 users registered...");
        }
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



