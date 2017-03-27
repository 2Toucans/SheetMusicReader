package com.twotoucans;

import java.util.Random;

public class Network
{
	private int numLayers;
	private int[] sizes;
	private double[][] biases;
	private double[][][] weights;
	
	public Network(int[] sizes)
	{
		numLayers = sizes.length;
		this.sizes = sizes;
		this.biases = new double[numLayers - 1][];
		this.weights = new double[numLayers - 1][][];

		Random r = new Random();
		
		for (int i = 0; i < numLayers - 1; i++)
		{
			int size = this.sizes[i + 1];
			biases[i] = new double[size];
			for (int y = 1; y < size; y++)
			{
				biases[i][y] = r.nextGaussian();
			}
		}
		
		for (int i = 0; i < numLayers - 1; i++)
		{
			int x = this.sizes[i];
			int y = this.sizes[i + 1];
			weights[i] = new double[y][x];
			
			for (int j = 0; j < y; j++)
			{
				for (int n = 0; n < x; n++)
				{
					weights[i][y][x] = r.nextGaussian();
				}
			}
		}
	}
}
