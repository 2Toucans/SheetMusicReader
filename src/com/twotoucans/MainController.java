package com.twotoucans;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import cern.colt.matrix.*;
import mnist.MnistReader;

public class MainController {
	MainGUI gui;
	Network network = null;
	public MainController() {
		gui = new MainGUI();
		gui.setController(this);
		gui.setPreferredSize(new Dimension(640, 480));
		gui.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		gui.pack();
		loadNetwork("n1.ntwk");
		if (network == null) {
			gui.showPopup("Failed to load n1.ntwk", "Failed to load network");
		}
	}
	
	public void displayScreen() {
		gui.setVisible(true);
	}

	public void onNetworkCreatorButton() {
		NetworkController n = new NetworkController();
		n.displayScreen();
	}
	
	public void onAnalyze() {
		JFileChooser jf = new JFileChooser(".");
		if (network != null) {
			if (jf.showOpenDialog(gui) == JFileChooser.APPROVE_OPTION) {
				File file = jf.getSelectedFile();
				FileInputStream fis = null;
				try {
					fis = new FileInputStream(file.getPath());
					BufferedImage img = ImageIO.read(file);
					Image tmp = img.getScaledInstance(28, 84, BufferedImage.SCALE_SMOOTH);
					BufferedImage i = new BufferedImage(28, 84, BufferedImage.TYPE_INT_RGB);
					Graphics drawToImg = i.createGraphics();
					drawToImg.drawImage(tmp, 0, 0, null);
					drawToImg.dispose();
					gui.setResultsImage(new ImageIcon(i));
					gui.setResultsText("Analyzing...");
					analyzeImage(i);
				}
				catch (IOException e) {
					e.printStackTrace();
				}
				finally {
					try {
						if (fis != null) {
							fis.close();
						}
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public void onLoad() {
		JFileChooser jf = new JFileChooser(".");
		if (jf.showOpenDialog(gui) == JFileChooser.APPROVE_OPTION) {
			File file = jf.getSelectedFile();
			loadNetwork(file.getPath());
		}
	}
	
	public void onAnalyzeMnist() {
		String imageFilename = null;
		TestData td = null;
		JFileChooser jf2 = new JFileChooser("./data");
		jf2.setDialogTitle("Please select an image file");
		if (jf2.showSaveDialog(gui) == JFileChooser.APPROVE_OPTION) {
			File file = jf2.getSelectedFile();
			imageFilename = file.getPath();
		}
		if (imageFilename != null) {
			
			List<int[][]> images = MnistReader.getImages(imageFilename);
		
			String numInput = (String)JOptionPane.showInputDialog(gui,
					"Please enter the index to check",
					"Enter index",
					JOptionPane.PLAIN_MESSAGE,
					null,
					null,
					3);
			int i = Integer.parseInt(numInput);

			int[][] img = images.get(i);
			BufferedImage displayImage = new BufferedImage(28, 28, BufferedImage.TYPE_INT_RGB);
			for (int y = 0; y < img.length; y++)
			{
				for (int x = 0; x < img[y].length; x++)
				{
					int val = 255 - img[y][x];
					displayImage.setRGB(x, y, (val << 16) + (val << 8) + val);
				}
			}
			gui.setResultsImage(new ImageIcon(displayImage));
			gui.setResultsText("Analyzing...");
			analyzeImage(displayImage);
		}
	}
	
	private void analyzeImage(BufferedImage i) {
		Runnable r = new Runnable() {
			public void run() {
				DoubleMatrix1D data = ImageConverter.convertImage(i);
				DoubleMatrix1D results = network.feedforward(data);
				int result = 0;
				double max = 0.0;
				for (int i = 0; i < results.size(); i++) {
					if (results.get(i) > max) {
						max = results.get(i);
						result = i;
					}
				}
				gui.setResultsText("I think the above image is a: " + Note.getNoteName(result - 10));
			}
		};
		Thread t = new Thread(r);
		t.start();
	}
	
	private void loadNetwork(String dir) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(dir);
			network = new Network(fis);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (fis != null) {
					fis.close();
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}