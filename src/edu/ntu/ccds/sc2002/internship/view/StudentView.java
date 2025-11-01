package edu.ntu.ccds.sc2002.internship.view;

import java.util.Scanner;

import edu.ntu.ccds.sc2002.internship.controller.StudentController;
import edu.ntu.ccds.sc2002.internship.model.Student;

public class StudentView {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("=== Simple Student Apply Test ===");
        System.out.print("Student ID (default s001): ");
        String sid = sc.nextLine().trim();
        if (sid.isEmpty())
            sid = "s001";

        System.out.print("Student Name (default Alice): ");
        String name = sc.nextLine().trim();
        if (name.isEmpty())
            name = "Alice";

        System.out.print("Internship ID to apply (default I001): ");
        String iid = sc.nextLine().trim();
        if (iid.isEmpty())
            iid = "I001";

        // create a Student and apply
        Student student = new Student(sid, name, 2, "Undeclared");
        StudentController s = new StudentController();
        s.applyForInternship(student, iid);

        System.out.println("Application attempt finished.");
        System.out.println("Check the application file: Internship_Applications_List.csv (in project folder)");
        sc.close();
    }
}