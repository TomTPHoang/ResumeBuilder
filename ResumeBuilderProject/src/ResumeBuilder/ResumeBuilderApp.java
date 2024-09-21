/*
 *  Resume Builder Project
 *  Runtime Terror
 *  COSC 3324.001
 *  Version: 2.0
 *  10/26/2023
 *  
 *  Purpose: The purpose of this program is to provide
 *  users with an innovative way of creating and 
 *  managing a professional resume.
 */

package ResumeBuilder;

import java.awt.Dimension;
import javax.swing.*;

public class ResumeBuilderApp {

	public static void main(String[] args) {
	    SwingUtilities.invokeLater(() -> {
	        try {
	            // Load user data and resumes here
	            UserProfileDatabase.loadUserProfiles();
	            AppContext.loadUserResumes();

	            JFrame frame = ResumeBuilderUI.createMainFrame();
	            frame.add(ResumeBuilderUI.createInitialPanel(frame));
	            frame.setPreferredSize(new Dimension(300, 250));
	            frame.pack();
	            frame.setLocationRelativeTo(null);
	            frame.setVisible(true);
	        } catch (Exception e) {
	            e.printStackTrace();
	            // Handle exceptions and possibly alert the user
	        }
	    });
	}
}