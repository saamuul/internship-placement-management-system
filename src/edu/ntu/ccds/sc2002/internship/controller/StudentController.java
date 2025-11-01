package edu.ntu.ccds.sc2002.internship.controller;

import edu.ntu.ccds.sc2002.internship.model.Student;

public class StudentController {

    // Controller to control application for internship
    public void applyForInternship(Student student, String internshipID) {
        student.applyForInternship(internshipID);
    }

}