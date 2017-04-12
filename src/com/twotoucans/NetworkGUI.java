package com.twotoucans;
import javax.swing.JTextPane;
import javax.swing.text.DefaultCaret;

import java.awt.BorderLayout;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.DropMode;
import javax.swing.JScrollPane;

public class NetworkGUI extends javax.swing.JFrame {
	private Console console;
	private NetworkController controller;
	public NetworkGUI() {
		setTitle("Network Creator");
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		
		JMenuItem newButton = new JMenuItem("New");
		newButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.onNew();
			}
		});
		fileMenu.add(newButton);
		
		JMenuItem loadButton = new JMenuItem("Load");
		loadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.onLoad();
			}
		});
		fileMenu.add(loadButton);
		
		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.onSave();
			}
		});
		fileMenu.add(mntmSave);
		
		JMenu controlMenu = new JMenu("Control");
		menuBar.add(controlMenu);
		
		JMenuItem startButton = new JMenuItem("Start");
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.onStart();
			}
		});
		controlMenu.add(startButton);
		
		JMenuItem stopButton = new JMenuItem("Stop");
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.onStop();
			}
		});
		controlMenu.add(stopButton);
		
		JMenu dataMenu = new JMenu("Data");
		menuBar.add(dataMenu);
		
		JMenuItem loadTrainingDataButton = new JMenuItem("Load Training Data");
		loadTrainingDataButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.onTrainingDataLoad();
			}
		});
		dataMenu.add(loadTrainingDataButton);
		
		JMenuItem loadTestingDataButton = new JMenuItem("Load Testing Data");
		loadTestingDataButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.onTestDataLoad();
			}
		});
		dataMenu.add(loadTestingDataButton);
		
		JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		JTextArea consoleView = new JTextArea();
		consoleView.setEditable(false);
		scrollPane.setViewportView(consoleView);
		DefaultCaret c = (DefaultCaret)(consoleView.getCaret());
		c.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		console = new Console(consoleView);
		console.println("Network Creator Initialized");
	}
	
	
	public void setController(NetworkController c) {
		controller = c;
	}
	
	public Console getConsole() {
		return console;
	}
}
