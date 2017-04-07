package com.twotoucans;

import cern.colt.matrix.DoubleFactory1D;
import cern.colt.matrix.DoubleMatrix1D;

public class Main
{
	public static void main(String[] args)
	{
		int[] layers = {728, 30, 10};
		Network n = new Network(layers);

    	n.SGD(training_data, epochs, mini_batch_size, eta, test_data);
	}
}