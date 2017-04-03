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
    
    public DoubleMatrix1D feedforward(DoubleMatrix1D a) {
    	/*
    	def feedforward(self, a):
        """Return the output of the network if ``a`` is input."""
        for b, w in zip(self.biases, self.weights):
            a = sigmoid(np.dot(w, a)+b)
        return a
    	 */
    	DoubleMatrix1D result = a;
    	for (int i = 0; i < numLayers - 1; i++)
    	{
    		result = weights[i].zMult(result, null)
    				.assign(biases[i], Functions.plus);
    				//.assign(Sigmoid.sigmoid);
    	}
    	return result;
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
        
        DoubleMatrix1D[] nabla_b = new DoubleMatrix1D[biases.length];
        DoubleMatrix2D[] nabla_w = new DoubleMatrix2D[weights.length];
        for(int i = 0; i < nabla_b.length; i++)
        {
            nabla_b[i] = DoubleFactory1D.dense.make(biases[i].size());
            nabla_w[i] = DoubleFactory2D.dense.make(weights[i].rows(), weights[i].columns());
        }
        
        for(TestEntry entry : mini_batch)
        {
            DoubleMatrix2D[] deltaNablas = backProp(entry.getImg(), entry.getValue());
            //FIGURE OUT BACK PROP FIRST THEN COME BACK HERE
        }
        
    }
    
    //Will return a tuple representing the gradient for cost functions
    //AKA nabla_b and nabla_w
    
    /*
        nabla_b = [np.zeros(b.shape) for b in self.biases]
        nabla_w = [np.zeros(w.shape) for w in self.weights]
        
        # feedforward
        activation = x
        activations = [x] # list to store all the activations, layer by layer
        zs = [] # list to store all the z vectors, layer by layer
        for b, w in zip(self.biases, self.weights):
            z = np.dot(w, activation)+b
            zs.append(z)
            activation = sigmoid(z)
            activations.append(activation)
            
        # backward pass
        delta = self.cost_derivative(activations[-1], y) * \
            sigmoid_prime(zs[-1])
        nabla_b[-1] = delta
        nabla_w[-1] = np.dot(delta, activations[-2].transpose())
        
        for l in xrange(2, self.num_layers):
            z = zs[-l]
            sp = sigmoid_prime(z)
            delta = np.dot(self.weights[-l+1].transpose(), delta) * sp
            nabla_b[-l] = delta
            nabla_w[-l] = np.dot(delta, activations[-l-1].transpose())
        return (nabla_b, nabla_w)*/
    public DoubleMatrix2D[] backProp(int[] x, int y)
    {
        DoubleMatrix1D[] nabla_b = new DoubleMatrix1D[biases.length];
        DoubleMatrix2D[] nabla_w = new DoubleMatrix2D[weights.length];
        for(int i = 0; i < nabla_b.length; i++)
        {
            nabla_b[i] = DoubleFactory1D.dense.make(biases[i].size());
            nabla_w[i] = DoubleFactory2D.dense.make(weights[i].rows(), weights[i].columns());
        }
        
        int[][] activations = new int[biases.length+1][x.length];
        
        return new DoubleMatrix2D[1];
    }
}
