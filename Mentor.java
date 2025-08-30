package dashboard;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

import java.util.Scanner;
import java.sql.*;
import sprints.Sprints;
import main.DBManager;

public class Mentor {
    public static final String RED = "\u001B[31m";
    public static final String CYAN = "\u001B[36m";
    public static final String PURPLE = "\u001B[35m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String MAGENTA = "\u001B[35m";

    public void showDashboard(String username) throws Exception {

        Scanner sc = new Scanner(System.in);
        int mentorId = getMentorId(username);
        if (mentorId == -1) return;


            int choice = -1;
            System.out.println(CYAN + "\n===== Mentor Dashboard =====");
            System.out.println(YELLOW + "1. View All Assigned Students");
            System.out.println(YELLOW + "2. Add Sprint");
            System.out.println(YELLOW + "3. Update Sprint");
            System.out.println(YELLOW + "4. Delete Sprint");
            System.out.println(YELLOW + "5. View Student Performance");
            System.out.println(YELLOW + "6. Exit ");
            System.out.print("Enter your choice: ");
            choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1:
                    viewAssignedStudents(mentorId);
                    break;
                case 2:
                    Sprints.addSprint(username);
                    break;
                case 3:
                    Sprints.updateSprint(username);
                    break;
                case 4:
                    Sprints.deleteSprint(username);
                    break;
                case 5:
                    System.out.print(PURPLE + "Enter Student ID: ");
                    int studentId = Integer.parseInt(sc.nextLine());
                    viewStudentPerformance(studentId);
                    break;
                case 6 :
                    System.out.println(PURPLE+"Exiting application. Goodbye!");
                    System.exit(0);
                default:
                    System.out.println(RED + "Invalid choice, try again.");
                    break;
            }

    }

    private int getMentorId(String username) throws SQLException {
        Connection con = DBManager.getConnection();
        String sql = "SELECT id FROM mentors WHERE username = ?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1, username);
        ResultSet rs = pst.executeQuery();
        int mentorId = -1;
        if (rs.next()) {
            mentorId = rs.getInt("id");
        } else {
            System.out.println("❌ Mentor not found.");
        }
        rs.close();
        pst.close();
        con.close();
        return mentorId;
    }

    private void viewAssignedStudents(int mentorId) throws SQLException {
        Connection con = DBManager.getConnection();
        String sql = "SELECT s.student_id, s.username " +
                "FROM students s " +
                "JOIN mentees m ON s.student_id = m.student_id " +
                "WHERE m.mentor_id = ?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setInt(1, mentorId);
        ResultSet rs = pst.executeQuery();

        System.out.println(GREEN+"\nAssigned Students:");
        boolean found = false;
        while (rs.next()) {
            System.out.println(MAGENTA+"ID: " + rs.getInt("student_id") + " | Username: " + rs.getString("username"));
            found = true;
        }
        if (!found) System.out.println("No students assigned.");
        rs.close();
        pst.close();
        con.close();
    }

    private void viewStudentPerformance(int studentId) throws Exception {
        Connection con = DBManager.getConnection();
        // Get username
        String sqlUser = "SELECT username FROM students WHERE student_id = ?";
        PreparedStatement pst = con.prepareStatement(sqlUser);
        pst.setInt(1, studentId);
        ResultSet rs = pst.executeQuery();
        if (!rs.next()) {
            System.out.println("❌ Student not found.");
            rs.close();
            pst.close();
            con.close();
            return;
        }
        String username = rs.getString("username");
        rs.close();
        pst.close();

        // Display milestone aggregation
        String sqlMilestone = "SELECT COALESCE(SUM(m.milestone_count),0) AS total_milestones " +
                "FROM milestone m " +
                "WHERE m.student_id = ?";
        pst = con.prepareStatement(sqlMilestone);
        pst.setInt(1, studentId);
        rs = pst.executeQuery();
        int totalMilestones = 0;
        if (rs.next()) totalMilestones = rs.getInt("total_milestones");
        System.out.println(MAGENTA+"\nStudent: " + username + " | Total Milestones: " + totalMilestones);
        rs.close();
        pst.close();

        // Display student's sprint history from std_<username> table
        String stdTable = "std_" + username;
        String sqlSprints = "SELECT sprint_id, subject, sprint_name, duration, start_time,  focus_score, distraction_reason, date " +
                "FROM " + stdTable;
        Statement stmt = con.createStatement();
        ResultSet rsSprints = stmt.executeQuery(sqlSprints);

        System.out.println("\n===== Sprint History =====");
        while (rsSprints.next()) {
            System.out.println(
                    "ID: " + rsSprints.getInt("sprint_id") +
                            " | Subject: " + rsSprints.getString("subject") +
                            " | Sprint Name: " + rsSprints.getString("sprint_name") +
                            " | Duration: " + rsSprints.getInt("duration") + " mins" +
                            " | Start: " + rsSprints.getTimestamp("start_time") +
                            " | Focus Score: " + rsSprints.getInt("focus_score") +
                            " | Distractions: " + rsSprints.getString("distraction_reason") +
                            " | Date: " + rsSprints.getDate("date")
            );
        }
        rsSprints.close();
        stmt.close();
        con.close();
    }
}
