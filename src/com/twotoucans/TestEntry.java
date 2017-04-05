package com.twotoucans;

import cern.colt.matrix.DoubleMatrix1D;

public class TestEntry
{
    private DoubleMatrix1D image;
    private DoubleMatrix1D value;
    
    public TestEntry(DoubleMatrix1D i, DoubleMatrix1D v)
    {
        image = i;
        value = v;
    }
    
    public DoubleMatrix1D getImg()
    {
        return image;
    }
    
    public DoubleMatrix1D getValue()
    {
        return value;
    }
}
