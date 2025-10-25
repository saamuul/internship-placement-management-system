## 📁 Project Structure

```
sc2002-scea-group2/
│
├── src/
│   └── edu/ntu/ccds/sc2002/internship/
│       ├── controller/          # Business logic controllers
│       │   ├── AuthController.java
│       │   ├── StudentController.java
│       │   ├── CompanyRepController.java
│       │   └── CareerStaffController.java
│       │
│       ├── model/               # Domain models
│       │   ├── User.java
│       │   ├── Student.java
│       │   ├── CompanyRepresentative.java
│       │   ├── CareerStaff.java
│       │   ├── Internship.java
│       │   ├── InternshipOpportunity.java
│       │   ├── Company.java
│       │   └── Filter.java
│       │
│       ├── view/                # User interface
│       │   ├── MainView.java
│       │   ├── StudentView.java
│       │   ├── CompanyRepView.java
│       │   └── CareerStaffView.java
│       │
│       ├── util/                # Utility classes
│       │   └── CSVLoader.java
│       │
│       └── main/                # Application entry point
│           └── InternshipSystem.java
│
├── data/                        # Sample data files
│   ├── sample_student_list.csv
│   ├── sample_company_representative_list.csv
│   └── sample_staff_list.csv
│
├── docs/                        # Documentation
│
└── README.md
```
