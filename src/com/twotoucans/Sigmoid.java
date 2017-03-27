package com.twotoucans;

public final class Sigmoid
{
	private Sigmoid() {}
	public static double sigmoid(double z)
	{
		return 1.0 / (1.0 + Math.exp(z));
	}
	
	public static double sigPrime(double z)
	{
		return sigmoid(z) * (1 - sigmoid(z));
	}
}
