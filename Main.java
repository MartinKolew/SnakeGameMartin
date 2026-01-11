import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Random;

public class Main extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardLayout);
    private User currentUser;

    public Main() {
        setTitle("Snake Game - Martin Version"); 
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 650);
        setLocationRelativeTo(null);
        setResizable(false);

        mainPanel.add(new WelcomePanel(), "WELCOME");
        mainPanel.add(new LoginPanel(), "LOGIN");
        mainPanel.add(new RegisterPanel(), "REGISTER");

        add(mainPanel);
        setVisible(true);
    }

    private void styleButton(JButton b) {
        b.setFocusable(false);
        b.setBackground(new Color(60, 60, 60));
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Arial", Font.BOLD, 16));
        b.setPreferredSize(new Dimension(180, 45));
        b.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
    }

    class WelcomePanel extends JPanel {
        WelcomePanel() {
            setLayout(new GridBagLayout());
            setBackground(new Color(20, 20, 20));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 0, 10, 0);
            gbc.gridx = 0;
            JLabel title = new JLabel("SNAKE ADVENTURE");
            title.setFont(new Font("Arial", Font.BOLD, 40));
            title.setForeground(Color.WHITE);
            gbc.gridy = 0; add(title, gbc);
            JButton logBtn = new JButton("Login");
            JButton regBtn = new JButton("Register");
            styleButton(logBtn); styleButton(regBtn);
            gbc.gridy = 1; add(logBtn, gbc);
            gbc.gridy = 2; add(regBtn, gbc);
            logBtn.addActionListener(e -> cardLayout.show(mainPanel, "LOGIN"));
            regBtn.addActionListener(e -> cardLayout.show(mainPanel, "REGISTER"));
        }
    }

    class LoginPanel extends JPanel {
        JTextField userField = new JTextField(15);
        JPasswordField passField = new JPasswordField(15);
        LoginPanel() {
            setLayout(new GridBagLayout());
            setBackground(new Color(30, 30, 30));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.gridx = 0; gbc.gridy = 0;
            JLabel l1 = new JLabel("Username:"); l1.setForeground(Color.WHITE);
            add(l1, gbc);
            gbc.gridx = 1; add(userField, gbc);
            gbc.gridx = 0; gbc.gridy = 1;
            JLabel l2 = new JLabel("Password:"); l2.setForeground(Color.WHITE);
            add(l2, gbc);
            gbc.gridx = 1; add(passField, gbc);
            JButton loginBtn = new JButton("Login");
            styleButton(loginBtn);
            gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
            add(loginBtn, gbc);
            JButton backBtn = new JButton("Back");
            styleButton(backBtn);
            gbc.gridy = 3; add(backBtn, gbc);

            loginBtn.addActionListener(e -> {
                String user = userField.getText();
                String pass = UserManager.hashPassword(new String(passField.getPassword()));
                List<User> users = UserManager.loadUsers();
                for (User u : users) {
                    if (u.getUsername().equals(user) && u.getPasswordHash().equals(pass)) {
                        currentUser = u;
                        mainPanel.add(new GameMenu(), "MENU");
                        cardLayout.show(mainPanel, "MENU");
                        return;
                    }
                }
                JOptionPane.showMessageDialog(null, "Invalid Credentials");
            });
            backBtn.addActionListener(e -> cardLayout.show(mainPanel, "WELCOME"));
        }
    }

    class RegisterPanel extends JPanel {
        JTextField userF = new JTextField(15);
        JPasswordField passF = new JPasswordField(15);
        JPasswordField confF = new JPasswordField(15);
        RegisterPanel() {
            setLayout(new GridBagLayout());
            setBackground(new Color(30, 30, 30));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(8, 8, 8, 8);
            gbc.gridx = 0;
            JLabel header = new JLabel("Create Account");
            header.setFont(new Font("Arial", Font.BOLD, 24));
            header.setForeground(Color.WHITE);
            gbc.gridy = 0; gbc.gridwidth = 2; add(header, gbc);
            gbc.gridwidth = 1; gbc.gridy = 1; add(new JLabel("<html><font color='white'>Username:</font></html>"), gbc);
            gbc.gridx = 1; add(userF, gbc);
            gbc.gridx = 0; gbc.gridy = 2; add(new JLabel("<html><font color='white'>Password:</font></html>"), gbc);
            gbc.gridx = 1; add(passF, gbc);
            gbc.gridx = 0; gbc.gridy = 3; add(new JLabel("<html><font color='white'>Confirm Password:</font></html>"), gbc);
            gbc.gridx = 1; add(confF, gbc);
            JButton regBtn = new JButton("Register Now");
            styleButton(regBtn);
            gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; add(regBtn, gbc);
            JButton backBtn = new JButton("Back");
            styleButton(backBtn);
            gbc.gridy = 5; add(backBtn, gbc);

            regBtn.addActionListener(e -> {
                String u = userF.getText();
                String p1 = new String(passF.getPassword());
                String p2 = new String(confF.getPassword());
                if (u.isEmpty() || p1.isEmpty()) { JOptionPane.showMessageDialog(null, "Fields cannot be empty"); return; }
                if (!p1.equals(p2)) { JOptionPane.showMessageDialog(null, "Passwords do not match"); return; }
                if (UserManager.userExists(u)) { JOptionPane.showMessageDialog(null, "Username Unavailable"); return; }
                List<User> list = UserManager.loadUsers();
                list.add(new User(u, UserManager.hashPassword(p1), 0));
                UserManager.saveUsers(list);
                JOptionPane.showMessageDialog(null, "Success! Please Login.");
                cardLayout.show(mainPanel, "WELCOME");
            });
            backBtn.addActionListener(e -> cardLayout.show(mainPanel, "WELCOME"));
        }
    }

    class GameMenu extends JPanel {
        double scale = 1.0;
        boolean growing = true;
        GameMenu() {
            setLayout(new GridBagLayout());
            JButton startBtn = new JButton("START GAME");
            styleButton(startBtn);
            add(startBtn);

            startBtn.addActionListener(e -> {
                GamePanel gp = new GamePanel();
                mainPanel.add(gp, "GAME");
                cardLayout.show(mainPanel, "GAME");
                gp.requestFocusInWindow();
            });

            Timer zoomTimer = new Timer(50, e -> {
                if (growing) scale += 0.005; else scale -= 0.005;
                if (scale > 1.15) growing = false;
                if (scale < 0.95) growing = true;
                repaint();
            });
            zoomTimer.start();
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            
            g2d.setColor(new Color(0, 80, 0));
            g2d.setFont(new Font("Arial", Font.BOLD, 120));
            FontMetrics fm = g2d.getFontMetrics();
            int x = (getWidth() - fm.stringWidth("SNAKE")) / 2;
            int y = (getHeight() / 2) + 40;
            g2d.translate(getWidth()/2, getHeight()/2);
            g2d.scale(scale, scale);
            g2d.translate(-getWidth()/2, -getHeight()/2);
            g2d.drawString("SNAKE", x, y);
            
            g2d.setTransform(new java.awt.geom.AffineTransform());
            g2d.setColor(new Color(212, 175, 55));
            g2d.setFont(new Font("Arial", Font.BOLD, 25));
            String highText = "PERSONAL BEST: " + currentUser.getHighScore();
            int hx = (getWidth() - g2d.getFontMetrics().stringWidth(highText)) / 2;
            g2d.drawString(highText, hx, 100);
        }
    }

    class GamePanel extends JPanel implements ActionListener {
        static final int UNIT_SIZE = 25;
        final int[] x = new int[600*600/25];
        final int[] y = new int[600*600/25];
        int bodyParts = 6;
        int applesEaten, appleX, appleY;
        char direction = 'R';
        boolean running = false;
        boolean paused = false;
        Timer timer;
        JButton restartBtn, menuBtn;

        GamePanel() {
            setPreferredSize(new Dimension(600, 600));
            setFocusable(true);
            setLayout(null);
            addKeyListener(new MyKeyAdapter());
            restartBtn = new JButton("Restart");
            menuBtn = new JButton("Menu");
            styleButton(restartBtn); styleButton(menuBtn);
            restartBtn.setBounds(140, 420, 140, 45);
            menuBtn.setBounds(320, 420, 140, 45);
            restartBtn.setVisible(false); menuBtn.setVisible(false);
            restartBtn.addActionListener(e -> { restartGame(); requestFocusInWindow(); });
            menuBtn.addActionListener(e -> cardLayout.show(mainPanel, "MENU"));
            add(restartBtn); add(menuBtn);
            startGame();
        }

        void startGame() { newApple(); running = true; timer = new Timer(100, this); timer.start(); }
        void restartGame() {
            bodyParts = 6; applesEaten = 0; direction = 'R'; paused = false;
            for(int i=0; i<bodyParts; i++) { x[i] = 0; y[i] = 0; }
            restartBtn.setVisible(false); menuBtn.setVisible(false);
            running = true; timer.start(); repaint();
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (int i = 0; i < 600; i += UNIT_SIZE) {
                for (int j = 0; j < 600; j += UNIT_SIZE) {
                    g.setColor((i + j) % 50 == 0 ? new Color(45, 45, 45) : new Color(35, 35, 35));
                    g.fillRect(i, j, UNIT_SIZE, UNIT_SIZE);
                    g.setColor(new Color(25, 25, 25)); g.drawRect(i, j, UNIT_SIZE, UNIT_SIZE);
                }
            }
            if (running) {
                g.setColor(Color.GRAY); g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
                
                for (int i = 0; i < bodyParts; i++) {
                    g.setColor(Color.WHITE); g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                    if (i % 2 != 0) { g.setColor(Color.BLACK); g.fillRect(x[i]+5, y[i], 4, UNIT_SIZE); g.fillRect(x[i]+16, y[i], 4, UNIT_SIZE); }
                }

                g.setColor(Color.WHITE); g.setFont(new Font("Arial", Font.BOLD, 18));
                g.drawString("Score: " + applesEaten, 10, 25);
                g.drawString("High Score: " + currentUser.getHighScore(), 10, 50);

                g.setFont(new Font("Arial", Font.PLAIN, 14));
                String pauseHint = "SPACE: Pause";
                int hintWidth = g.getFontMetrics().stringWidth(pauseHint);
                g.drawString(pauseHint, 600 - hintWidth - 15, 25);

                if(paused) {
                    g.setColor(new Color(0, 0, 0, 150)); // Darken screen
                    g.fillRect(0, 0, 600, 600);
                    g.setColor(Color.WHITE);
                    g.setFont(new Font("Arial", Font.BOLD, 50));
                    FontMetrics fm = getFontMetrics(g.getFont());
                    g.drawString("PAUSED", (600 - fm.stringWidth("PAUSED")) / 2, 300);
                }
            } else { gameOver(g); }
        }

        void move() {
            for (int i = bodyParts; i > 0; i--) { x[i] = x[i - 1]; y[i] = y[i - 1]; }
            switch (direction) {
                case 'U': y[0] -= UNIT_SIZE; break;
                case 'D': y[0] += UNIT_SIZE; break;
                case 'L': x[0] -= UNIT_SIZE; break;
                case 'R': x[0] += UNIT_SIZE; break;
            }
        }
        void checkApple() { if (x[0] == appleX && y[0] == appleY) { bodyParts++; applesEaten++; newApple(); } }
        void newApple() { appleX = new Random().nextInt(23) * 25; appleY = new Random().nextInt(23) * 25; }
        void checkCollisions() {
            for (int i = bodyParts; i > 0; i--) if (x[0] == x[i] && y[0] == y[i]) running = false;
            if (x[0] < 0 || x[0] >= 600 || y[0] < 0 || y[0] >= 600) running = false;
            if (!running) {
                timer.stop();
                UserManager.updateHighScore(currentUser.getUsername(), applesEaten);
                if (applesEaten > currentUser.getHighScore()) currentUser.setHighScore(applesEaten);
                restartBtn.setVisible(true); menuBtn.setVisible(true);
            }
        }
        void gameOver(Graphics g) {
            g.setColor(new Color(200, 0, 0)); g.setFont(new Font("Arial", Font.BOLD, 60));
            FontMetrics fm = getFontMetrics(g.getFont());
            g.drawString("GAME OVER", (600 - fm.stringWidth("GAME OVER")) / 2, 220);

            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.setColor(Color.WHITE);
            String scoreText = "Current Score: " + applesEaten;
            g.drawString(scoreText, (600 - g.getFontMetrics().stringWidth(scoreText)) / 2, 300);

            g.setColor(new Color(212, 175, 55));
            String highText = "Your Best: " + currentUser.getHighScore();
            g.drawString(highText, (600 - g.getFontMetrics().stringWidth(highText)) / 2, 350);
        }

        @Override public void actionPerformed(ActionEvent e) { 
            if (running && !paused) { 
                move(); 
                checkApple(); 
                checkCollisions(); 
            } 
            repaint(); 
        }

        private class MyKeyAdapter extends KeyAdapter {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_P) {
                    if(running) paused = !paused;
                    return;
                }
                if(!paused) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_LEFT: case KeyEvent.VK_A: if (direction != 'R') direction = 'L'; break;
                        case KeyEvent.VK_RIGHT: case KeyEvent.VK_D: if (direction != 'L') direction = 'R'; break;
                        case KeyEvent.VK_UP: case KeyEvent.VK_W: if (direction != 'D') direction = 'U'; break;
                        case KeyEvent.VK_DOWN: case KeyEvent.VK_S: if (direction != 'U') direction = 'D'; break;
                    }
                }
            }
        }
    }
    public static void main(String[] args) { new Main(); }
}