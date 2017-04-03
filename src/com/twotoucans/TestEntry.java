package com.twotoucans;

public class TestEntry
{
    private int[] image;
    private int value;
    
    public TestEntry(int[] i, int v)
    {
        image = i;
        value = v;
    }
    
    public int[] getImg()
    {
        return image;
    }
    
    public int getValue()
    {
        return value;
    }
}
