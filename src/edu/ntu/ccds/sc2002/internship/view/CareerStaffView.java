package edu.ntu.ccds.sc2002.internship.view;

import java.util.ArrayList;
import java.util.Scanner;

public class CareerStaffView {

    public static void main(String[] args) {
        CareerStaff staff = new CareerStaff("1", "Alice Tan", "Officer", "alice.tan@university.edu", "Career Services");

        ArrayList<CompanyRepresentative> comRepList = new ArrayList<>();
        comRepList.add(new CompanyRepresentative("1", "John Lim", new Company("TechNova"), "Sales", "Executive", "john@technova.com"));
        comRepList.add(new CompanyRepresentative("2", "Maya Ong", new Company("BlueStar"), "Marketing", "Manager", "maya@bluestar.com"));

        comRepList.get(0).setStatus(Status.PENDING);
        comRepList.get(1).setStatus(Status.PENDING);

        Scanner sc = new Scanner(System.in);
        int choice = -1;

        while(choice != 0) {
            System.out.println("\n===== CAREER STAFF MENU =====");
            System.out.println("Logged in as: " + staff.getName());
            System.out.println("------------------------------");
            System.out.println("[1] View Pending Company Representatives");
            System.out.println("[2] Approve a Company Representative");
            System.out.println("[0] Exit");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();
            sc.nextLine();

            switch(choice) {

                case 1:
                    System.out.println("\n--- Pending Company Representatives ---");
                    for (CompanyRepresentative rep : comRepList) {
                        if (rep.getStatus() != Status.SUCCESSFUL) {
                            System.out.println(rep.getUserID() + " | " + rep.getName() + " | " + rep.getCompany().getName());
                        }
                    }
                    break;

                case 2:
                    System.out.print("Enter UserID to approve: ");
                    String repID = sc.nextLine();

                    CompanyRepresentative target = null;
                    for (CompanyRepresentative rep : comRepList) {
                        if (rep.getUserID().equals(repID)) {
                            target = rep;
                            break;
                        }
                    }

                    if (target == null) {
                        System.out.println("Representative not found.");
                    } else {
                        boolean approved = staff.authoriseComRepAcc(target);
                        if (approved) {
                            System.out.println(target.getName() + " has been approved!");
                        } else {
                            System.out.println("Approval failed.");
                        }
                    }
                    break;

                case 0:
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid option.");
            }
        }
        sc.close();
    }
}
