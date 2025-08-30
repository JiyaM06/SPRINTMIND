package sprints;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import main.DBManager;


public class Journal {

    private static Scanner sc = new Scanner(System.in);

    public static void writeJournal(String username) throws SQLException {
        System.out.println("Enter your journal text:");
        String text = sc.nextLine();

        Connection con = DBManager.getConnection();
        String sql = "INSERT INTO journal (username, entry, created_at) VALUES (?, ?, NOW())";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, username);
        ps.setString(2, text);
        ps.executeUpdate();
        con.close();

        System.out.println("Journal entry saved successfully.");
    }

    public static void readJournal(String username) throws SQLException {
        Connection con = DBManager.getConnection();
        String sql = "SELECT entry, created_at FROM journal WHERE username=? ORDER BY created_at DESC";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();

        System.out.println("\n===== Your Journal Entries =====");
        while (rs.next()) {
            System.out.println(rs.getTimestamp("created_at") + " → " + rs.getString("entry"));
        }
        con.close();
    }
}
