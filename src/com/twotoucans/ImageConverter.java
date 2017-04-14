package com.twotoucans;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;

import cern.colt.matrix.*;

public final class ImageConverter {
	public static DoubleMatrix1D convertImage(BufferedImage i) {
		DoubleMatrix1D img = DoubleFactory1D.dense.make(i.getWidth() * i.getHeight());
		for (int y = 0; y < i.getHeight(); y++) {
			for (int x = 0; x < i.getWidth(); x++) {
				Color c = new Color(i.getRGB(x, y));
				double colorValue = 255 - c.getBlue() + 255 - c.getGreen() + 255 - c.getRed();
				img.set(y * i.getWidth() + x,  colorValue / 768);
			}
		}
		return img;
	}
	
	public static DoubleMatrix1D convertImage(String path) {
		File file = new File(path);
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file.getPath());
			BufferedImage img = ImageIO.read(file);
			Image tmp = img.getScaledInstance(28, 84, BufferedImage.SCALE_SMOOTH);
			BufferedImage i = new BufferedImage(28, 84, BufferedImage.TYPE_INT_RGB);
			Graphics drawToImg = i.createGraphics();
			drawToImg.drawImage(tmp, 0, 0, null);
			drawToImg.dispose();
			return convertImage(i);
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
		return null;
	}
}
