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

public class GUI extends javax.swing.JFrame {
	public GUI() {
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu file = new JMenu("File");
		menuBar.add(file);
		
		JMenuItem analyze = new JMenuItem("Analyze");
		file.add(analyze);
		
		JMenu mnNetwork = new JMenu("Network");
		menuBar.add(mnNetwork);
		
		JMenuItem mntmCreate = new JMenuItem("Train");
		mnNetwork.add(mntmCreate);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Stop");
		mnNetwork.add(mntmNewMenuItem);
		
		JLabel results = new JLabel("");
		results.setIcon(null);
		getContentPane().add(results, BorderLayout.CENTER);
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
