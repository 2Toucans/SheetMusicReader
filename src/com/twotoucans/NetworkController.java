package com.twotoucans;

import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;


public class NetworkController {
	NetworkGUI gui;
	Network n;
	TestData training;
	TestData testing;
	public NetworkController() {
		gui = new NetworkGUI();
		gui.setController(this);
		gui.setPreferredSize(new Dimension(640, 480));
		gui.pack();
	}
	
	public void displayScreen() {
		gui.setVisible(true);
	}

	public void onNew() {
		String layerNumInput = (String)JOptionPane.showInputDialog(gui,
				"Please enter the number of layers",
				"New Network",
				JOptionPane.PLAIN_MESSAGE,
				null,
				null,
				3);
		int layers = Integer.parseInt(layerNumInput);
		if (layers > 0) {
			int[] sizes = {784, 40, 10};
			if (layers != 3) {
				sizes = new int[layers];
			}
			for (int i = 0; i < layers; i++) {
				String layerInput = (String)JOptionPane.showInputDialog(gui,
						"Please enter the size of layer " + i,
						"New Network",
						JOptionPane.PLAIN_MESSAGE,
						null,
						null,
						sizes[i]);
				int layer = Integer.parseInt(layerInput);
				if (layer < 0) {
					return;
				}
				sizes[i] = layer;
			}
			n = new Network(sizes);
			n.setConsole(gui.getConsole());
			gui.getConsole().println("Created network");
		}
	}
	
	public void onLoad() {
		JFileChooser jf = new JFileChooser(".");
		if (jf.showOpenDialog(gui) == JFileChooser.APPROVE_OPTION) {
			File file = jf.getSelectedFile();
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(file.getPath());
				n = new Network(fis);
				n.setConsole(gui.getConsole());
				gui.getConsole().println("Loaded network");
			}
			catch (IOException e) {
				gui.getConsole().println("ERROR: " + e.getMessage());
			}
			finally {
				try {
					if (fis != null) {
						fis.close();
					}
				}
				catch (IOException e) {
					gui.getConsole().println("ERROR: " + e.getMessage());
				}
			}
		}
	}
	
	public void onSave() {
		if (n != null) {
			JFileChooser jf = new JFileChooser(".");
			if (jf.showSaveDialog(gui) == JFileChooser.APPROVE_OPTION) {
				File file = jf.getSelectedFile();
				FileOutputStream fos = null;
				try {
					fos = new FileOutputStream(file.getPath());
					n.write(fos);
					gui.getConsole().println("Saved network");
				}
				catch (IOException e) {
					gui.getConsole().println("ERROR: " + e.getMessage());
				}
				finally {
					try {
						if (fos != null) {
							fos.close();
						}
					}
					catch (IOException e) {
						gui.getConsole().println("ERROR: " + e.getMessage());
					}
				}
			}
		}
	}
	
	public void onStart() {
		if (n != null && training != null) {
			Runnable r = new Runnable() {
				public void run() {
			    	n.SGD(training.getData(), 30, 10, 3.0, testing.getData());
				}
			};
			Thread t = new Thread(r, "NetworkSGD");
			t.start();
			gui.getConsole().println("Started network training...");
		}
	}
	
	public void onStop() {
		n.stop();
		gui.getConsole().println("Halted training");
	}
	
	private TestData dataLoad() {
		String labelFilename = null;
		String imageFilename = null;
		TestData td = null;
		if (n != null) {
			JFileChooser jf = new JFileChooser("./data");
			jf.setDialogTitle("Please select a label file");
			if (jf.showSaveDialog(gui) == JFileChooser.APPROVE_OPTION) {
				File file = jf.getSelectedFile();
				labelFilename = file.getPath();
			}
			if (labelFilename == null) {
				return null;
			}
			JFileChooser jf2 = new JFileChooser("./data");
			jf2.setDialogTitle("Please select an image file");
			if (jf2.showSaveDialog(gui) == JFileChooser.APPROVE_OPTION) {
				File file = jf2.getSelectedFile();
				imageFilename = file.getPath();
			}
			if (labelFilename != null &&  imageFilename != null) {
				td = new TestData(n.getLayerSize(n.getNumLayers() - 1));
				td.readFromFiles(labelFilename, imageFilename);
			}
		}
		else {
			gui.getConsole().println("No network; Please create or load a network before loading training data.");
		}
		return td;
	}
	public void onTrainingDataLoad() {
		TestData tmp = dataLoad();
		if (tmp != null) {
			training = tmp;
		}
	}
	public void onTestDataLoad() {
		TestData tmp = dataLoad();
		if (tmp != null) {
			testing = tmp;
		}
	}
}
