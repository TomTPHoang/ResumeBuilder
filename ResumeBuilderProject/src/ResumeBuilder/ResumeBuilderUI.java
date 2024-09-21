package ResumeBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.border.*;

public class ResumeBuilderUI {
	
	private static JTextField usernameField;
    private static JPasswordField passwordField;

    public static JFrame createMainFrame() {
        JFrame frame = new JFrame("Resume Builder");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 300);
        return frame;
    }

    public static JPanel createMainPanel(JFrame frame, ActionListener loginAction, ActionListener registerAction, ActionListener editProfileAction, ActionListener editResumeAction) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2));

        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        panel.add(new JLabel());
        panel.add(createButton("Login", loginAction));

        panel.add(new JLabel());
        panel.add(createButton("Register", registerAction));

        panel.add(new JLabel());
        panel.add(createButton("Edit Profile", editProfileAction));

        panel.add(new JLabel());
        panel.add(createButton("Edit Resume", editResumeAction));

        return panel;
    }
    
    public static JTextField getUsernameField() {
        return usernameField;
    }
    
    public static JPasswordField getPasswordField() {
        return passwordField;
    }

    private static JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.addActionListener(listener);
        return button;
    }
    
    public static JPanel createInitialPanel(JFrame frame) {
        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel welcomeLabel = new JLabel("Welcome to Resume Builder!", SwingConstants.CENTER);
        panel.add(welcomeLabel);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            frame.setContentPane(createLoginPanel(frame));
            frame.revalidate();
            frame.repaint();
        });
        panel.add(loginButton);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> {
            frame.setContentPane(createRegistrationPanel(frame));
            frame.revalidate();
            frame.repaint();
        });
        panel.add(registerButton);

        return panel;
    }
    
    public static JPanel createRegistrationPanel(JFrame frame) {
        JPanel registerPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        registerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel registerLabel = new JLabel("Register", SwingConstants.CENTER);
        registerPanel.add(registerLabel);

        JTextField usernameField = new JTextField();
        registerPanel.add(usernameField);

        JPasswordField passwordField = new JPasswordField();
        registerPanel.add(passwordField);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            // Assuming you have a static method in UserRegistration to handle registration with Strings
            if (UserRegistration.handleRegistration(username, password, frame)) {
                JOptionPane.showMessageDialog(frame, "Registration successful! Please log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
                frame.setContentPane(ResumeBuilderUI.createLoginPanel(frame));
                frame.revalidate();
                frame.repaint();
            } else {
                JOptionPane.showMessageDialog(frame, "Registration failed. Username might be taken, or fields are invalid.", "Registration Failed", JOptionPane.ERROR_MESSAGE);
            }
        });
        registerPanel.add(registerButton);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            frame.setContentPane(createInitialPanel(frame));
            frame.revalidate();
            frame.repaint();
        });
        registerPanel.add(backButton);

        return registerPanel;
    }
    
    public static JPanel createLoginPanel(JFrame frame) {
        JPanel loginPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        loginPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel loginLabel = new JLabel("Login", SwingConstants.CENTER);
        loginPanel.add(loginLabel);

        JTextField usernameField = new JTextField();
        loginPanel.add(usernameField);

        JPasswordField passwordField = new JPasswordField();
        loginPanel.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            // Assuming you have a static method in UserAuthentication to handle login with Strings
            if (UserAuthentication.handleLogin(username, password, frame)) {
                UserProfile userProfile = UserProfileDatabase.getUserProfile(username);
                AppContext.setCurrentUserProfile(userProfile); // This should set the current user profile
                frame.setContentPane(ResumeBuilderUI.createMainApplicationPanel(frame));
                frame.revalidate();
                frame.repaint();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });
        loginPanel.add(loginButton);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            frame.setContentPane(createInitialPanel(frame));
            frame.revalidate();
            frame.repaint();
        });
        loginPanel.add(backButton);

        return loginPanel;
    }
    
    public static JPanel createMainApplicationPanel(JFrame frame) {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));

        // Welcome label at the top
        JLabel welcomeLabel = new JLabel("Welcome to the Resume Builder Application", SwingConstants.CENTER);
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        // Buttons panel in the center
        JPanel buttonsPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        buttonsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton editProfileButton = new JButton("Edit Profile");
        editProfileButton.addActionListener(e -> {
            UserProfileEditor.handleProfileEditing(frame);
        });
        buttonsPanel.add(editProfileButton);

        JButton editResumeButton = new JButton("Edit Resume");
        editResumeButton.addActionListener(e -> {
            UserResumeEditor.handleResumeEditing(frame);
        });
        buttonsPanel.add(editResumeButton);

        mainPanel.add(buttonsPanel, BorderLayout.CENTER);

        // Logout button at the bottom
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            // Logic to handle logout, for example:
            AppContext.setCurrentUserProfile(null); // Clear the current user
            frame.setContentPane(createInitialPanel(frame)); // Go back to the initial panel
            frame.revalidate();
            frame.repaint();
        });
        mainPanel.add(logoutButton, BorderLayout.SOUTH);

        return mainPanel;
    }
}