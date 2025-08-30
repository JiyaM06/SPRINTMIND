package sprints;
import java.util.Scanner;

import java.util.Scanner;
public class Distraction {

    private static Scanner sc = new Scanner(System.in);

    public static String handleDistraction(DistractionQueue queue) {
        System.out.println("\n⚠️ You got distracted!");
        System.out.print("Enter your distraction reason: ");
        String reason = sc.nextLine().trim();

        if (reason.isEmpty()) reason = "No reason provided";

        queue.logDistraction(reason);

        System.out.println("1. Pause sprint\n2. Exit sprint");
        System.out.print("Enter choice: ");
        String choice = sc.nextLine().trim();

        if (choice.equals("1")) {
            return pauseSprint(queue);
        } else if (choice.equals("2")) {
            System.exit(0);
            return null;
        } else {
            System.out.println("No valid choice, sprint will continue.");
            return reason;
        }
    }


    private static String pauseSprint(DistractionQueue queue) {
        System.out.println("⏸ Sprint Paused. Press 'p' to continue (auto exit after 5 min)");

        long pauseStart = System.currentTimeMillis();
        while ((System.currentTimeMillis() - pauseStart) < 300_000) { // 5 min timeout
            if (sc.hasNextLine()) {
                String input = sc.nextLine().trim();
                if (input.equalsIgnoreCase("p")) {
                    System.out.println("▶ Sprint Continued...");
                    return "Paused and continued";
                }
            }
        }
        String reason = "Exited due to long pause (>5 min)";
        queue.logDistraction(reason);
        return reason;
    }
}


class DistractionQueue {
    private int rear, front, size;
    private String[] queue;

    // Constructor to set max number of distractions
    public DistractionQueue(int maxSize) {
        this.size = maxSize;
        this.queue = new String[maxSize];
        this.front = -1;
        this.rear = -1;
    }

    // Log a new distraction reason
    public void logDistraction(String reason) {
        if (rear == size - 1) {
            System.out.println("⚠ Queue Overflow (Too many distractions!)");
        } else {
            if (front == -1) front = 0;
            rear++;
            queue[rear] = reason;
            System.out.println("✅ Distraction logged: " + reason);
        }
    }

    // Remove oldest distraction (optional, internal use)
    public String removeDistraction() {
        if (front == -1) return null;
        String removed = queue[front];
        if (front == rear) {
            front = -1;
            rear = -1;
        } else {
            front++;
        }
        return removed;
    }

    // Print all distractions
    public void printDistractions() {
        if (front == -1) {
            System.out.println("No distractions recorded yet.");
        } else {
            System.out.println("📌 Distractions recorded:");
            for (int i = front; i <= rear; i++) {
                System.out.println("- " + queue[i]);
            }
        }
    }

    // Get current count of distractions
    public int count() {
        return (front == -1) ? 0 : (rear - front + 1);
    }
}
