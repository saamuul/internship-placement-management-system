package main;

import util.CSVLoader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import model.CompanyRepresentative;

public class InternshipSystem {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
        //testing read function start
        String path = "data/sample_student_list.csv";

        String[][] data1 = CSVLoader.read(path);
        for (String[] row : data1) {
            for (String value : row) {
                System.out.print(value + " | ");
            }
            System.out.println();
        }
        //testing read function end


        // testing and writing sample company rep list
       /* String filePath = "data/sample_company_representative_list.csv";

        // Example data
        // data to input : CompanyRepID,Name,CompanyName,Department,Position,Email,Status
        String[][] data1 = {
            {"R001", "Annie", "ABC", "HR", "Manager", "Annie123@gmail.com", "APPROVED"}
        };

        // Write to CSV
        CSVLoader.write(filePath, data1);

        // Optional: read back and print
        String[][] readData = CSVLoader.read(filePath);
        for (String[] row : readData) {
            System.out.println(String.join(" | ", row));
        }*/

        //main function
        String filePath = "data/sample_company_representative_list.csv";
        String[][] data = CSVLoader.read(filePath)
        List<CompanyRepresentative> employees = new ArrayList<>();

        // Skip header (start at i = 1)
        for (int i = 1; i < data.length; i++) {
            String id = data[i][0];
            String name = data[i][1];
            Company company = data[i][2];
            String depart = data[i][3];
            String position = data[i][4];
            String email = data[i][5];
            String status = data[i][6];  // last column

            // ✅ Only add if approved
            if (status.equalsIgnoreCase("approved")) {
                CompanyRepresentative emp = new CompanyRepresentative(id, name, company,depart,position,email);
                employees.add(emp);
            }
        }

        // Print who was added
        System.out.println("Approved employees:");
        for (CompanyRepresentative emp : employees) {
            emp.printInfo();
        }
    }
}
