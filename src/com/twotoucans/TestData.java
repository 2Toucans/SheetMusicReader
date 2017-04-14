package com.twotoucans;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import cern.colt.matrix.DoubleFactory1D;
import cern.colt.matrix.DoubleMatrix1D;
import mnist.MnistReader;

public class TestData
{
	public TestEntry[] tests;
	private int outputLayerSize;
	
	public TestData(int outputLayerSize)
	{
		this.outputLayerSize = outputLayerSize;
	}
	
	public void readFromFile(String labelFile)
	{
		ArrayList<TestEntry> expandingEntries = new ArrayList<TestEntry>();
		FileReader fr = null;
		BufferedReader read = null;
		try {
			File f = new File(labelFile);
			fr = new FileReader(f);
			read = new BufferedReader(fr);
			String val;
			while ((val = read.readLine()) != null) {
				int pos;
				int mod;
				int lenNum;
				int lenDen;
				Scanner sc = new Scanner(val);
				String tmp = sc.next();
				DoubleMatrix1D img = ImageConverter.convertImage(f.getParent() + "/" + tmp);
				tmp = sc.next();
				pos = Integer.parseInt(tmp);
				tmp = sc.next();
				mod = Integer.parseInt(tmp);
				tmp = sc.next();
				lenNum = Integer.parseInt(tmp);
				tmp = sc.next();
				lenDen = Integer.parseInt(tmp);
				Note n = new Note(pos, mod, lenNum, lenDen);
				expandingEntries.add(new TestEntry(img, n, outputLayerSize));
			}
			tests = new TestEntry[expandingEntries.size()];
			tests = expandingEntries.toArray(tests);
			read.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (fr != null) {
					fr.close();
				}
				if (read != null) {
					read.close();
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public TestEntry[] getData()
	{
	    return tests;
	}
}
