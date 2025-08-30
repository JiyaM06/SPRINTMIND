package main;
import java.sql.SQLException;
import java.util.Scanner;

import java.util.*;

public class Main {
    public static final String RED = "\u001B[31m";
    public static final String RESET = "\u001B[0m";
    public static final String CYAN = "\u001B[36m";
    public static final String PURPLE = "\u001B[35m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String MAGENTA = "\u001B[35m";

    public static void main(String[] args) throws Exception {
        header();
        Scanner sc = new Scanner(System.in);

        // Role selection
        String role = "";
        for (int attempts = 0; attempts < 2; attempts++) {
            System.out.print(BLUE+"Enter role (mentor/student): ");
            role = sc.nextLine().trim().toLowerCase();
            if (role.equals("mentor") || role.equals("student")) {
                if (role.equals("mentor")) {
                    while (true) {
                        // Show main menu
                        System.out.println(MAGENTA+"\nSelect an option:");
                        System.out.println(MAGENTA+"1. Login");
                        System.out.println(MAGENTA+"2. Exit");
                        System.out.print("Enter your choice: ");
                        String choice = sc.nextLine().trim();
                        switch (choice) {
                            case "1": // Login
                                String loginUsername = User.validateUsername();
                                if (loginUsername == null) break;

                                String loginPassword = User.validatePassword();
                                if (loginPassword == null) break;

                                // Create main.User object with entered username, role, and password
                                User existingUser = new User(loginUsername, role, loginPassword);

                                // Call login method (throws SQLException)
                                boolean success = existingUser.login(loginUsername); // no parameter needed
                                if (success) {
                                    System.out.println();
                                } else {
                                    System.out.println(RED+"Login failed! Check username or password.");
                                }
                                break;


                            case "2": // Exit
                                System.out.println(PURPLE+"Exiting application. Goodbye!");
                                sc.close();
                                return;

                            default:
                                System.out.println(RED+"Invalid choice. Please enter 1 or 2");
                        }
                        break;
                    }
                }
                else {
                    while (true) {
                        // Show main menu
                        System.out.println(MAGENTA+"\nSelect an option:");
                        System.out.println(MAGENTA+"1. Signup");
                        System.out.println(MAGENTA+"2. Login");
                        System.out.println(MAGENTA+"3. Exit");
                        System.out.print("Enter your choice: ");
                        String choice = sc.nextLine().trim();

                        switch (choice) {
                            case "1": // Signup
                                    String signupUsername = User.validateUsername();
                                    if (signupUsername == null) break;

                                    String signupPassword = User.validatePassword();
                                    if (signupPassword == null) break;

                                    User newUser = new User(signupUsername, role, signupPassword);
                                    newUser.signup(signupUsername);
                                break;

                            case "2": // Login
                                String loginUsername = User.validateUsername();
                                if (loginUsername == null) break;

                                String loginPassword = User.validatePassword();
                                if (loginPassword == null) break;

                                // Create main.User object with entered username, role, and password
                                User existingUser = new User(loginUsername, role, loginPassword);

                                // Call login method (throws SQLException)
                                boolean success = existingUser.login(loginUsername); // no parameter needed
                                if (success) {
                                    System.out.println();
                                } else {
                                    System.out.println(RED+"Login failed! Check username or password.");
                                }
                                break;


                            case "3": // Exit
                                System.out.println(PURPLE+"Exiting application. Goodbye!");
                                sc.close();
                                return;

                            default:
                                System.out.println(RED+"Invalid choice. Please enter 1, 2, or 3.");
                        }
                    }

                }
            }

            else {
                System.out.println("Invalid role.");
            }
        }

        if (!role.equals("mentor") && !role.equals("student")) {
            System.out.println(RED+"Program terminated due to invalid role...");
            return;
        }

        }


    public static void header() {
        // ASCII art for "SPRINT MIND"
        String[] art = {
                MAGENTA + "===========================================================================================\n" + RESET,
                CYAN + "███████  ██████  ██████    ██ ███    ██ ███████        ███    ███  ██   ███    ██ ██████  " + RESET,
                CYAN + "██       ██   ██ ██   ██   ██ ████   ██   ██           ████  ████  ██   ████   ██ ██   ██ " + RESET,
                CYAN + "  ███    █████   ██████    ██ ██ ██  ██   ██           ██ ████ ██  ██   ██ ██  ██ ██   ██ " + RESET,
                CYAN + "    ██   ██      ██   ██   ██ ██  ██ ██   ██           ██  ██  ██  ██   ██  ██ ██ ██   ██ " + RESET,
                CYAN + "██████   ██      ██    ██  ██ ██  █████   ██           ██      ██  ██   ██   ████ ██████  " + RESET,
                MAGENTA + "\n===========================================================================================\n" + RESET

        };

        // Print ASCII art
        for (String line : art) {
            System.out.println(line);
        }
    }
}
