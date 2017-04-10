package com.twotoucans;

import cern.colt.function.*;

public final class Sigmoid
{
	private Sigmoid() {}
	public static DoubleFunction sigmoid = new DoubleFunction()
	{
		public final double apply(double z)
		{
			return 1.0 / (1.0 + Math.exp(-z));
		}
	};
	
	public static DoubleFunction sigPrime = new DoubleFunction()
	{
		public final double apply(double z)
		{
			return sigmoid.apply(z) * (1 - sigmoid.apply(z));
		}
	};
}
