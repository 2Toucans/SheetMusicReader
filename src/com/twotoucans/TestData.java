package com.twotoucans;
import java.util.List;

import cern.colt.matrix.DoubleFactory1D;
import cern.colt.matrix.DoubleMatrix1D;
import mnist.MnistReader;

public class TestData {
	public TestEntry[] tests;
	private int outputLayerSize;
	
	public TestData(int outputLayerSize)
	{
		this.outputLayerSize = outputLayerSize;
	}
	
	public void readFromFiles(String labelFile, String imageFile)
	{
		int[] labels = MnistReader.getLabels(labelFile);
		List<int[][]> images = MnistReader.getImages(imageFile);
		
		tests = new TestEntry[labels.length];
		for (int i = 0; i < labels.length; i++)
		{
			int[][] img = images.get(i);
			double[] imageDec = new double[img.length * img[0].length];
			for (int x = 0; x < img.length; x++)
			{
				for (int y = 0; y < img[x].length; y++)
				{
					imageDec[x*img.length + y] = (double)img[x][y] / 256;
				}
			}
			DoubleMatrix1D inputValues = DoubleFactory1D.dense.make(imageDec);
			tests[i] = new TestEntry(inputValues, labels[i], outputLayerSize);
		}
	}
	
	public TestEntry[] getData()
	{
	    return tests;
	}
}
