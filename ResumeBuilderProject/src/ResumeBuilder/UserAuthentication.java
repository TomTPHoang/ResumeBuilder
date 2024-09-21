package ResumeBuilder;

import javax.swing.*;

public class UserAuthentication {
	
	public static boolean handleLogin(String username, String password, JFrame parentFrame) {
        if (authenticateUser(username, password)) {
            JOptionPane.showMessageDialog(parentFrame, "Login successful!");
            // Additional logic to transition to the main app panel
            return true;
        } else {
            return false;
        }
    }
	
	public static boolean authenticateUser(String username, String password) {
        UserProfile userProfile = UserProfileDatabase.getUserProfile(username);
        if (userProfile != null && userProfile.getPassword().equals(password)) {
            return true; // Authentication successful
        }
        return false; // Authentication failed
    }
}