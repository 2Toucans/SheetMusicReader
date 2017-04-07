package com.twotoucans;

import cern.colt.matrix.DoubleFactory1D;
import cern.colt.matrix.DoubleMatrix1D;

public class TestEntry
{
    private DoubleMatrix1D image;
    private DoubleMatrix1D value;
    private int num;
    
    public TestEntry(DoubleMatrix1D i, int n, int outSize)
    {
        image = i;
        num = n;
        
        double[] expectedValues = new double[outSize];
        for (int x = 0; x < outSize; x++)
        {
            expectedValues[x] = x == n ? 1.0 : 0.0;
        }
        value = DoubleFactory1D.dense.make(expectedValues);
    }
    
    public DoubleMatrix1D getImg()
    {
        return image;
    }
    
    public DoubleMatrix1D getValue()
    {
        return value;
    }
    
    public int getNum()
    {
        return num;
    }
}
