package ResumeBuilder;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.nio.file.Path;

public class AppContext {
    private static UserProfile currentUserProfile;
    private static Map<String, String> userResumes = new HashMap<>();

    public static Map<String, String> getUserResumes() {
        return userResumes;
    }

    public static void setUserResumes(Map<String, String> resumes) {
        userResumes = resumes;
    }

    public static UserProfile getCurrentUserProfile() {
        return currentUserProfile;
    }

    public static void setCurrentUserProfile(UserProfile profile) {
        currentUserProfile = profile;
    }
    
    public static String getResumeData(String username) {
        return userResumes.getOrDefault(username, "<html><body></body></html>"); // Provide a default empty HTML if none found
    }
    
    public static void setResumeData(String username, String resumeData) {
        userResumes.put(username, resumeData);
        saveUserResumes(); // Save changes after updating resume data
    }
    
    public static void transferResumeData(String oldUsername, String newUsername) {
        String resumeData = getResumeData(oldUsername);
        if (resumeData != null) {
            setResumeData(newUsername, resumeData);
            userResumes.remove(oldUsername); // Remove old username's resume data
            saveUserResumes();
        }
    }
    
    public static void loadUserResumes() {
        try {
            Path path = Paths.get("userResumes.json");
            if (Files.exists(path)) {
                String content = new String(Files.readAllBytes(path));
                JSONObject jsonObject = new JSONObject(content);

                for (String key : jsonObject.keySet()) {
                    userResumes.put(key, jsonObject.getString(key));
                }
            } else {
                // If the file doesn't exist, initialize an empty map
                userResumes = new HashMap<>();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void saveUserResumes() {
        JSONObject jsonObject = new JSONObject();
        for (Map.Entry<String, String> entry : userResumes.entrySet()) {
            jsonObject.put(entry.getKey(), entry.getValue());
        }

        try {
            Files.write(Paths.get("userResumes.json"), jsonObject.toString(4).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
