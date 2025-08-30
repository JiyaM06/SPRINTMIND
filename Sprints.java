package sprints;
import java.io.BufferedReader;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import main.DBManager;
import java.io.*;
import java.sql.Date;
import java.sql.*;
import java.util.*;
import java.time.*;

public class Sprints {

    static Scanner sc = new Scanner(System.in);

    // ✅ Add new sprint
    public static void addSprint(String username) throws Exception {
        System.out.print("Enter subject: ");
        String subject = sc.nextLine();

        System.out.print("Enter sprint name: ");
        String sprintName = sc.nextLine();

        System.out.print("Enter duration in minutes (less than 100): ");
        String input = sc.nextLine();

        if (!checkNumeric(input)) {
            System.out.println("Invalid input.");
            return;
        }
        int duration = Integer.parseInt(input);
        if (duration <= 0 || duration >= 100) {
            System.out.println("Duration must be between 1 and 99 minutes.");
            return;
        }

        // Ask mentor for file path (can be TXT, PDF, DOCX, etc.)
        System.out.print("Enter path of notes file: ");
        String filePath = sc.nextLine();
        File file = new File(filePath);

        if (!file.exists() || !file.isFile()) {
            System.out.println("❌ File not found or invalid path.");
            return;
        }

        try (Connection con = DBManager.getConnection();
             PreparedStatement psMentor = con.prepareStatement(
                     "SELECT id FROM mentors WHERE username=?")) {

            psMentor.setString(1, username);
            try (ResultSet rs = psMentor.executeQuery()) {
                if (!rs.next()) {
                    System.out.println("❌ Mentor not found.");
                    return;
                }

                int id = rs.getInt("id");
                String tableName = "mentor_" + id;

                String sql = "INSERT INTO " + tableName +
                        " (subject, sprint_name, duration, notes, date) VALUES (?, ?, ?, ?, ?)";

                try (PreparedStatement ps = con.prepareStatement(sql);
                     FileInputStream fis = new FileInputStream(file)) {

                    ps.setString(1, subject);
                    ps.setString(2, sprintName);
                    ps.setInt(3, duration);
                    ps.setBlob(4, fis);  // ✅ Store as BLOB
                    ps.setDate(5, Date.valueOf(LocalDate.now()));

                    ps.executeUpdate();
                    System.out.println("✅ Sprint added successfully with notes file (stored as BLOB).");
                }
            }
        }
    }


    // ✅ Update sprint
    public static void updateSprint(String username) throws Exception {
        try (Connection con = DBManager.getConnection()) {

            // ✅ Get mentor ID
            int mentorId;
            String mentorQuery = "SELECT mentor_id FROM students WHERE username=?";
            try (PreparedStatement psMentor = con.prepareStatement(mentorQuery)) {
                psMentor.setString(1, username);
                try (ResultSet rs = psMentor.executeQuery()) {
                    if (!rs.next()) {
                        System.out.println("❌ Student not found.");
                        return;
                    }
                    mentorId = rs.getInt("mentor_id");
                }
            }
            String mentorTable = "`mentor_" + mentorId + "`";

            // ✅ Show sprints
            System.out.println("\n===== Available Sprints =====");
            try (Statement stmt = con.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT sprintID, sprint_name FROM " + mentorTable)) {
                while (rs.next()) {
                    System.out.println("ID: " + rs.getInt("sprintID") +
                            " | Name: " + rs.getString("sprint_name"));
                }
            }

            // ✅ Get sprint ID
            System.out.print("Enter sprint ID to update: ");
            int sprintID = Integer.parseInt(sc.nextLine());

            // ✅ Update details
            System.out.print("Enter new sprint name: ");
            String sprintName = sc.nextLine();
            System.out.print("Enter new subject: ");
            String subject = sc.nextLine();
            System.out.print("Enter new duration (minutes): ");
            int duration = Integer.parseInt(sc.nextLine());

            // ✅ File for notes (txt file → store as BLOB)
            System.out.print("Enter file path for notes (txt): ");
            String filePath = sc.nextLine();

            String updateQuery = "UPDATE " + mentorTable +
                    " SET sprint_name=?, subject=?, duration=?, notes=? WHERE sprintID=?";
            try (PreparedStatement psUpdate = con.prepareStatement(updateQuery);
                 FileInputStream fis = new FileInputStream(filePath)) {

                psUpdate.setString(1, sprintName);
                psUpdate.setString(2, subject);
                psUpdate.setInt(3, duration);
                psUpdate.setBinaryStream(4, fis, (int) new File(filePath).length()); // ✅ BLOB here
                psUpdate.setInt(5, sprintID);

                int rows = psUpdate.executeUpdate();
                if (rows > 0) {
                    System.out.println("✅ Sprint updated successfully.");
                } else {
                    System.out.println("❌ Sprint not found.");
                }
            }
        }
    }



    // ✅ Delete sprint from mentor table
    public static void deleteSprint(String username) throws SQLException {
        System.out.print("Enter sprint ID to delete: ");
        String input = sc.nextLine();
        if (!checkNumeric(input)) {
            System.out.println("Invalid input");
            return;
        }
        int sprintID = Integer.parseInt(input);

        Connection con = DBManager.getConnection();

        // Get mentor_id
        String mi = "SELECT id FROM mentors WHERE username=?";
        PreparedStatement psMentor = con.prepareStatement(mi);
        psMentor.setString(1, username);
        ResultSet rs = psMentor.executeQuery();

        if (!rs.next()) {
            System.out.println("❌ Mentor not found.");
            con.close();
            return;
        }

        int mentorId = rs.getInt("id");
        String tableName = "mentor_" + mentorId;

        // Delete sprint
        String sql = "DELETE FROM " + tableName + " WHERE sprintID=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, sprintID);

        int rows = ps.executeUpdate();
        if (rows > 0) System.out.println("✅ Sprint deleted successfully.");
        else System.out.println("❌ Sprint ID not found.");

        // Close resources
        rs.close();
        psMentor.close();
        ps.close();
        con.close();
    }


    // ✅ View all sprints
    public static void viewSprints(String username) throws Exception {
        Connection con = DBManager.getConnection();

        // Step 1: Get mentor_id of this student
        String mi = "SELECT mentor_id FROM students WHERE username=?";
        PreparedStatement psMentor = con.prepareStatement(mi);
        psMentor.setString(1, username);
        ResultSet rs = psMentor.executeQuery();

        while (rs.next()) {
            int id = rs.getInt("mentor_id");
            String tableName = "mentor_" + id;

            // Step 2: Fetch sprints from that mentor's table
            String sql = "SELECT sprintID , subject , sprint_name , duration , date  FROM " + tableName;
            Statement stmt = con.createStatement();
            ResultSet rs1 = stmt.executeQuery(sql);

            while (rs1.next()) {
                System.out.println(
                        "ID: " + rs1.getInt("sprintID") +
                                " | Subject Name: " + rs1.getString("subject") +
                                " | Sprint Name: " + rs1.getString("sprint_name") +
                                " | Duration: " + rs1.getInt("duration") + " mins" +
                                " | Date: " + rs1.getDate("date")
                );
                rs1.close();
                stmt.close();
            }
            rs.close();
            con.close();
        }
    }

    // ✅ Start sprint (with focus time, pause/exit, distraction, score)
    // Inside Sprints class
    public static void startSprint(String username) throws Exception {
        // 1️⃣ Get DB connection
        try (Connection con = DBManager.getConnection()) {

            // 2️⃣ Get mentor ID of student
            int mentorId;
            String mentorQuery = "SELECT mentor_id FROM students WHERE username=?";
            try (PreparedStatement psMentor = con.prepareStatement(mentorQuery)) {
                psMentor.setString(1, username);
                try (ResultSet rsMentor = psMentor.executeQuery()) {
                    if (!rsMentor.next()) {
                        System.out.println("❌ Student not found.");
                        return;
                    }
                    mentorId = rsMentor.getInt("mentor_id");
                }
            }
            String mentorTable = "`mentor_" + mentorId + "`";

            // 3️⃣ Show available sprints
            System.out.println("\n===== Available Sprints =====");
            try (Statement stmt = con.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT sprintID, sprint_name, subject, duration FROM " + mentorTable)) {
                while (rs.next()) {
                    System.out.println(
                            "ID: " + rs.getInt("sprintID") +
                                    " | Subject: " + rs.getString("subject") +
                                    " | Name: " + rs.getString("sprint_name") +
                                    " | Duration: " + rs.getInt("duration") + " mins"
                    );
                }
            }

            // 4️⃣ Get sprint ID to start
            System.out.print("Enter sprint ID to start: ");
            String input = sc.nextLine();
            if (!checkNumeric(input)) {
                System.out.println("Invalid input.");
                return;
            }
            int selectedSprintID = Integer.parseInt(input);

            // 5️⃣ Fetch sprint details (including notes)
            String sprintName = null, subject = null;
            int duration = 0;
            Blob notesBlob = null; // ✅ change Clob → Blob
            String fetchSprint = "SELECT sprint_name, subject, duration, notes FROM " + mentorTable + " WHERE sprintID=?";
            try (PreparedStatement psSprint = con.prepareStatement(fetchSprint)) {
                psSprint.setInt(1, selectedSprintID);
                try (ResultSet rsSprint = psSprint.executeQuery()) {
                    if (!rsSprint.next()) {
                        System.out.println("❌ Sprint not found.");
                        return;
                    }
                    sprintName = rsSprint.getString("sprint_name");
                    subject = rsSprint.getString("subject");
                    duration = rsSprint.getInt("duration");
                    notesBlob = rsSprint.getBlob("notes"); // ✅ get BLOB here
                }
            }

            // 6️⃣ Insert into student's sprint table
            String studentTable = "`std_" + username + "`";
            int studentSprintID;
            String insertStudent = "INSERT INTO " + studentTable +
                    " (sprint_id, sprint_name, subject, duration, date, start_time) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement psInsert = con.prepareStatement(insertStudent, Statement.RETURN_GENERATED_KEYS)) {
                psInsert.setInt(1, selectedSprintID);
                psInsert.setString(2, sprintName);
                psInsert.setString(3, subject);
                psInsert.setInt(4, duration);
                psInsert.setDate(5, Date.valueOf(LocalDate.now()));
                psInsert.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
                psInsert.executeUpdate();

                try (ResultSet rsGenerated = psInsert.getGeneratedKeys()) {
                    if (rsGenerated.next()) {
                        studentSprintID = rsGenerated.getInt(1);
                    } else {
                        throw new SQLException("Failed to retrieve generated student sprint ID.");
                    }
                }
            }

            // 7️⃣ Print notes
            System.out.println("\n🚀 Starting sprint: " + sprintName + " (" + duration + " mins)");
            if (notesBlob != null) {
                try (InputStream is = notesBlob.getBinaryStream();
                     BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                    System.out.println("\n📄 Notes:");
                    String line;
                    while ((line = br.readLine()) != null) {
                        System.out.println(line);
                    }
                }
            }

            // 8️⃣ Start distraction thread
            DistractionQueue queue = new DistractionQueue(10);
            Thread distractionThread = new Thread(() -> {
                Scanner distractionScanner = new Scanner(System.in);
                while (!Thread.currentThread().isInterrupted()) {
                    if (distractionScanner.hasNextLine()) {
                        distractionScanner.nextLine(); // consume Enter
                        String reason = Distraction.handleDistraction(queue);
                        System.out.println("⚡ Distraction logged: " + reason);
                    }
                }
            });
            distractionThread.start();

            // 9️⃣ Timer loop
            int focusMinutes = 0;
            for (int remaining = duration; remaining > 0; remaining--) {
                System.out.println("⏳ Time left: " + remaining + " minutes");
                Thread.sleep(1000 * 60); // simulate 1 minute
                focusMinutes++;
            }
            distractionThread.interrupt();

            // 🔟 Calculate focus score
            int focusScore = calculateFocusScore(duration, focusMinutes);

            // 1️⃣1️⃣ Update sprint record
            String updateStudent = "UPDATE " + studentTable +
                    " SET end_time=?, focus_score=? WHERE id=?";
            try (PreparedStatement psUpdate = con.prepareStatement(updateStudent)) {
                psUpdate.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
                psUpdate.setInt(2, focusScore);
                psUpdate.setInt(3, studentSprintID);
                psUpdate.executeUpdate();
            }

            // 1️⃣2️⃣ Feedback
            System.out.println("\n🏁 Sprint ended. Focus minutes: " + focusMinutes + "/" + duration);
            System.out.println("⭐ Focus Score: " + focusScore);
            giveFeedback(focusScore);

            System.out.println("\n===== Distractions Recorded During Sprint =====");
            queue.printDistractions();
        }
    }



    // ✅ Focus score calculator
    private static int calculateFocusScore(int planned, int actual) {
        return (int) (((double) actual / planned) * 100);
    }

    // ✅ Feedback
    private static void giveFeedback(int score) {
        if (score >= 90)
        {
            System.out.println("🔥 Excellent focus!");
        }
        else if (score >= 70)
        {
            System.out.println("👍 Good job, can improve.");
        }
        else if (score >= 50){
            System.out.println("⚠ Stay consistent, avoid distractions.");
        }
        else
        {
            System.out.println("😟 You need to improve your focus.");
        }
    }

    public static boolean checkNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }

        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
