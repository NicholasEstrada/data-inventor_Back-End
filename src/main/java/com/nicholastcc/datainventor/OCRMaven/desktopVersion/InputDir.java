package com.nicholastcc.datainventor.OCRMaven.desktopVersion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Serial;

public class InputDir extends JFrame{
	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = 1L;
	JTextField textField1;
	JButton button;

	public InputDir() {

		setSize(500, 200);
		setTitle("PROCURA E-MAIL");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new FlowLayout());

		textField1 = new JTextField(30);
		button = new JButton("Submit");

		getContentPane().add(textField1);
		getContentPane().add(button);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String caminho;
				caminho = textField1.getText();
				try {
					@SuppressWarnings("unused")
					ListaPasta v = new ListaPasta(caminho);

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		setVisible(true);
	}

	public static void main(String args[]) {
		new InputDir();
	}
}
