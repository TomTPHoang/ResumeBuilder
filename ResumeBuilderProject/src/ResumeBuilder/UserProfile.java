package ResumeBuilder;

public class UserProfile {
	// Initializing attributes
	private String username;
	private String password;
	private String resumeData;
	
	// Constructor
	public UserProfile(String username, String password) {
		this.username = username;
		this.password = password;
		this.resumeData = "";
	}
	
    // Getters and setters for username and password
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getResumeData() {
		return resumeData;
	}
	
	public void setUsername(String newUsername) {
		if (!newUsername.equals(username)) {
	        // Remove the old username from the userProfiles
	        UserProfileDatabase.removeUserProfile(username);

	        // Get the resume data associated with the old username
	        String resumeData = AppContext.getResumeData(username);

	        // Update the username and set the resume data for the new username
	        AppContext.transferResumeData(username, newUsername);
	        username = newUsername;

	        // Set the resume data for the new username
	        AppContext.setResumeData(newUsername, resumeData);

	        // Add the new username and password to the userProfiles
	        UserProfileDatabase.addUserProfile(newUsername, password);
	    }
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setResumeData(String resumeData) {
		this.resumeData = resumeData;
	}
}
