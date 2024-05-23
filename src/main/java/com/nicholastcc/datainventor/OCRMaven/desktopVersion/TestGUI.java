package com.nicholastcc.datainventor.OCRMaven.desktopVersion;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class TestGUI {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestGUI window = new TestGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws IOException 
	 */
	public TestGUI() throws IOException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws IOException 
	 */
	private void initialize() throws IOException {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		TextField textField = new TextField();
		frame.getContentPane().add(textField, BorderLayout.NORTH);
		// ListaPasta v = new ListaPasta("test");
		
		Checkbox checkbox = new Checkbox("e-mail");
		frame.getContentPane().add(checkbox, BorderLayout.WEST);
		
		Checkbox checkbox_1 = new Checkbox("New check box");
		frame.getContentPane().add(checkbox_1, BorderLayout.CENTER);
	}

}
