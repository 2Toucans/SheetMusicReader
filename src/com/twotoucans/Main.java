package com.twotoucans;

import cern.colt.matrix.DoubleFactory1D;
import cern.colt.matrix.DoubleMatrix1D;

public class Main
{
	public static void main(String[] args)
	{
		int[] layers = {1, 2, 1};
		double[] inputs = {0.5};
		DoubleMatrix1D input = DoubleFactory1D.dense.make(inputs);
		Network n = new Network(layers);

    	System.out.println("Output: " + n.feedforward(input));
	}
}