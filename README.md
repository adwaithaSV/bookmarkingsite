# 🌐 Personal Bookmarking Site

A simple yet robust web application built with **Spring Boot** that allows users to manage their personal collection of URLs.  
Users can **sign up**, **log in**, **add**, **view**, **edit**, and **delete** their bookmarks — with a built-in limit of **5 bookmarks per user**.

---

## ✨ Features

### 🔐 User Authentication
- Sign up for a new account  
- Secure password hashing with **BCrypt**  
- Login and Logout functionality  

### 🔖 Bookmark Management
- Add, view, edit, and delete bookmarks  
- Limit of **5 bookmarks per user** (error shown if limit exceeded)  
- Paginated list of bookmarks with **title**, **URL**, and **creation time**  
- Search bookmarks by **title** or **URL**  

### 💻 Responsive UI
- Designed with **Tailwind CSS** for a clean, modern, and adaptive layout  

---

## 🚀 Technologies Used

### Backend
- **Spring Boot** – Main application framework  
- **Spring Web (MVC)** – Controllers and routing  
- **Spring Data JPA** – Data access layer  
- **Hibernate** – ORM implementation  
- **Spring Security** – Authentication and authorization  
- **Jakarta Validation** – Server-side validation  
- **MySQL** – Relational database  

### Frontend
- **Thymeleaf** – Template engine for dynamic pages  
- **Tailwind CSS** – Responsive and utility-first styling  
- **JavaScript** – Client-side logic (e.g., password confirmation modal)  
- **Font Awesome** – Icons  

### Build Tool
- **Maven** – Dependency and build management  

---

## 🏁 Getting Started

Follow these steps to get the project running locally for development and testing.

### ✅ Prerequisites
- **Java 21** or higher  
- **Maven** installed (or bundled with your IDE)  
- **MySQL Database Server** (via XAMPP, Docker, or local installation)

---

## ⚙️ Installation & Setup

### 1. Clone the Repository
```bash
git clone https://github.com/adwaithaSV/bookmark.git
cd bookmark
```

### 2. Database Setup (MySQL)
 
Create Database:
Open your MySQL client (e.g., MySQL Workbench, command line) and create a new database named bookmark_db:
```bash
CREATE DATABASE bookmark_db;
```
#### Configure application.properties:

Open src/main/resources/application.properties and ensure your MySQL connection details are correct. The ddl-auto=create will automatically create tables on startup.
```bash
server.port=8080
spring.datasource.url=jdbc:mysql://localhost:3306/bookmark_db
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_ROOT_PASSWORD_HERE # <--- IMPORTANT: Replace with your actual MySQL password
spring.h2.console.enabled=false
spring.h2.console.path=/h2-console
spring.jpa.hibernate.ddl-auto=create # This will drop and recreate tables on every app restart
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.thymeleaf.cache=false
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html
```
Note: spring.jpa.hibernate.ddl-auto=create is useful for development as it ensures a clean database schema on every restart. For production, you would typically use update or none and manage schema changes via migrations.

### 3. Build the Project
Open your terminal in the bookmark project root directory and run:
```bash
mvn clean install
```
This command will download all necessary dependencies and build the project.

### 4. Run the Application
   
You can run the Spring Boot application from your IDE or using Maven:

From IDE: Locate BookmarkApplication.java (in src/main/java/com/example/bookmark/) and run it as a Spring Boot App.
From Terminal: In the project root, run:
```bash
mvn spring-boot:run
```
The application will start on http://localhost:8080.

## 🖥️ Usage
### Access the Application:
Open your web browser and navigate to http://localhost:8080/signup.

Sign Up:

Fill in a Username and Password.

Confirm your Password.

Click "Sign Up". You will be redirected to the login page.

Log In:

Enter the Username and Password you just registered.

Click "Log In". You will be redirected to the /bookmarks page.

Manage Bookmarks:

Add New Bookmark: Click the "Add New Bookmark" button. Fill in the title and URL, then save. Remember, there's a limit of 5 bookmarks per user!

View Bookmarks: The /bookmarks page lists your bookmarks with pagination.

Search: Use the search bar to filter bookmarks by title or URL.

Edit Bookmark: Click the "Edit" link next to a bookmark to modify its title or URL.

Delete Bookmark: Click the "Delete" button next to a bookmark to remove it.

Log Out:

Click the "Logout" button in the header to end your session.

## Future Enhancements
Robust Validation: Add more comprehensive server-side validation for URL format, title length, etc., using jakarta.validation annotations and handling errors in controllers.

Custom Modals: Replace the JavaScript confirm() for delete actions with a more styled, custom modal.

User Profile Page: Allow users to view/edit their profile information.

Role-Based Features: Implement different features or access levels for users with ROLE_ADMIN (e.g., an admin dashboard to manage all users/bookmarks).

Improved UI/UX: Enhance the overall look and feel, add animations, and improve user feedback.

Testing: Add comprehensive unit and integration tests for all layers of the application.

Error Handling: Implement custom error pages and more graceful error handling.

Security Hardening: Review Spring Security best practices for production (e.g., enable CSRF, proper session management).
