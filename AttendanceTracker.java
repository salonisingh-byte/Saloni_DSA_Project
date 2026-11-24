import java.util.*;
import java.io.*;

public class AttendanceTracker {

    static Scanner sc = new Scanner(System.in);

    static String[] rollNumbers = new String[100];
    static String[] names = new String[100];
    static String[][] attendance = new String[100][100];

    static int[] attendanceDays = new int[100];
    static int studentCount = 0;

    static HashMap<String, Integer> studentMap = new HashMap<>();

    static void addStudent() {
        if (studentCount == 100) {
            System.out.println("Student limit reached!");
            return;
        }

        System.out.print("Enter student name: ");
        String name = sc.nextLine();

        String roll = "R" + (101 + studentCount);

        rollNumbers[studentCount] = roll;
        names[studentCount] = name;

        studentMap.put(roll, studentCount);

        System.out.println("Student added successfully!");
        System.out.println("Roll Number: " + roll);

        studentCount++;
        saveData();
    }

    static void markAttendance() {
        if (studentCount == 0) {
            System.out.println("No students available!");
            return;
        }

        for (int i = 0; i < studentCount; i++) {

            if (attendanceDays[i] == 100) {
                System.out.println("Attendance limit reached for " + names[i]);
                continue;
            }

            System.out.println("\nName: " + names[i]);
            System.out.println("Roll Number: " + rollNumbers[i]);

            System.out.print("Enter P for Present or A for Absent: ");
            String status = sc.nextLine().toUpperCase();

            while (!status.equals("P") && !status.equals("A")) {
                System.out.print("Invalid input! Enter P or A: ");
                status = sc.nextLine().toUpperCase();
            }

            attendance[i][attendanceDays[i]] = status;
            attendanceDays[i]++;
        }

        saveData();
        System.out.println("Attendance marked successfully!");
    }

    static double calculatePercentage(int index) {
        if (attendanceDays[index] == 0) {
            return 0;
        }

        int present = 0;

        for (int j = 0; j < attendanceDays[index]; j++) {
            if (attendance[index][j].equals("P")) {
                present++;
            }
        }

        return (present * 100.0) / attendanceDays[index];
    }

    static void viewPercentage() {
        System.out.print("Enter roll number: ");
        String roll = sc.nextLine().toUpperCase();

        if (studentMap.containsKey(roll)) {
            int index = studentMap.get(roll);

            System.out.println("Name: " + names[index]);
            System.out.println("Roll Number: " + roll);
            System.out.printf("Attendance Percentage: %.2f%%\n",
                    calculatePercentage(index));
        } else {
            System.out.println("Student not found!");
        }
    }

    static void searchStudent() {
        System.out.print("Enter roll number: ");
        String roll = sc.nextLine().toUpperCase();

        if (studentMap.containsKey(roll)) {
            int index = studentMap.get(roll);

            System.out.println("\nStudent Found!");
            System.out.println("Name: " + names[index]);
            System.out.println("Roll Number: " + roll);
            System.out.printf("Attendance: %.2f%%\n",
                    calculatePercentage(index));
        } else {
            System.out.println("Student not found!");
        }
    }

    static void sortByPercentage() {
        if (studentCount == 0) {
            System.out.println("No students available!");
            return;
        }

        int[] index = new int[studentCount];

        for (int i = 0; i < studentCount; i++) {
            index[i] = i;
        }

        for (int i = 0; i < studentCount - 1; i++) {
            for (int j = 0; j < studentCount - i - 1; j++) {

                if (calculatePercentage(index[j]) <
                    calculatePercentage(index[j + 1])) {

                    int temp = index[j];
                    index[j] = index[j + 1];
                    index[j + 1] = temp;
                }
            }
        }

        System.out.println("\nStudents Sorted by Attendance:");

        for (int i = 0; i < studentCount; i++) {
            int k = index[i];

            System.out.printf("%s - %s - %.2f%%\n",
                    rollNumbers[k], names[k],
                    calculatePercentage(k));
        }
    }

    static void saveData() {
        try {
            PrintWriter pw = new PrintWriter(
                    new FileWriter("attendance.txt"));

            for (int i = 0; i < studentCount; i++) {

                pw.print(rollNumbers[i] + "|");
                pw.print(names[i] + "|");
                pw.print(attendanceDays[i] + "|");

                for (int j = 0; j < attendanceDays[i]; j++) {
                    pw.print(attendance[i][j]);

                    if (j < attendanceDays[i] - 1) {
                        pw.print(",");
                    }
                }

                pw.println();
            }

            pw.close();

        } catch (IOException e) {
            System.out.println("Error saving data!");
        }
    }

    static void loadData() {
        try {
            BufferedReader br = new BufferedReader(
                    new FileReader("attendance.txt"));

            String line;

            while ((line = br.readLine()) != null &&
                    studentCount < 100) {

                String[] data = line.split("\\|", -1);

                rollNumbers[studentCount] = data[0];
                names[studentCount] = data[1];

                attendanceDays[studentCount] =
                        Integer.parseInt(data[2]);

                if (!data[3].isEmpty()) {
                    String[] records = data[3].split(",");

                    for (int j = 0; j < records.length; j++) {
                        attendance[studentCount][j] = records[j];
                    }
                }

                studentMap.put(
                        rollNumbers[studentCount], studentCount);

                studentCount++;
            }

            br.close();

        } catch (FileNotFoundException e) {
            System.out.println("New attendance file will be created.");
        } catch (IOException e) {
            System.out.println("Error loading data!");
        }
    }

    public static void main(String[] args) {

        loadData();

        int choice;

        do {
            System.out.println("\n===== ATTENDANCE TRACKER =====");
            System.out.println("1. Add Student");
            System.out.println("2. Mark Attendance");
            System.out.println("3. View Attendance Percentage");
            System.out.println("4. Search Student");
            System.out.println("5. Sort Students by Percentage");
            System.out.println("6. Exit");

            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    addStudent();
                    break;

                case 2:
                    markAttendance();
                    break;

                case 3:
                    viewPercentage();
                    break;

                case 4:
                    searchStudent();
                    break;

                case 5:
                    sortByPercentage();
                    break;

                case 6:
                    saveData();
                    System.out.println("Thank you for using Attendance Tracker!");
                    break;

                default:
                    System.out.println("Invalid choice!");
            }

        } while (choice != 6);
    }
}
