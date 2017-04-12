package com.twotoucans;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import cern.jet.math.*;
import cern.jet.random.*;
import cern.jet.random.engine.RandomEngine;
import cern.colt.matrix.*;
import cern.colt.matrix.linalg.Algebra;

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
    				.assign(biases[i], Functions.plus)
    				.assign(Sigmoid.sigmoid);
    	}
    	return result;
	}
    
    /*Train the neural network using mini-batch stochastic
    gradient descent.  The ``training_data`` is a list of tuples
    ``(x, y)`` representing the training inputs and the desired
    outputs.  The other non-optional parameters are
    self-explanatory.  If ``test_data`` is provided then the
    network will be evaluated against the test data after each
    epoch, and partial progress printed out.  This is useful for
    tracking progress, but slows things down substantially.*/
    public void SGD(TestEntry[] training_data, int epochs,
            int mini_batch_size, double eta, TestEntry[] test_data)
    {
        int n_test = 0;
        
        if(test_data != null)
            n_test = test_data.length;
        
        int n = training_data.length;
        
        for(int j = 0; j < epochs; j++)
        {
            List<TestEntry> shuffData = Arrays.asList(training_data);
            
            Collections.shuffle(shuffData);
            
            TestEntry[][] mini_batches = new TestEntry[n/mini_batch_size][mini_batch_size];
            
            for(int k = 0; k < mini_batches.length; k++)
            {
                for(int m = 0; m < mini_batch_size; m++)
                {
                    mini_batches[k][m] = shuffData.get((k*mini_batch_size)+m);
                }
                
                update_mini_batch(mini_batches[k], eta);
            }
            
            if (test_data != null)
                System.out.println("Epoch " + j + ": " + evaluate(test_data) + " / " + n_test);
            else
                System.out.println("Epoch " + j + " complete");
        }
    }
    
    /*
     * Update the network's weights and biases by applying
     * gradient descent using backpropagation to a single mini batch.
     * The "mini_batch" is a list of tuples "(x, y)", and "eta"
     * is the learning rate.
     */
    public void update_mini_batch(TestEntry[] mini_batch, double eta)
    {
        DoubleMatrix1D[] nabla_b = new DoubleMatrix1D[biases.length];
        DoubleMatrix2D[] nabla_w = new DoubleMatrix2D[weights.length];
        for(int i = 0; i < nabla_b.length; i++)
        {
            nabla_b[i] = DoubleFactory1D.dense.make(biases[i].size());
            nabla_w[i] = DoubleFactory2D.dense.make(weights[i].rows(), weights[i].columns());
        }
        
        for(TestEntry entry : mini_batch)
        {
            DoubleMatrix2D[][] deltaNablas = backProp(entry.getImg(), entry.getValue());
            DoubleMatrix1D[] deltaNabla_b = new DoubleMatrix1D[deltaNablas[0].length];
            DoubleMatrix2D[] deltaNabla_w = deltaNablas[1];
            
            //Convert the deltaNabla_b values to 1D matrices
            //Then add the deltas to the weights and biases
            for(int i = 0; i < deltaNablas[0].length; i++)
            {
                deltaNabla_b[i] = deltaNablas[0][i].viewColumn(0);
                nabla_b[i] = nabla_b[i].assign(deltaNabla_b[i], Functions.plus);
                nabla_w[i] = nabla_w[i].assign(deltaNabla_w[i], Functions.plus);
            }
            
            for(int i = 0; i < weights.length; i++)
            {
                double learnAmt = eta/mini_batch.length;
                
                for(int j = 0; j < nabla_w[i].rows(); j++)
                {
                    for(int k = 0; k < nabla_w[i].columns(); k++)
                    {
                        nabla_w[i].setQuick(j, k, nabla_w[i].getQuick(j, k)*learnAmt);
                    }
                }
                
                for(int j = 0; j < nabla_b[i].size(); j++)
                {
                    nabla_b[i].setQuick(j, nabla_b[i].getQuick(j)*learnAmt);
                }
                
                weights[i].assign(nabla_w[i], Functions.minus);
                biases[i].assign(nabla_b[i], Functions.minus);
            }
        }
        
    }
    
    /*
     * Will return a tuple representing the gradient for cost functions
     * AKA nabla_b and nabla_w
     * X is set of pixel values and Y is set of answers
     */
    public DoubleMatrix2D[][] backProp(DoubleMatrix1D x, DoubleMatrix1D y)
    {
        //Create the arrays of bias and weight gradients; 1 entry per layer
        DoubleMatrix2D[] nabla_b = new DoubleMatrix2D[biases.length];
        DoubleMatrix2D[] nabla_w = new DoubleMatrix2D[weights.length];
        
        for(int i = 0; i < nabla_b.length; i++)
        {
            //Creates the gradient matrices for layer i
            nabla_b[i] = DoubleFactory2D.dense.make(biases[i].size(), 1);
            nabla_w[i] = DoubleFactory2D.dense.make(weights[i].rows(), weights[i].columns());
        }
        
        DoubleMatrix1D[] activations = new DoubleMatrix1D[biases.length+1];
        //array of z vectors for each layer
        DoubleMatrix1D[] zs = new DoubleMatrix1D[biases.length];
        //sets the first layer of activations to the pixel values
        activations[0] = x;
        DoubleMatrix1D activation = x;
        DoubleMatrix1D z;
        
        //feed forward
        for(int i = 0; i < biases.length; i++)
        {
            //calculate w*a+b for layer i
            z = weights[i].zMult(activation, null)
                    .assign(biases[i], Functions.plus);
            //add result to array of z vectors
            zs[i] = z;
            //set new activation for layer i to sigmoid(z)
            activation = z.assign(Sigmoid.sigmoid);
            activations[i+1] = activation;
        }
        
        //pass backwards
        //find the cost derivative of the activations and the expected results
        DoubleMatrix1D costDeriv = cost_derivative(activations[activations.length-1], y);
        //delta = cost derivative * sigmoid prime of the last z vector
        DoubleMatrix1D delta = costDeriv.assign(zs[zs.length-1].assign(Sigmoid.sigPrime), Functions.mult);
        
        Algebra myAlg = new Algebra();//Used for transposing
        
        //The factory calls basically just turn delta into a 2D matrix
        //set last set of biases to that delta
        nabla_b[nabla_b.length-1] = DoubleFactory2D.dense.make(delta.toArray(), delta.size());
        //set last set of weights to 
        DoubleMatrix2D act2D = DoubleFactory2D.dense.make(activations[activations.length-2].toArray(),
                activations[activations.length-2].size());
        act2D = myAlg.transpose(act2D);
        nabla_w[nabla_w.length-1] = DoubleFactory2D.dense.make(delta.toArray(), delta.size()).zMult(act2D, null);
        
        for(int i = 2; i < numLayers; i++)
        {
            z = zs[zs.length-i];
            DoubleMatrix1D sp = z.assign(Sigmoid.sigPrime);
            
            //The factory calls basically just turn the 1D matrices to 2Ds
            delta = myAlg.transpose(weights[weights.length-i+1]).zMult(DoubleFactory2D.dense.make(delta.toArray(), delta.size()), null)
                    .assign(DoubleFactory2D.dense.make(sp.toArray(), sp.size()), Functions.mult).viewColumn(0);
            
            act2D = DoubleFactory2D.dense.make(activations[activations.length-i-1].toArray(),
                    activations[activations.length-i-1].size());
            act2D = myAlg.transpose(act2D);//transpose for use in nabla_w
            
            nabla_b[nabla_b.length-i] = DoubleFactory2D.dense.make(delta.toArray(), delta.size());
            nabla_w[nabla_w.length-i] = DoubleFactory2D.dense.make(delta.toArray(), delta.size()).zMult(act2D, null);
        }
        
        DoubleMatrix2D[][] nablas = new DoubleMatrix2D[2][nabla_b.length];
        nablas[0] = nabla_b;
        nablas[1] = nabla_w;
        
        return nablas;
    }
    
    public int evaluate(TestEntry[] test_data)
    {
        int eval_sum = 0;
        
        for(int i = 0; i < test_data.length; i++)
        {
            //Get the highest output value (which number it thinks the img is)
            DoubleMatrix1D feed_results = feedforward(test_data[i].getImg());
            int max = 0;
            for(int f = 1; f < feed_results.size(); f++)
            {
                if(feed_results.getQuick(f) > feed_results.getQuick(max))
                    max = f;
            }
            
            eval_sum += max == test_data[i].getNum() ? 1 : 0;
        }
        
        return eval_sum;
    }
    
    public DoubleMatrix1D cost_derivative(DoubleMatrix1D output_activations, DoubleMatrix1D y)
    {
        //A really stupid way of making a copy of output_activations

        DoubleMatrix1D[] tempthing = new DoubleMatrix1D[1];
        tempthing[0] = output_activations;
        DoubleMatrix1D tempAct = DoubleFactory1D.dense.make(tempthing);
        //Return output_activations - y
        return tempAct.assign(y, Functions.minus);
    }
}
