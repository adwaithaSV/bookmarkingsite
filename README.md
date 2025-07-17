Personal Bookmarking Site
A simple yet robust web application built with Spring Boot that allows users to manage their personal collection of URLs. Users can sign up, log in, add, view, edit, and delete their bookmarks, with a built-in limit of 5 bookmarks per user.

✨ Features
User Authentication:
User can sign up for a new account.
Logged-in users can log out of the website.
Secure password hashing using BCrypt.

Bookmark Management:
Logged-in users can add a URL and a title for their bookmarks.
Bookmark Limit: A logged-in user can add a maximum of 5 bookmarks. An error message is displayed if the limit is exceeded.
Logged-in users can view a paginated list of their URLs, along with the title and the time they were added.
Users can search for a particular title or URL within their bookmarks.
Logged-in users can edit existing URLs and their titles.
Logged-in users can delete their bookmarks.
Responsive UI: Designed with Tailwind CSS for a clean and adaptive user experience on various devices.

🚀 Technologies Used
Backend:
Spring Boot: Framework for building robust, stand-alone, production-grade Spring applications.
Spring Web (Spring MVC): For building the web layer (controllers, request mapping).
Spring Data JPA: For simplified data access and persistence with Hibernate.
Hibernate: JPA implementation for ORM (Object-Relational Mapping).
Spring Security: For authentication (user signup, login, logout) and authorization.
Jakarta Validation: For server-side form validation.
MySQL: Relational database for storing user and bookmark data.

Frontend:
Thymeleaf: Server-side template engine for rendering dynamic HTML content.
Tailwind CSS: A utility-first CSS framework for rapid UI development and responsive design.
JavaScript: For client-side interactions (e.g., password confirmation modal).
Font Awesome: For icons.

Build Tool:
Maven: Project management and comprehension tool.

📁 Project Structure
bookmark/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── bookmark/
│   │   │               ├── BookmarkApplication.java       # Main Spring Boot application
│   │   │               ├── config/                        # Security configurations
│   │   │               │   └── SecurityConfig.java
│   │   │               ├── controller/                    # Handles web requests
│   │   │               │   ├── AuthenticationController.java
│   │   │               │   └── BookmarkController.java
│   │   │               ├── model/                         # JPA Entities
│   │   │               │   ├── User.java
│   │   │               │   ├── Role.java
│   │   │               │   └── Bookmark.java
│   │   │               ├── repository/                    # Spring Data JPA Repositories
│   │   │               │   ├── UserRepository.java
│   │   │               │   ├── RoleRepository.java
│   │   │               │   └── BookmarkRepository.java
│   │   │               ├── service/                       # Business logic
│   │   │               │   ├── UserService.java
│   │   │               │   └── BookmarkService.java
│   │   │               └── dto/                           # Data Transfer Objects
│   │   │                   ├── RegistrationDTO.java
│   │   │                   └── LoginResponseDTO.java
│   │   │
│   │   └── resources/
│   │       ├── static/
│   │       │   └── css/
│   │       │       └── style.css                          # Custom CSS
│   │       │
│   │       ├── templates/
│   │       │   ├── login.html                           # Login page
│   │       │   ├── signup.html                          # Registration page
│   │       │   ├── bookmark-list.html                   # Bookmark listing page
│   │       │   ├── bookmark-form.html                   # Add/Edit bookmark form
│   │       │   └── partials/                            # Reusable HTML fragments
│   │       │       ├── header.html
│   │       │       └── footer.html
│   │       │
│   │       └── application.properties                   # Application configuration
│   │
│   └── test/
│       └── java/
│           └── com/
│               └── example/
│                   └── bookmark/
│                       └── BookmarkApplicationTests.java
│
└── pom.xml                                            # Maven project file

🏁 Getting Started
Follow these instructions to get a copy of the project up and running on your local machine for development and testing purposes.

Prerequisites
Java 21 or higher
Maven (usually comes with your IDE or can be installed separately)

MySQL Database Server running locally (e.g., via XAMPP, Docker, or direct installation)

1. Clone the Repository
git clone https://github.com/your-username/bookmark.git
cd bookmark

2. Database Setup (MySQL)
Create Database:
Open your MySQL client (e.g., MySQL Workbench, command line) and create a new database named bookmark_db:
CREATE DATABASE bookmark_db;

Configure application.properties:
Open src/main/resources/application.properties and ensure your MySQL connection details are correct. The ddl-auto=create will automatically create tables on startup.

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

Note: spring.jpa.hibernate.ddl-auto=create is useful for development as it ensures a clean database schema on every restart. For production, you would typically use update or none and manage schema changes via migrations (e.g., Flyway, Liquibase).

3. Build the Project
Open your terminal in the bookmark project root directory and run:
mvn clean install
This command will download all necessary dependencies and build the project.

4. Run the Application
You can run the Spring Boot application from your IDE or using Maven:

From IDE: Locate BookmarkApplication.java (in src/main/java/com/example/bookmark/) and run it as a Spring Boot App.
From Terminal: In the project root, run:
mvn spring-boot:run
The application will start on http://localhost:8080.

🖥️ Usage
Access the Application:
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

💡 Future Enhancements
Robust Validation: Add more comprehensive server-side validation for URL format, title length, etc., using jakarta.validation annotations and handling errors in controllers.
Custom Modals: Replace the JavaScript confirm() for delete actions with a more styled, custom modal.
User Profile Page: Allow users to view/edit their profile information.
Role-Based Features: Implement different features or access levels for users with ROLE_ADMIN (e.g., an admin dashboard to manage all users/bookmarks).
Improved UI/UX: Enhance the overall look and feel, add animations, and improve user feedback.
Testing: Add comprehensive unit and integration tests for all layers of the application.
Error Handling: Implement custom error pages and more graceful error handling.
Security Hardening: Review Spring Security best practices for production (e.g., enable CSRF, proper session management).
