# 🎓 Internship Placement Management System

[![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.java.com/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg?style=for-the-badge)](LICENSE)

A comprehensive Java-based system for managing internship placements at Nanyang Technological University (NTU). This application facilitates the entire internship lifecycle, from opportunity posting to placement management, serving students, career staff, and company representatives.

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [System Architecture](#system-architecture)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Running the Application](#running-the-application)
- [User Roles](#user-roles)
- [Project Structure](#project-structure)
- [Data Management](#data-management)
- [Contributing](#contributing)
- [Authors](#authors)
- [License](#license)

## 🎯 Overview

The Internship Placement Management System is developed as part of the **SC2002 - Object Oriented Design & Programming** course at NTU. This system streamlines the internship management process by providing a centralized platform where:

- **Students** can browse and apply for internship opportunities
- **Company Representatives** can post and manage internship listings
- **Career Staff** can oversee the entire placement process

## ✨ Features

### For Students

- 🔍 Browse available internship opportunities
- 📝 Apply for internships with customizable filters
- 📊 Track application status
- 🔐 Secure authentication and profile management
- 🔑 Password management

### For Company Representatives

- ➕ Post new internship opportunities
- ✏️ Edit and manage existing listings
- 👥 View and review student applications
- 📈 Track internship placements
- 🏢 Manage company profile

### For Career Staff

- 🎛️ Administrative oversight of all internships
- 📋 Manage student and company representative accounts
- 📊 Generate reports and analytics
- ⚙️ System configuration and maintenance

## 🏗️ System Architecture

The application follows the **Model-View-Controller (MVC)** architectural pattern:

```
┌─────────────┐      ┌──────────────┐      ┌─────────────┐
│    View     │ ───▶ │  Controller  │ ───▶ │    Model    │
│  (UI Layer) │ ◀─── │   (Logic)    │ ◀─── │   (Data)    │
└─────────────┘      └──────────────┘      └─────────────┘
```

### Key Components

- **Models**: Core business entities (User, Student, Internship, Company, etc.)
- **Views**: User interface components for different user roles
- **Controllers**: Business logic and user interaction handling
- **Utilities**: Helper classes for data loading and processing

## 🚀 Getting Started

### Prerequisites

- **Java Development Kit (JDK)** 8 or higher
- **Java IDE** (IntelliJ IDEA, Eclipse, or VS Code recommended)
- Basic understanding of Java and Object-Oriented Programming

### Installation

1. **Clone the repository**

   ```bash
   git clone https://github.com/saamuul/sc2002-scea-group2.git
   cd sc2002-scea-group2
   ```

2. **Navigate to the project directory**

   ```bash
   cd "sc2002-scea-group2"
   ```

3. **Compile the Java files**
   ```bash
   javac -d bin src/edu/ntu/ccds/sc2002/internship/**/*.java
   ```

### Running the Application

```bash
java -cp bin edu.ntu.ccds.sc2002.internship.main.InternshipSystem
```

Alternatively, open the project in your preferred Java IDE and run `InternshipSystem.java`.

## 👥 User Roles

### Student

- Default password: `password`
- Can browse and apply for internships
- Manage personal profile and applications

### Company Representative

- Default password: `password`
- Post and manage internship opportunities
- Review student applications

### Career Staff

- Default password: `password`
- Full administrative access
- Oversee all system operations

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

## 💾 Data Management

The system uses CSV files for data persistence, located in the `data/` directory:

- **`sample_student_list.csv`**: Student account information
- **`sample_company_representative_list.csv`**: Company representative accounts
- **`sample_staff_list.csv`**: Career staff accounts

The `CSVLoader` utility class handles loading and parsing of these data files.

## 🤝 Contributing

This is an academic project for SC2002. Contributions are limited to group members:

1. Create a feature branch
2. Make your changes
3. Submit a pull request for review

## 👨‍💻 Authors

**SCEA Group 2**

- SC2002 - Object Oriented Design & Programming
- Nanyang Technological University
- Academic Year 2024/2025

## 📄 License

This project is developed for educational purposes as part of the SC2002 course at NTU.

---

<div align="center">
  <p><strong>Built with ❤️ by SCEA Group 2</strong></p>
  <p>Nanyang Technological University • 2024-2025</p>
</div>
