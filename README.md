# Snake Game

A high-performance, classic Snake game built with **Java Swing**. This version features a complete user authentication system and persistent high scores.

## Features

- **User Authentication:** 
  - Register with a unique username and password.
  - Secure login system.
  - Password security using **SHA-256 hashing** (no plain-text passwords stored).
  - "Username Unavailable" validation logic.
- **Persistent Progress:** 
  - Player data and high scores are saved to a local database (`users.txt`).
  - Your "Personal Best" is displayed on the main menu and game over screen.
- **Visuals:**
  - **Zebra Snake:** The snake features a realistic white and black striped pattern.
  - **Stone Floor:** A textured grid background for a more realistic environment.
  - **Animated UI:** Smooth zoom-in/out animations on the main menu.
- **Responsive Controls:** 
  - Supports both **WASD** and **Arrow Keys**.
  - Optimized keyboard focus for lag-free gameplay.

## How to Play

1. **Register:** Create an account from the main screen.
2. **Login:** Access the game menu using your credentials.
3. **Start:** Hit the "START GAME" button.
4. **Objective:** Eat the grey apples to grow longer and increase your score.
5. **Game Over:** The game ends if you hit the wall or your own tail. Your high score will be saved automatically!

## Technical Details

- **Language:** Java
- **Framework:** Swing / AWT
- **Architecture:** Modular (Separated logic for Data Models, Security, and UI)
- **Persistence:** File-based storage (CSV format)
- **Encryption:** SHA-256 MessageDigest

## Installation & Running

1. **Prerequisites:** 
   - Ensure you have **JDK 8** or higher installed.
   - A Java IDE (VS Code, IntelliJ, or Eclipse).

2. **Setup:**
   - Clone the repository:
     ```bash
     git clone https://github.com/MartinKolew/SnakeGameMartin.git
     ```
   - Ensure all `.java` files are in the same directory.

3. **Run via Terminal:**
   ```bash
   javac Main.java
   java Main
