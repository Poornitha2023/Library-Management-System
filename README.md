# 📚 Library Management System

A robust and user-friendly Library Management System built with **Java Swing** and **MySQL**. This application streamlines library operations for both administrators and students.

---

## 🚀 Features

- **Admin Dashboard:** Manage books, students, and track issued items.
- **Student Portal:** Search for books, view borrowing history, and check fines.
- **Automated Fine Calculation:** Built-in logic for overdue returns.
- **Conflict Detection:** Ensures books aren't over-issued beyond stock levels.

---

## 🛠️ Installation & Setup

### 1. Prerequisites

Make sure the following are installed:

- **JDK:** Java Development Kit (JDK) 8 or higher  
- **MySQL:** MySQL Server installed and running  
- **Connector:** MySQL Connector/J (placed in the `/lib` folder)

---

## 🗄️ Database Setup

Create the database and tables by running the following SQL:

```sql
CREATE DATABASE IF NOT EXISTS librarydb;
USE librarydb;

CREATE TABLE IF NOT EXISTS books (
    book_id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    author VARCHAR(100) NOT NULL,
    quantity INT DEFAULT 0,
    category VARCHAR(50),
    added_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS students (
    student_id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    password VARCHAR(50) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(15),
    registered_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS issues (
    id INT AUTO_INCREMENT PRIMARY KEY,
    book_id VARCHAR(20),
    student_id VARCHAR(20),
    issue_date DATE NOT NULL,
    return_date DATE,
    due_date DATE NOT NULL,
    fine_amount DECIMAL(10,2) DEFAULT 0.00,
    status ENUM('issued', 'returned', 'overdue') DEFAULT 'issued',
    FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE
);

-- Default Admin Login
INSERT INTO students (student_id, name, password)
VALUES ('admin', 'Administrator', 'admin123');
```

---

## ⚙️ Configure Connection

Open **LibraryManagementSystem.java** and update your MySQL credentials:

```java
return DriverManager.getConnection(
    "jdbc:mysql://localhost:3306/librarydb",
    "root",         // Your MySQL username
    "password"      // Your MySQL password
);
```

---

## 🔄 Application Workflow

### 🔐 Login
Access the system using **Admin or Student credentials**.

### 👨‍💼 Admin Dashboard
Admins can:
- Manage the book inventory
- Register students
- Issue books
- Process returns
- Monitor overdue books

### 👩‍🎓 Student Portal
Students can:
- Browse available books
- View borrowing history
- Check issued books
- View fine details

### 💰 Return & Fine Calculation
When returning a book:
- The system checks the **due_date**
- Calculates **fine automatically** if the return is late.

---

## 📸 Application Screenshots

### 🔐 Login Page
<img width="1091" height="617" alt="image" src="https://github.com/user-attachments/assets/a6857c43-07a4-404b-add7-fd1c4bee9a03" />


### 👨‍💼 Admin Dashboard
<img width="927" height="629" alt="image" src="https://github.com/user-attachments/assets/96f9010d-84cd-4916-bb9c-359c6abb0e36" />


### 👩‍🎓 Student Portal
<img width="846" height="577" alt="image" src="https://github.com/user-attachments/assets/7f03eeb3-d55d-4e84-ae74-e32627d01066" />

### 📚 Add Book
<img width="927" height="634" alt="image" src="https://github.com/user-attachments/assets/13ee5ca5-627d-4a9d-9dda-6ab434c5ea6e" />

### 📚 Issue Book
<img width="929" height="634" alt="image" src="https://github.com/user-attachments/assets/55c3f861-6609-4c95-a815-39cd004a5f42" />


---

## 📁 Project Structure

```
LibraryManagementSystem/
├── LibraryManagementSystem.java  # Main application
├── README.md                     # Documentation
├── lib/                          # MySQL Connector JAR
└── database/                     # SQL Scripts
```

---

## 🔧 Configuration Settings

| Setting | Value |
|-------|-------|
| Database Name | librarydb |
| Daily Fine Rate | ₹2.00 |
| Grace Period | 7 Days |
| Default Port | 3306 |

---

## 📝 License

This project is licensed under the **MIT License**.

---

Made with ❤️ using **Java Swing & MySQL**

---

## 🚀 Final Upload Command

After saving the file, run this command in your terminal to overwrite the old repository with the new clean structure:

```bash
rd /s /q .git && git init && git add . && git commit -m "Final cleanup and restructure" && git remote add origin https://github.com/Poornitha2023/Library-Management-System.git && git branch -M main && git push -u origin main --force
```
