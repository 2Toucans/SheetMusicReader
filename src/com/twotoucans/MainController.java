package com.twotoucans;

import java.awt.Dimension;

import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

public class MainController {
	MainGUI gui;
	public MainController() {
		gui = new MainGUI();
		gui.setController(this);
		gui.setPreferredSize(new Dimension(640, 480));
		gui.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		gui.pack();
	}
	
	public void displayScreen() {
		gui.setVisible(true);
	}

	public void onNetworkCreatorButton() {
		NetworkController n = new NetworkController();
		n.displayScreen();
	}
}