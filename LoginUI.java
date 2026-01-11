import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LoginUI extends JFrame {
    private JTextField userField = new JTextField(15);
    private JPasswordField passField = new JPasswordField(15);
    private JButton loginBtn = new JButton("Login");
    private JButton regBtn = new JButton("Register");

    public LoginUI() {
        setTitle("Snake Game - Login");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 2, 5, 5));
        setLocationRelativeTo(null);

        add(new JLabel(" Username:")); add(userField);
        add(new JLabel(" Password:")); add(passField);
        add(loginBtn); add(regBtn);

        loginBtn.addActionListener(e -> handleLogin());
        regBtn.addActionListener(e -> handleRegister());

        setVisible(true);
    }

    private void handleLogin() {
        String user = userField.getText();
        String pass = UserManager.hashPassword(new String(passField.getPassword()));
        List<User> users = UserManager.loadUsers();

        for (User u : users) {
            if (u.getUsername().equals(user) && u.getPasswordHash().equals(pass)) {
                dispose();
                new SnakeGame(u);
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Invalid credentials!");
    }

    private void handleRegister() {
        String user = userField.getText();
        String pass = new String(passField.getPassword());
        if (user.isEmpty() || pass.isEmpty()) return;

        List<User> users = UserManager.loadUsers();
        for (User u : users) {
            if (u.getUsername().equals(user)) {
                JOptionPane.showMessageDialog(this, "User already exists!");
                return;
            }
        }

        users.add(new User(user, UserManager.hashPassword(pass), 0));
        UserManager.saveUsers(users);
        JOptionPane.showMessageDialog(this, "Registration Successful!");
    }
}