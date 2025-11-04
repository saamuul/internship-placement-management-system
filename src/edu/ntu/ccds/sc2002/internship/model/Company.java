package edu.ntu.ccds.sc2002.internship.model;

import java.util.ArrayList;

public class Company {
    private String name;
    private int companyID;
    private ArrayList<CompanyRepresentative> repList;

    public Company(String name, int companyID) {
        this.name = name;
        this.companyID = companyID;
        this.repList = new ArrayList<>();
    }

    public String getName() {return name;}
    public int getCompanyID() {return companyID;}
    public ArrayList<CompanyRepresentative> getRepList() {return repList;}

    public void addRep(CompanyRepresentative person) { repList.add(person); }

}