package ResumeBuilder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserProfileEditor {
	public static void handleProfileEditing(JFrame parentFrame) {
    	UserProfile currentUserProfile = AppContext.getCurrentUserProfile();
        if (currentUserProfile != null) {
            editProfile(currentUserProfile);
        } else {
            JOptionPane.showMessageDialog(parentFrame, "Please log in first.");
        }
    }
	
	public static void editProfile(UserProfile userProfile) {
		JFrame editFrame = new JFrame("Edit Profile");
	    editFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	    JPanel editPanel = new JPanel();
	    editPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
	    editPanel.setLayout(new GridBagLayout());

	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.gridwidth = GridBagConstraints.REMAINDER;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    gbc.insets = new Insets(4, 4, 4, 4); // Margin around components

	    JLabel usernameLabel = new JLabel("Username:");
	    JTextField usernameField = new JTextField(userProfile.getUsername());
	    usernameField.setPreferredSize(new Dimension(200, 20)); // Set the preferred size
	    JLabel passwordLabel = new JLabel("Password:");
	    JPasswordField passwordField = new JPasswordField(userProfile.getPassword());
	    passwordField.setPreferredSize(new Dimension(200, 20)); // Set the preferred size

	    editPanel.add(usernameLabel, gbc);
	    editPanel.add(usernameField, gbc);
	    editPanel.add(passwordLabel, gbc);
	    editPanel.add(passwordField, gbc);

	    // Save button
	    gbc.insets = new Insets(12, 4, 4, 4); // Adjust spacing for the button
	    JButton saveButton = new JButton("Save");
	    editPanel.add(saveButton, gbc);

        // Action listener for the save button
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get username and password from the input fields
                String newUsername = usernameField.getText();
                char[] newPasswordChars = passwordField.getPassword();
                String newPassword = new String(newPasswordChars);

                // Check if username is taken
                if (!newUsername.equals(userProfile.getUsername()) && UserProfileDatabase.userExists(newUsername)) {
                    JOptionPane.showMessageDialog(editFrame, "Username already taken.");
                    return;
                }

                // Update database and profile information
                UserProfileDatabase.updateUserProfile(userProfile, newUsername, newPassword);

                editFrame.dispose();

                JOptionPane.showMessageDialog(editFrame, "Profile updated!");
            }
        });

        editFrame.add(editPanel);
        editFrame.pack(); // Let pack determine the frame size
        editFrame.setLocationRelativeTo(null); // Center on screen
        editFrame.setVisible(true);
    }
}
