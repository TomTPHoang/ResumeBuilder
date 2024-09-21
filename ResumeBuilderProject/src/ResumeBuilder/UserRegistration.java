package ResumeBuilder;

import javax.swing.*;

public class UserRegistration {
	
	public static boolean handleRegistration(String username, String password, JFrame parentFrame) {
	    if (username.isEmpty() || password.isEmpty()) {	        
	        return false;
	    }

	    if (userExists(username)) {	        
	        return false;
	    }

	    if (registerUser(username, password)) {	        
	        return true;
	    } else {
	        return false;
	    }
	}
	
	public static boolean registerUser(String username, String password) {
	    if (username.isEmpty() || password.isEmpty()) {
	        return false; // Username and password cannot be blank
	    }

	    if (UserProfileDatabase.getUserProfile(username) != null) {
	        return false; // Username already exists
	    }

	    UserProfileDatabase.addUserProfile(username, password);
	    UserProfileDatabase.saveUserProfiles(); // Make sure to save the updated profiles list
	    return true;
	}
	
	public static boolean userExists(String username) {
        // Here you would check against the stored user profiles.
        // Assuming 'userProfiles' is a Map object holding user information.
        // This requires access to the 'userProfiles' data structure from 'UserProfileDatabase' class.
        return UserProfileDatabase.userExists(username);
    }
}
