package ResumeBuilder;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONArray;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class UserProfileDatabase {
	private static Map<String, UserProfile> userProfiles = new HashMap<>();
	private static final String FILE_PATH = "userProfiles.json";
	
	public static void loadUserProfiles() {
        try {
            String content = new String(Files.readAllBytes(Paths.get(FILE_PATH)));
            JSONArray jsonArray = new JSONArray(content);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                String username = jsonObj.getString("username");
                String password = jsonObj.getString("password");
                String resumeHtml = jsonObj.optString("resume", "<html><body><p>Your resume content goes here...</p></body></html>"); // Default content
                UserProfile userProfile = new UserProfile(username, password);
                userProfile.setResumeData(resumeHtml); // Set the HTML content
                userProfiles.put(username, userProfile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	public static void saveUserProfiles() {
        JSONArray jsonArray = new JSONArray();
        for (UserProfile userProfile : userProfiles.values()) {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("username", userProfile.getUsername());
            jsonObj.put("password", userProfile.getPassword()); // Remember to hash passwords in a real application
            jsonObj.put("resume", AppContext.getResumeData(userProfile.getUsername())); // Save the resume data as HTML string
            jsonArray.put(jsonObj);
        }

        try {
            Files.write(Paths.get(FILE_PATH), jsonArray.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static UserProfile getUserProfile(String username) {
        return userProfiles.get(username);
    }

    public static boolean userExists(String username) {
        return userProfiles.containsKey(username);
    }

    public static boolean addUserProfile(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            return false; // Username and password cannot be blank
        }

        if (userProfiles.containsKey(username)) {
            return false; // Username already exists
        }

        userProfiles.put(username, new UserProfile(username, password));
        saveUserProfiles(); // Save the updated user profiles to the file
        return true;
    }
    
    public static void removeUserProfile(String username) {
        if (userProfiles.containsKey(username)) {
            userProfiles.remove(username);
            saveUserProfiles(); // Save changes
        }
    }

    public static void updateUserProfile(UserProfile userProfile, String newUsername, String newPassword) {
        if (userProfile != null) {
            boolean needsUpdate = false;
            // Check if the username is changing
            if (!newUsername.equals(userProfile.getUsername())) {
                userProfile.setUsername(newUsername);
                needsUpdate = true;
            }
            if (!newPassword.equals(userProfile.getPassword())) {
                userProfile.setPassword(newPassword);
                needsUpdate = true;
            }
            if (needsUpdate) {
                userProfiles.put(newUsername, userProfile);
                saveUserProfiles(); // Save changes
            }
        }
    }
    
    public static void saveResumeData(String username, String htmlContent) {
        // Assuming you are saving to a JSON file, and each resume is associated with a username
        JSONObject resumes = new JSONObject();
        resumes.put(username, htmlContent);
        // Write JSON object to file
        try (FileWriter file = new FileWriter("resumes.json")) {
            file.write(resumes.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static String loadResumeData(String username) {
        // Assuming you are loading from a JSON file
        String htmlContent = "";
        try {
            JSONObject resumes = new JSONObject(new JSONTokener(new FileReader("resumes.json")));
            htmlContent = resumes.optString(username, "");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return htmlContent;
    }
}
