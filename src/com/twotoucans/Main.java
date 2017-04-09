package com.twotoucans;

public class Main
{
	public static void main(String[] args)
	{
		int[] layers = {728, 30, 10};
		Network n = new Network(layers);
		TestData training = new TestData(layers[layers.length-1]);
		training.readFromFiles("./data/train-labels.idx1-ubyte", "./data/train-images.idx3-ubyte");
		
		TestData testing = new TestData(layers[layers.length-1]);
        testing.readFromFiles("./data/t10k-labels.idx1-ubyte", "./data/t10k-images.idx3-ubyte");
        
    	n.SGD(training.getData(), 30, 10, 3, testing.getData());
	}
}