## 🏗️ System Architecture

The application follows the **Model-View-Controller (MVC)** architectural pattern.

```
┌─────────────┐      ┌──────────────┐      ┌─────────────┐
│    View     │ ───▶ │  Controller  │ ───▶ │    Model    │
│  (UI Layer) │ ◀─── │   (Logic)    │ ◀─── │   (Data)    │
└─────────────┘      └──────────────┘      └─────────────┘
```

Key components:

- **Models**: Core business entities (User, Student, Internship, Company, etc.)
- **Views**: User interface components for different user roles
- **Controllers**: Business logic and user interaction handling
- **Utilities**: Helper classes for data loading and processing
