package edu.ntu.ccds.sc2002.internship.model;

import java.util.ArrayList;

public class Company {
    private String name;
    private int companyId;
    private ArrayList<CompanyRepresentative> repList;

    public Company(String name, int companyId) {
        this.name = name;
        this.companyId = companyId;
        this.repList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getCompanyId() {
        return companyId;
    }

    public ArrayList<CompanyRepresentative> getRepList() {
        return repList;
    }

    public void addRep(CompanyRepresentative person) {
        repList.add(person);
    }

}