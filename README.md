# Internship Placement Management System

The Internship Placement Management System is developed as part of the **SC2002 - Object Oriented Design & Programming** course at NTU. This system streamlines the internship management process by providing a centralized platform where:

- **Students** can browse and apply for internship opportunities
- **Company Representatives** can post and manage internship listings
- **Career Staff** can oversee the entire placement process

## Project Architecture

This project follows the **Model-View-Controller (MVC)** architectural pattern for clear separation of concerns:

- **model/** - Data entities and enums (User, Internship, Application, etc.)
- **controller/** - Business logic and coordination (StudentController, etc.)
- **view/** - User interface and display (StudentView, MainView, etc.)
- **util/** - Helper classes for file I/O, validation, date handling
- **main/** - Application entry point
- **data/** - CSV files for data storage
- **docs/** - Project documentation, diagrams, and reports

## Getting Started

### Prerequisites

- **Java Development Kit (JDK)** 8 or higher
- **Java IDE** (IntelliJ IDEA, Eclipse, or VS Code recommended)
- Basic understanding of Java and Object-Oriented Programming

### Installation

1. **Clone the repository**

   ```bash
   git clone https://github.com/saamuul/internship-placement-management-system.git
   ```

2. **Navigate to the project directory**

   ```bash
   cd "internship-placement-management-system"
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

## Authors

| Name          | Contact                                 |
| :------------ | :-------------------------------------- |
| Matthew Loho  | [mtlh01p](https://github.com/mtlh01p)   |
| Goh Pei Han   | [Erence](https://github.com/Erence)     |
| Lai Kah Seng  | [ExFlameZ](https://github.com/ExFlameZ) |
| Lim Xiao Xuan | [erthiee](https://github.com/erthiee)   |
| Samuel Chan   | [saamuul](https://github.com/saamuul)   |

## License

This project is developed for educational purposes as part of the SC2002 course at NTU.

---

<div>
  <p><strong>Built with ❤️ by SCEA Group 2</strong></p>
  <p>Nanyang Technological University • 2025</p>
</div>
