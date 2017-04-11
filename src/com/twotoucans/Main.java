package com.twotoucans;

import java.awt.Dimension;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Main
{
	public static void main(String[] args)
	{
		GUI g = new GUI();
		g.setPreferredSize(new Dimension(640, 480));
		g.pack();
		g.setVisible(true);
		
		int[] layers = {784, 40, 10};
		Network n = new Network(layers);
		TestData training = new TestData(layers[layers.length-1]);
		training.readFromFiles("./data/train-labels.idx1-ubyte", "./data/train-images.idx3-ubyte");
		
		TestData testing = new TestData(layers[layers.length-1]);
        testing.readFromFiles("./data/t10k-labels.idx1-ubyte", "./data/t10k-images.idx3-ubyte");
        
    	n.SGD(training.getData(), 30, 10, 3.0, testing.getData());
	}
}