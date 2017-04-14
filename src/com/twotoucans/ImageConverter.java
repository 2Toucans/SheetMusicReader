package com.twotoucans;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;

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
}
