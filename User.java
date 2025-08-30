package main;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

import dashboard.Student;
import dashboard.Mentor;

import java.sql.*;
import java.util.*;

public class User {
    public static final String PURPLE = "\u001B[35m";
    private String username;
    private String role; // "mentor" or "student"
    private int passwordHash;

    private static Scanner sc = new Scanner(System.in);

    // Constructor
    public User(String username, String role, String password) {
        this.username = username;
        this.role = role;
        this.passwordHash = customHash(password);
    }

    // Your provided hash method
    public static int customHash(String input) {
        int hash = 7;
        for (int i = 0; i < input.length(); i++) {
            hash = hash * 31 + input.charAt(i); // Prime multiplier
        }
        return hash;
    }

    // Validate username input: letters + digits allowed, must have letter, cannot start with digit
    public static String validateUsername() {
        for (int attempts = 0; attempts < 2; attempts++) {
            System.out.print("Enter username: ");
            String input = sc.nextLine().trim();
            if (!isValidUsername(input)) {
                System.out.println("Invalid entry.");
            } else {
                return input;
            }
        }
        System.out.println("Invalid entry. Try again later.");
        return null;
    }

    // Helper method to validate username
    private static boolean isValidUsername(String str) {
        if (str.isEmpty()) return false;                 // Empty invalid
        char first = str.charAt(0);
        if (first >= '0' && first <= '9') return false; // Cannot start with digit

        boolean hasLetter = false;
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if ((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z')) {
                hasLetter = true;
            } else if (ch >= '0' && ch <= '9') {
                // digits are okay
            } else {
                return false; // special character found
            }
        }
        return hasLetter; // must contain at least one letter
    }

    // Validate password input (not empty)
    public static String validatePassword() {
        for (int attempts = 0; attempts < 2; attempts++) {
            System.out.print("Enter password: ");
            String input = sc.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Invalid entry.");
            } else {
                return input;
            }
        }
        System.out.println("Invalid entry. Try again later.");
        return null;
    }

    // Signup: save user in role-specific table
    public void signup(String username) throws Exception {
        if (role.equalsIgnoreCase("student") && isExistsUsername(username)) {
            System.out.println("❌ Username already exists. Please choose another.");
            return;
        }
        Connection con = DBManager.getConnection();
        String sql = "";
        // Inside main.User.signup() for student role
        if (role.equalsIgnoreCase("student")) {
            int assignedMentorId = (int) (Math.random() * 5) + 1;
            System.out.println(PURPLE+"Assigned Mentor ID: " + assignedMentorId);
            System.out.println(PURPLE+"Student signup successful! ");

            sql = "INSERT INTO students (username, password, mentor_id) VALUES (?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, username);
            pst.setInt(2, passwordHash);
            pst.setInt(3, assignedMentorId);
            pst.executeUpdate();
            pst.close();

            String sql2 = "INSERT INTO mentees (mentor_id) VALUES (?)";
            PreparedStatement pst2 = con.prepareStatement(sql2);
            pst2.setInt(1, assignedMentorId);
            pst2.executeUpdate();
            pst2.close();

            // Create student's sprint table
            Statement stmt = con.createStatement();
            String tableSQL = "CREATE TABLE IF NOT EXISTS `std_" + username + "` (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "sprint_id INT," +
                    "subject VARCHAR(50)," +
                    "sprint_name VARCHAR(100)," +
                    "duration INT," +
                    "start_time TIME," +
                    "end_time TIME," +
                    "focus_score INT," +
                    "distraction_reason VARCHAR(50)," +
                    "date DATE)";
            stmt.executeUpdate(tableSQL);
            stmt.close();
            con.close();
        }
    }

    // Login: verify password
    public boolean login(String username) throws Exception {
        Connection con = DBManager.getConnection();
        String sql = "";

        if (role.equalsIgnoreCase("student")) {
            sql = "SELECT password FROM students WHERE username = ?";
        } else if (role.equalsIgnoreCase("mentor")) {
            sql = "SELECT password FROM mentors WHERE username = ?";
        } else {
            System.out.println("Invalid role.");
            DBManager.closeConnection(con);
            return false;
        }

        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1, username);
        ResultSet rs = pst.executeQuery();
        boolean valid = false;

        if (rs.next()) {
            int storedHash = rs.getInt("password");
            valid = storedHash == passwordHash;
            if (valid) {
                System.out.println("Login successful!");
                // Call dashboard after successful login
                if (role.equalsIgnoreCase("student")) {
                    new dashboard.Student().showDashboard(username); // call student dashboard
                }
                else if (role.equalsIgnoreCase("mentor")) {
                    new dashboard.Mentor().showDashboard(username); // call mentor dashboard
                }
            } else {
                System.out.println("Incorrect password.");
            }
        } else {
            System.out.println("Username not found.");
        }

        rs.close();
        pst.close();
        DBManager.closeConnection(con);
        return valid;
    }

    // Check if student username already exists
    public static boolean isExistsUsername(String username) throws SQLException {
        Connection con = DBManager.getConnection();
        String sql = "SELECT COUNT(*) FROM students WHERE username = ?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1, username);
        ResultSet rs = pst.executeQuery();

        boolean exists = false;
        if (rs.next()) {
            exists = rs.getInt(1) > 0;
        }

        rs.close();
        pst.close();
        con.close();
        return exists;
    }


    public String getRole() {
        return role;
    }

    public String getUsername() {
        return username;
    }
}
