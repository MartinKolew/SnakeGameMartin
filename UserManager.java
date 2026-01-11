import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private static final String FILE_NAME = "users.txt";

    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedhash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public static List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (!file.exists()) return users;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 3) {
                    users.add(new User(parts[0], parts[1], Integer.parseInt(parts[2])));
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
        return users;
    }

    public static void saveUsers(List<User> users) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (User u : users) {
                bw.write(u.toString());
                bw.newLine();
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static boolean userExists(String username) {
        return loadUsers().stream().anyMatch(u -> u.getUsername().equalsIgnoreCase(username));
    }

    public static void updateHighScore(String username, int newScore) {
        List<User> users = loadUsers();
        for (User u : users) {
            if (u.getUsername().equals(username) && newScore > u.getHighScore()) {
                u.setHighScore(newScore);
            }
        }
        saveUsers(users);
    }
}