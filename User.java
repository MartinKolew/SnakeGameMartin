public class User {
    private String username;
    private String passwordHash;
    private int highScore;

    public User(String username, String passwordHash, int highScore) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.highScore = highScore;
    }

    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public int getHighScore() { return highScore; }
    public void setHighScore(int highScore) { this.highScore = highScore; }

    @Override
    public String toString() {
        return username + ":" + passwordHash + ":" + highScore;
    }
}