package com.twotoucans;
import javax.swing.JToolBar;
import java.awt.BorderLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainGUI extends javax.swing.JFrame {
	private MainController controller;
	private JLabel results;
	public MainGUI() {
		setTitle("Sheet Music Reader");
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		
		JMenuItem analyzeButton = new JMenuItem("Analyze Image");
		analyzeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.onAnalyze();
			}
		});
		
		JMenuItem loadButton = new JMenuItem("Load Network");
		loadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.onLoad();
			}
		});
		fileMenu.add(loadButton);
		
		JMenuItem mntmAnalyzeMnist = new JMenuItem("Analyze MNIST");
		mntmAnalyzeMnist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.onAnalyzeMnist();
			}
		});
		fileMenu.add(mntmAnalyzeMnist);
		fileMenu.add(analyzeButton);
		
		JMenu developMenu = new JMenu("Develop");
		menuBar.add(developMenu);
		
		JMenuItem networkCreatorButton = new JMenuItem("Network Creator");
		networkCreatorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.onNetworkCreatorButton();
			}
		});
		developMenu.add(networkCreatorButton);
		
		results = new JLabel("");
		results.setIcon(null);
		getContentPane().add(results, BorderLayout.CENTER);
	}
	
	public void setController(MainController c) {
		controller = c;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 733576162090238072L;

	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
	
	public void setResultsImage(ImageIcon i) {
		results.setIcon(i);
	}
	
	public void setResultsText(String s) {
		results.setText(s);
	}
	
	public void showPopup(String title, String message) {
		JOptionPane.showMessageDialog(this, title, message, JOptionPane.PLAIN_MESSAGE);
	}
}
