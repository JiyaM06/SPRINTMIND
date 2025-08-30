package sprints;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

import java.io.*;
import java.sql.*;
import java.util.*;

import main.DBManager;

public class Reports {

    public static void todayReport(String username) throws Exception {
        Scanner sc = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("Enter 1 for console report");
            System.out.println("Enter 2 for file report");
            System.out.println("Enter 3 for both");
            System.out.println("Enter 4 to exit");
            System.out.print("Your choice: ");
            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1 :
                    generateReportConsole(username);
                    break;
                case 2 :
                    generateReportFile(username);
                    break;
                case 3 :
                    generateReportConsole(username);
                    generateReportFile(username);
                    break;
                case 4 :
                    running = false;
                    break;
                default :
                    System.out.println("Invalid choice");
                    break;
            }
        }
    }

    // Console report
    public static void generateReportConsole(String username) throws Exception {
        Connection con = DBManager.getConnection();
        System.out.println("\n📘 Daily Report for " + username);
        System.out.println("----------------------------------");

        // Focused sprints
        System.out.println("\n✅ Focused Sprints:");
        String sqlSprints = "SELECT sprint_name, start_time, focus_score, distraction_reason, date " +
                "FROM std_" + username + " WHERE date = CURDATE()";
        Statement stmt1 = con.createStatement();
        ResultSet rs1 = stmt1.executeQuery(sqlSprints);
        while (rs1.next()) {
            System.out.println("\n  Date : " + rs1.getDate("date") +
                    "\n  Sprint Name: " + rs1.getString("sprint_name") +
                    "\n  Focus Score: " + rs1.getInt("focus_score") +
                    "\n  Distractions: " + rs1.getString("distraction_reason"));
        }

        // Milestones
        System.out.println("\n🎯 Milestones:");
        String sqlMilestones = "SELECT milestone_count, date FROM milestone m " +
                "JOIN students s ON m.student_id = s.Student_id " +
                "WHERE s.username = ? AND date = CURDATE()";
        PreparedStatement pst = con.prepareStatement(sqlMilestones);
        pst.setString(1, username);
        ResultSet rs2 = pst.executeQuery();
        while (rs2.next()) {
            System.out.println(" - Milestone Count: " + rs2.getInt("milestone_count") +
                    " | Date: " + rs2.getDate("date"));
        }

        System.out.println("----------------------------------");

        // Close resources
        rs1.close();
        rs2.close();
        stmt1.close();
        pst.close();
        DBManager.closeConnection(con);
    }

    // File report
    public static void generateReportFile(String username) throws Exception {
        String filePath = "D://daily_report_" + username + ".txt";

        try (FileWriter fw = new FileWriter(filePath)) {
            Connection con = DBManager.getConnection();
            fw.write("📘 Daily Report for " + username + "\n");
            fw.write("----------------------------------\n");

            // Focused sprints
            fw.write("\n✅ Focused Sprints:\n");
            String sqlSprints = "SELECT sprint_name, start_time, focus_score, distraction_reason, date " +
                    "FROM std_" + username + " WHERE date = CURDATE()";
            Statement stmt1 = con.createStatement();
            ResultSet rs1 = stmt1.executeQuery(sqlSprints);
            while (rs1.next()) {
                fw.write(" Date : " + rs1.getDate("date") +
                        " | Sprint Name: " + rs1.getString("sprint_name") +
                        " | Focus Score: " + rs1.getInt("focus_score") +
                        " | Distractions: " + rs1.getString("distraction_reason") + "\n");
            }

            // Milestones
            fw.write("\n🎯 Milestones:\n");
            String sqlMilestones = "SELECT milestone_count, date FROM milestone m " +
                    "JOIN students s ON m.student_id = s.student_id " +
                    "WHERE s.username = ? AND date = CURDATE()";
            PreparedStatement pst = con.prepareStatement(sqlMilestones);
            pst.setString(1, username);
            ResultSet rs2 = pst.executeQuery();
            while (rs2.next()) {
                fw.write(" - Milestone Count: " + rs2.getInt("milestone_count") +
                        " | Date: " + rs2.getDate("date") + "\n");
            }

            fw.write("----------------------------------\n");
            System.out.println("✅ Report saved to " + filePath);

            // Close resources
            rs1.close();
            rs2.close();
            stmt1.close();
            pst.close();
            DBManager.closeConnection(con);
        }
    }
}
