package com.twotoucans;

import java.util.Random;

public class Network
{
    private int numLayers;
    private int[] sizes;
    private double[][] biases;
    private double[][][] weights;
    
    public Network(int[] sizes)
    {
        numLayers = sizes.length;
        this.sizes = sizes;
        this.biases = new double[numLayers - 1][];
        this.weights = new double[numLayers - 1][][];

        Random r = new Random();
        
        for (int i = 0; i < numLayers - 1; i++)
        {
            int size = this.sizes[i + 1];
            biases[i] = new double[size];
            for (int y = 1; y < size; y++)
            {
                biases[i][y] = r.nextGaussian();
            }
        }
        
        for (int i = 0; i < numLayers - 1; i++)
        {
            int x = this.sizes[i];
            int y = this.sizes[i + 1];
            weights[i] = new double[y][x];
            
            for (int j = 0; j < y; j++)
            {
                for (int n = 0; n < x; n++)
                {
                    weights[i][y][x] = r.nextGaussian();
                }
            }
        }
    }
    
    //Mini batch will be an array of input/outputs [FIX LATER]
    public void update_mini_batch(TestEntry[] mini_batch, int eta)
    {
        /*Update the network's weights and biases by applying
        gradient descent using backpropagation to a single mini batch.
        The "mini_batch" is a list of tuples "(x, y)", and "eta"
        is the learning rate.*/
        
        /*for x, y in mini_batch:
            delta_nabla_b, delta_nabla_w = self.backprop(x, y)
            nabla_b = [nb+dnb for nb, dnb in zip(nabla_b, delta_nabla_b)]
            nabla_w = [nw+dnw for nw, dnw in zip(nabla_w, delta_nabla_w)]
        self.weights = [w-(eta/len(mini_batch))*nw 
                        for w, nw in zip(self.weights, nabla_w)]
        self.biases = [b-(eta/len(mini_batch))*nb 
                       for b, nb in zip(self.biases, nabla_b)]*/
        
        double[][] nabla_b = new double[biases.length][biases[0].length];
        double[][] nabla_w = new double[weights.length][weights[0].length];
        
        for(TestEntry entry : mini_batch)
        {
            double[] gradientTup = backProp(entry.getImg(), entry.getValue());
        }
        
    }
    
    //Will return a tuple representing the gradient for cost functions
    //AKA nabla_b and nabla_w
    public double[] backProp(double x, double y)
    {
        double[] hi = new double[2];
        hi[0] = 1;
        hi[1] = 1;
        return hi;
    }
}
