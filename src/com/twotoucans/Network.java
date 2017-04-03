package com.twotoucans;

import java.util.Random;

import cern.jet.math.*;
import cern.jet.random.*;
import cern.jet.random.engine.RandomEngine;
import cern.colt.matrix.*;
import cern.colt.function.*;

public class Network
{
    private int numLayers;
    private int[] sizes;
    private DoubleMatrix1D[] biases;
    private DoubleMatrix2D[] weights;
    
    public Network(int[] sizes)
    {
        numLayers = sizes.length;
        this.sizes = sizes;
        this.biases = new DoubleMatrix1D[numLayers - 1];
        this.weights = new DoubleMatrix2D[numLayers - 1];
        
        Normal gaussGen = new Normal(0.0, 1.0, RandomEngine.makeDefault());
        
        for (int i = 0; i < numLayers - 1; i++)
        {
        	biases[i] = DoubleFactory1D.dense.make(this.sizes[i + 1]);
        	weights[i] = DoubleFactory2D.dense.make(this.sizes[i + 1], this.sizes[i]);
        	
        	biases[i].assign(gaussGen);
        	weights[i].assign(gaussGen);
        	
        	System.out.println(biases[i]);
        	System.out.println(weights[i]);
        }
    }
    
    //Mini batch will be an array of input/outputs [FIX LATER]
    public void update_mini_batch(int[] mini_batch, int eta)
    {
        /*Update the network's weights and biases by applying
        gradient descent using backpropagation to a single mini batch.
        The "mini_batch" is a list of tuples "(x, y)", and "eta"
        is the learning rate.*/
        
        /*nabla_b = [np.zeros(b.shape) for b in self.biases]
        nabla_w = [np.zeros(w.shape) for w in self.weights]
        for x, y in mini_batch:
            delta_nabla_b, delta_nabla_w = self.backprop(x, y)
            nabla_b = [nb+dnb for nb, dnb in zip(nabla_b, delta_nabla_b)]
            nabla_w = [nw+dnw for nw, dnw in zip(nabla_w, delta_nabla_w)]
        self.weights = [w-(eta/len(mini_batch))*nw 
                        for w, nw in zip(self.weights, nabla_w)]
        self.biases = [b-(eta/len(mini_batch))*nb 
                       for b, nb in zip(self.biases, nabla_b)]
        
        
        double[][] nabla_b = new double[biases.length][biases[0].length];
        double[][] nabla_w = new double[weights.length][weights[0].length];   
        */
    }
}
