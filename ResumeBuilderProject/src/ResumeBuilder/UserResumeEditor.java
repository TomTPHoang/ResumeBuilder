package ResumeBuilder;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

public class UserResumeEditor {
	public static void handleResumeEditing(JFrame parentFrame) {
		UserProfile currentUserProfile = AppContext.getCurrentUserProfile();
        if (currentUserProfile != null) {
            editResume(currentUserProfile, parentFrame);
        } else {
            JOptionPane.showMessageDialog(parentFrame, "Please log in first.");
        }
    }
	
	public static void editResume(UserProfile userProfile, JFrame parentFrame) {
		JDialog dialog = new JDialog(parentFrame, "Edit Resume", true);
	    dialog.setLayout(new BorderLayout());

	    JTextPane textPane = new JTextPane();
	    textPane.setContentType("text/html"); // Set content type as HTML



	    // Load the HTML content into the JTextPane
	    String resumeHtmlContent = AppContext.getResumeData(userProfile.getUsername());
	    textPane.setText(resumeHtmlContent);

	    // Add the JTextPane to a JScrollPane
	    JScrollPane scrollPane = new JScrollPane(textPane);
	    dialog.add(scrollPane, BorderLayout.CENTER);

	    // Toolbar for text customization
	    JToolBar toolBar = new JToolBar();
	    Action boldAction = new StyledEditorKit.BoldAction();
	    boldAction.putValue(Action.NAME, "Bold");
	    toolBar.add(boldAction);

	    Action italicAction = new StyledEditorKit.ItalicAction();
	    italicAction.putValue(Action.NAME, "Italic");
	    toolBar.add(italicAction);

	    Action underlineAction = new StyledEditorKit.UnderlineAction();
	    underlineAction.putValue(Action.NAME, "Underline");
	    toolBar.add(underlineAction);

	    // Add font size increase and decrease actions
	    @SuppressWarnings("serial")
	    Action increaseFontSizeAction = new AbstractAction("Increase Font Size") {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            increaseFontSize(textPane);
	        }
	    };
        toolBar.add(increaseFontSizeAction);
        @SuppressWarnings("serial")
        Action decreaseFontSizeAction = new AbstractAction("Decrease Font Size") {
            @Override
            public void actionPerformed(ActionEvent e) {
                decreaseFontSize(textPane);
            }
        };
        toolBar.add(decreaseFontSizeAction);

	    // Add toolbar to the dialog
	    dialog.add(toolBar, BorderLayout.NORTH);

	    // Panel for the buttons
	    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

	    JButton saveButton = new JButton("Save");
	    saveButton.addActionListener(e -> {
	        try {
	            // Get the HTML content from the JTextPane
	            String editedResume = getStyledDocumentAsHtml(textPane);
	            // Save the HTML content
	            AppContext.setResumeData(userProfile.getUsername(), editedResume);
	            AppContext.saveUserResumes();
	            JOptionPane.showMessageDialog(dialog, "Resume saved successfully.");
	        } catch (Exception ex) {
	            ex.printStackTrace();
	            JOptionPane.showMessageDialog(dialog, "Error saving resume: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	        }
	        dialog.dispose();
	    });
	    buttonPanel.add(saveButton);

	    JButton exportToPDFButton = new JButton("Export to PDF");
	    exportToPDFButton.addActionListener(e -> {
	        try {
	            // Call the method to export the resume to PDF
	            String resumeContent = getStyledDocumentAsHtml(textPane); // Get HTML content
	            JFileChooser fileChooser = new JFileChooser();
	            fileChooser.setDialogTitle("Specify a file to save");
	            fileChooser.setSelectedFile(new File(userProfile.getUsername() + "_resume.pdf"));
	            
	            int userSelection = fileChooser.showSaveDialog(dialog);
	            
	            if (userSelection == JFileChooser.APPROVE_OPTION) {
	                File fileToSave = fileChooser.getSelectedFile();
	                
	                // Ensure .pdf extension
	                if (!fileToSave.getName().toLowerCase().endsWith(".pdf")) {
	                    fileToSave = new File(fileToSave.toString() + ".pdf");
	                }
	                
	                // Export to PDF
	                PDFExport.exportResumeToPDF(fileToSave.getAbsolutePath(), resumeContent);
	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	            JOptionPane.showMessageDialog(dialog, "Error exporting resume to PDF: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	        }
	    });
	    buttonPanel.add(exportToPDFButton);

	    JButton backButton = new JButton("Back");
	    backButton.addActionListener(e -> {
	        dialog.dispose();
	    });
	    buttonPanel.add(backButton);

	    dialog.add(buttonPanel, BorderLayout.SOUTH);

	    dialog.pack();
	    dialog.setMinimumSize(new Dimension(500, 400));
	    dialog.setLocationRelativeTo(parentFrame);
	    dialog.setVisible(true);
	}
	
	public static String getStyledDocumentAsHtml(JTextPane textPane) {
	    // Get the document from the JTextPane
	    StyledDocument doc = textPane.getStyledDocument();
	    // Create a writer to hold the HTML content
	    StringWriter writer = new StringWriter();
	    
	    try {
	        // Write the document content to the writer as HTML
	        textPane.getEditorKit().write(writer, doc, 0, doc.getLength());
	    } catch (IOException | BadLocationException e) {
	        e.printStackTrace();
	    }
	    
	    // Return the HTML content as a string
	    return writer.toString();
	}
	
	private static void increaseFontSize(JTextPane textPane) {
	    StyledDocument doc = textPane.getStyledDocument();
	    int selectionStart = textPane.getSelectionStart();
	    int selectionEnd = textPane.getSelectionEnd();

	    if (selectionStart != selectionEnd) { // Some text is selected
	        changeFontSize(doc, selectionStart, selectionEnd - selectionStart, 2);
	    } else { // No text is selected
	        MutableAttributeSet attrs = textPane.getInputAttributes();
	        int fontSize = StyleConstants.getFontSize(attrs);
	        StyleConstants.setFontSize(attrs, fontSize + 2);
	        textPane.setCharacterAttributes(attrs, false);
	    }
	}
	
	private static void decreaseFontSize(JTextPane textPane) {
	    StyledDocument doc = textPane.getStyledDocument();
	    int selectionStart = textPane.getSelectionStart();
	    int selectionEnd = textPane.getSelectionEnd();

	    if (selectionStart != selectionEnd) { // Some text is selected
	        changeFontSize(doc, selectionStart, selectionEnd - selectionStart, -2);
	    } else { // No text is selected
	        MutableAttributeSet attrs = textPane.getInputAttributes();
	        int fontSize = StyleConstants.getFontSize(attrs);
	        if (fontSize > 4) { // Prevent font size from becoming too small
	            StyleConstants.setFontSize(attrs, fontSize - 2);
	            textPane.setCharacterAttributes(attrs, false);
	        }
	    }
	}
	
	private static void changeFontSize(StyledDocument doc, int start, int length, int increment) {
	    for (int i = start; i < start + length; ) {
	        Element elem = doc.getCharacterElement(i);
	        AttributeSet attrSet = elem.getAttributes();
	        int fontSize = StyleConstants.getFontSize(attrSet);
	        MutableAttributeSet newAttrs = new SimpleAttributeSet(attrSet.copyAttributes());

	        int newFontSize = fontSize + increment;
	        if (newFontSize > 4) { // Prevent font size from becoming too small
	            StyleConstants.setFontSize(newAttrs, newFontSize);
	            doc.setCharacterAttributes(i, elem.getEndOffset() - i, newAttrs, false);
	        }
	        i = elem.getEndOffset();
	    }
	}
}
