package dashboard;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import java.sql.*;
import java.util.*;
import sprints.Sprints;
import sprints.MilestoneTracker;
import sprints.Reports;
import sprints.Journal;
import main.DBManager;

public class Student {
    public static final String RED = "\u001B[31m";
    public static final String RESET = "\u001B[0m";
    public static final String CYAN = "\u001B[36m";
    public static final String PURPLE = "\u001B[35m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String MAGENTA = "\u001B[35m";
    private Scanner sc = new Scanner(System.in);
    MilestoneTracker mt = new MilestoneTracker("My Milestones");

    public void showDashboard(String username) throws Exception {
        int choice ;

            System.out.println(CYAN + "\n===== Student Dashboard =====");
            System.out.println(YELLOW + "1. Start Sprint");
            System.out.println(YELLOW + "2. View Sprints");
            System.out.println(YELLOW + "3. Write Journal");
            System.out.println(YELLOW + "4. Read Journal");
            System.out.println(YELLOW + "5. View Milestones");
            System.out.println(YELLOW + "6. View Report");
            System.out.println(YELLOW + "7. Exit Application");
            System.out.print("Enter your choice: ");


            String input = sc.nextLine();
            if (!checkNumeric(input)) {
                System.out.println(RED + "Invalid input! Please enter a number.");

            }

            choice = Integer.parseInt(input);

            switch (choice) {
                case 1:
                    int studentId = getStudentId(username);
                    if (studentId != -1) {
                        Sprints.startSprint(username);
                    } else {
                        System.out.println(RED + "❌ Error fetching student ID.");
                    }
                    break;
                case 2:
                    Sprints.viewSprints(username);
                    break;
                case 3:
                    Journal.writeJournal(username);
                    break;
                case 4:
                    Journal.readJournal(username);
                    break;
                case 5:
                    int studentIdForMilestone = getStudentId(username);
                    if (studentIdForMilestone != -1) {
                        // Fetch today’s focus scores and distractions from DB
                        List<Integer> focusScores = fetchFocusScores(username);
                        List<Integer> distractions = fetchDistractions(username);

                        // Generate milestone and save in DB
                        mt.generate(focusScores, distractions, username, studentIdForMilestone);

                        // Show milestones
                        mt.display();
                        break;
                    } else {
                        System.out.println("❌ Error fetching student ID for milestones.");
                    }
                case 6:
                    Reports.todayReport(username);
                    break;
                case 7:
                    System.out.println(PURPLE + "Exiting application. Goodbye!");
                    System.exit(0);
                default:
                    System.out.println(RED + "Invalid choice, try again.");
            }
    }
    private int getStudentId(String username) {
        int id = -1;
        try {
            Connection con = DBManager.getConnection();
            PreparedStatement pst = con.prepareStatement("SELECT student_id FROM students WHERE username=?");
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                id = rs.getInt("student_id");
            }
            rs.close();
            pst.close();
            DBManager.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    private boolean checkNumeric(String str) {
        if (str == null || str.isEmpty()) {
            System.out.println(RED+"Invalid input: empty string.");
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                System.out.println(RED+"Invalid input:");
                return false;
            }
        }
        return true;
    }
    private List<Integer> fetchFocusScores(String username) throws Exception {
        List<Integer> scores = new ArrayList<>();
        Connection con = DBManager.getConnection();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(
                "SELECT focus_score FROM std_" + username + " WHERE date = CURDATE()"
        );
        while (rs.next()) {
            scores.add(rs.getInt("focus_score"));
        }
        rs.close();
        st.close();
        con.close();
        return scores;
    }

    private List<Integer> fetchDistractions(String username) throws Exception {
        List<Integer> distractions = new ArrayList<>();
        Connection con = DBManager.getConnection();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(
                "SELECT COUNT(distraction_reason) AS d FROM std_" + username + " WHERE date = CURDATE()"
        );
        while (rs.next()) {
            distractions.add(rs.getInt("d"));
        }
        rs.close();
        st.close();
        con.close();
        return distractions;
    }
}
