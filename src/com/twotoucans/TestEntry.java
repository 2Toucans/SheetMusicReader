package com.twotoucans;

import cern.colt.matrix.DoubleFactory1D;
import cern.colt.matrix.DoubleMatrix1D;

public class TestEntry
{
    private DoubleMatrix1D image;
    private DoubleMatrix1D value;
    private Note out;
    
    public TestEntry(DoubleMatrix1D i, Note n, int outSize)
    {
        image = i;
        out = n;
        
        double[] expectedValues = new double[outSize];
        for (int x = 0; x < outSize; x++)
        {
            expectedValues[x] = x - 10 == n.getPosition() ? 1.0 : 0.0;
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
    
    public Note getNote()
    {
        return out;
    }
}
