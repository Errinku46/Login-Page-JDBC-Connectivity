<img width="591" height="774" alt="Screenshot 2026-06-09 190710" src="https://github.com/user-attachments/assets/568617c6-3a2a-409b-95a7-d27281a5d26e" />
# Java Login App – JDBC + MySQL

A complete Java Swing login system with MySQL database connectivity via JDBC.

---

## Project Structure

```
LoginApp/
├── src/
│   └── com/login/
│       ├── Main.java                 ← Entry point
│       ├── model/
│       │   └── User.java             ← User POJO
│       ├── util/
│       │   ├── DBConnection.java     ← JDBC connection manager
│       │   └── PasswordUtil.java     ← SHA-256 hashing
│       ├── dao/
│       │   └── UserDAO.java          ← DB queries (auth + register)
│       └── ui/
│           ├── LoginFrame.java       ← Main login window
│           └── RegisterDialog.java   ← New-user registration dialog
├── sql/
│   └── schema.sql                    ← DB schema + seed data
├── lib/                              ← Place MySQL JDBC .jar here
├── build.sh                          ← Compile + run script
└── README.md
```

---

## Prerequisites

| Tool | Version |
|------|---------|
| JDK  | 17 +    |
| MySQL | 8.0 +  |
| MySQL Connector/J | 8.x or 9.x |

---

## Setup (step-by-step)

### 1 — Create the database

```bash
mysql -u root -p < sql/schema.sql
```

This creates `login_db` with a `users` table and two demo accounts.

### 2 — Add the JDBC driver

Download **MySQL Connector/J** from:  
https://dev.mysql.com/downloads/connector/j/

Place the downloaded `.jar` (e.g. `mysql-connector-j-9.2.0.jar`) inside the `lib/` folder.

### 3 — Configure the connection

Open `src/com/login/util/DBConnection.java` and update:

```java
private static final String DB_URL      = "jdbc:mysql://localhost:3306/login_db?useSSL=false&serverTimezone=UTC";
private static final String DB_USER     = "root";
private static final String DB_PASSWORD = "your_password";   // ← your MySQL password
```

### 4 — Build and run

```bash
chmod +x build.sh
./build.sh
```

**Windows:**
```cmd
mkdir out
dir /s /b src\*.java > sources.txt
javac -cp "lib\mysql-connector-j-*.jar" -d out @sources.txt
java -cp "out;lib\mysql-connector-j-*.jar" com.login.Main
```

---

## Demo Credentials

| Username | Password  | Role  |
|----------|-----------|-------|
| rinku    | 052002    | user |
| shahji   | naveen31  | user  |

---

## Features

- **Swing GUI** — dark-themed login form with field-focus highlighting  
- **PreparedStatement** — SQL injection proof  
- **SHA-256 hashing** — passwords never stored in plain text  
- **SwingWorker** — DB calls run off the Event Dispatch Thread (no UI freeze)  
- **Register dialog** — create new accounts from within the app  
- **Last login tracking** — `last_login` column updated on every successful sign-in  

---

## Extending the project

- Replace SHA-256 with **BCrypt** (add `jbcrypt-0.4.jar` to `lib/`)  
- Add a **"Forgot password"** flow with email token  
- Replace Swing with **JavaFX** for a more modern UI  
- Add **connection pooling** (HikariCP) for production use  
