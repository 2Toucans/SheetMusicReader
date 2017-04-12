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
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainGUI extends javax.swing.JFrame {
	private MainController controller;
	public MainGUI() {
		setTitle("Sheet Music Reader");
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		
		JMenuItem openButton = new JMenuItem("Open");
		fileMenu.add(openButton);
		
		JMenu developMenu = new JMenu("Develop");
		menuBar.add(developMenu);
		
		JMenuItem networkCreatorButton = new JMenuItem("Network Creator");
		networkCreatorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.onNetworkCreatorButton();
			}
		});
		developMenu.add(networkCreatorButton);
		
		JLabel results = new JLabel("");
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
}
