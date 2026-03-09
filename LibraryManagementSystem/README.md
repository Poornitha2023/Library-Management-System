# Library Management System

A Java Swing desktop application for managing a college library — supports Admin and Student roles with book management, issue/return tracking, fine calculation, and notifications.

## Prerequisites

- **Java JDK 8+**
- **MySQL Server** running on `localhost:3306`
- **MySQL Connector/J** JAR (configured in `.vscode/settings.json`)

## Database Setup

Run the following SQL before first launch:

```sql
CREATE DATABASE IF NOT EXISTS librarydb;
USE librarydb;

CREATE TABLE IF NOT EXISTS books (
    book_id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    author VARCHAR(100),
    quantity INT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS students (
    student_id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    password VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS issues (
    book_id VARCHAR(20),
    student_id VARCHAR(20),
    issue_date DATE,
    return_date DATE,
    FOREIGN KEY (book_id) REFERENCES books(book_id),
    FOREIGN KEY (student_id) REFERENCES students(student_id)
);
```

## How to Run

```bash
javac LibraryManagementSystem.java
java LibraryManagementSystem
```

## Login Credentials

| Role    | Username | Password  |
|---------|----------|-----------|
| Admin   | admin    | admin123  |
| Student | *(from DB)* | *(from DB)* |

## Features

### Admin
- Add / View Books
- Add / View Students
- Issue / Return Books

### Student
- View available books
- View issued books
- Notifications for due dates
- Check dues & fines
