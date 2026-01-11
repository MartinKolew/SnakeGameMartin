import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class SnakeGame extends JFrame {
    public SnakeGame(User user) {
        add(new GamePanel(user));
        setTitle("Snake Game - Player: " + user.getUsername());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}

class GamePanel extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int ALL_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    static final int DELAY = 100;

    final int[] x = new int[ALL_UNITS];
    final int[] y = new int[ALL_UNITS];
    int bodyParts = 6;
    int applesEaten;
    int appleX, appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    User currentUser;
    
    JButton restartBtn;

    GamePanel(User user) {
        this.currentUser = user;
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.setLayout(null);
        this.addKeyListener(new MyKeyAdapter());

        restartBtn = new JButton("Restart Game");
        restartBtn.setBounds(SCREEN_WIDTH / 2 - 75, SCREEN_HEIGHT / 2 + 80, 150, 40);
        restartBtn.setFocusable(false);
        restartBtn.setVisible(false);
        restartBtn.addActionListener(e -> restartGame());
        this.add(restartBtn);

        startGame();
    }

    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void restartGame() {
        bodyParts = 6;
        applesEaten = 0;
        direction = 'R';

        for (int i = 0; i < bodyParts; i++) {
            x[i] = 0;
            y[i] = 0;
        }
        restartBtn.setVisible(false);
        running = true;
        newApple();
        timer.start();
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
            g.setColor(Color.GRAY);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++) {
                g.setColor(Color.WHITE);
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }

            g.setColor(Color.WHITE);
            g.setFont(new Font("SansSerif", Font.BOLD, 20));
            g.drawString("Score: " + applesEaten, 10, 30);
            g.drawString("High Score: " + currentUser.getHighScore(), 10, 60);
        } else {
            gameOver(g);
        }
    }

    public void newApple() {
        appleX = new Random().nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = new Random().nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (direction) {
            case 'U': y[0] -= UNIT_SIZE; break;
            case 'D': y[0] += UNIT_SIZE; break;
            case 'L': x[0] -= UNIT_SIZE; break;
            case 'R': x[0] += UNIT_SIZE; break;
        }
    }

    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void checkCollisions() {
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }
        if (x[0] < 0 || x[0] >= SCREEN_WIDTH || y[0] < 0 || y[0] >= SCREEN_HEIGHT) {
            running = false;
        }
        if (!running) {
            timer.stop();
            UserManager.updateHighScore(currentUser.getUsername(), applesEaten);
            if (applesEaten > currentUser.getHighScore()) {
                currentUser.setHighScore(applesEaten);
            }
            restartBtn.setVisible(true);
        }
    }

    public void gameOver(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("SansSerif", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 30));
        String scoreText = "Final Score: " + applesEaten;
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString(scoreText, (SCREEN_WIDTH - metrics2.stringWidth(scoreText)) / 2, SCREEN_HEIGHT / 2 + 50);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT: if (direction != 'R') direction = 'L'; break;
                case KeyEvent.VK_RIGHT: if (direction != 'L') direction = 'R'; break;
                case KeyEvent.VK_UP: if (direction != 'D') direction = 'U'; break;
                case KeyEvent.VK_DOWN: if (direction != 'U') direction = 'D'; break;
            }
        }
    }
}