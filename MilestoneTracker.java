package sprints;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

import main.DBManager;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;

// Abstract tracker
abstract class Tracker {
    protected String trackerName;
    public Tracker(String name) {
        this.trackerName = name;
    }
    public abstract void display();
    public abstract int generate(List<Integer> focusScores, List<Integer> distractionCounts, String username, int studentId);
}



// BST Node for milestones
class MilestoneNode {
    String name;
    LocalDate dueDate;
    boolean completed;
    int successScore;
    MilestoneNode left, right;

    MilestoneNode(String name, LocalDate dueDate, int score) {
        this.name = name;
        this.dueDate = dueDate;
        this.successScore = score;
        this.completed = score >= 70;
        this.left = this.right = null;
    }
}

// MilestoneTracker using BST and storing count in DB
public class MilestoneTracker extends Tracker {

    private MilestoneNode root;

    public MilestoneTracker(String name) {
        super(name);
        root = null;
    }

    // Insert by successScore instead of dueDate
    private MilestoneNode insertRec(MilestoneNode current, MilestoneNode node) {
        if (current == null) return node;
        if (node.successScore < current.successScore)
            current.left = insertRec(current.left, node);
        else
            current.right = insertRec(current.right, node);
        return current;
    }


    private int countNodes(MilestoneNode node) {
        if (node == null) return 0;
        return 1 + countNodes(node.left) + countNodes(node.right);
    }

    public void insertMilestone(String name, int score) {
        MilestoneNode node = new MilestoneNode(name, LocalDate.now(), score);
        root = insertRec(root, node);
    }

    @Override
    public void display() {
        System.out.println("\n===== " + trackerName + " =====");
        displayRec(root);
    }

    private void displayRec(MilestoneNode node) {
        if (node != null) {
            displayRec(node.left);
            System.out.println("Milestone: " + node.name +
                    " | Due: " + node.dueDate +
                    " | Completed: " + node.completed +
                    " | Score: " + node.successScore);
            displayRec(node.right);
        }
    }

    // Generate milestone based on focus & distractions
    @Override
    public int generate(List<Integer> focusScores, List<Integer> distractionCounts, String username, int studentId) {
        int totalSprints = focusScores.size();
        int sumFocus = 0, totalDistractions = 0;

        for (int score : focusScores) sumFocus += score;
        for (int dist : distractionCounts) totalDistractions += dist;

        int avgFocus = (totalSprints == 0) ? 0 : sumFocus / totalSprints;
        int penalty = totalDistractions * 2;
        int finalScore = Math.max(avgFocus - penalty, 0);

        // Insert in BST
        insertMilestone("Milestone " + (countNodes(root)+1), finalScore);

        // Update milestone table using CallableStatement
        try {
            Connection con = DBManager.getConnection();
            CallableStatement cs = con.prepareCall("{CALL insert_milestone(?, ?, ?, ?)}");
            cs.setInt(1, studentId);
            cs.setString(2, username);
            cs.setDate(3, Date.valueOf(LocalDate.now()));
            cs.setInt(4, countNodes(root)); // milestone count
            cs.executeUpdate();
            cs.close();
            con.close();
        } catch (Exception e) {
            System.out.println("❌ Error updating milestone count in DB: " + e.getMessage());
        }

        return finalScore;
    }
}
