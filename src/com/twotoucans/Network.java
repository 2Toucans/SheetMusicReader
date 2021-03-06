package com.twotoucans;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
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
    private boolean stopTraining;
    
    private Console console;
    
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
        }
    }

    public Network(InputStream is) throws IOException {
    	DataInputStream dis = new DataInputStream(is);
    	try {
	    	byte[] ntwk = new byte[4];
	    	dis.read(ntwk, 0, 4);
	    	if (!Arrays.equals(ntwk, "NTWK".getBytes(StandardCharsets.UTF_8))) {
	    		throw new IOException(".nwtk file was expected");
	    	}
	    	numLayers = dis.readInt();
	    	sizes = new int[numLayers];
	
	    	for (int i = 0; i < numLayers; i++) {
	    		sizes[i] = dis.readInt();
	    	}
	    	biases = new DoubleMatrix1D[numLayers - 1];
	    	weights = new DoubleMatrix2D[numLayers - 1];
	    	for (int i = 0; i < numLayers - 1; i++) {
	    		double[] b = new double[sizes[i+1]];
	    		double[][] w = new double[sizes[i+1]][];
				for (int j = 0; j < sizes[i+1]; j++) {
					b[j] = dis.readDouble();
					w[j] = new double[sizes[i]];
					for (int x = 0; x < sizes[i]; x++) {
						w[j][x] = dis.readDouble();
					}
				}
				biases[i] = DoubleFactory1D.dense.make(b);
				weights[i] = DoubleFactory2D.dense.make(w);
			}
    	}
    	catch (IOException e) {
    		throw e;
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
        for(int j = 0; (j < epochs || epochs <= 0) && !stopTraining; j++)
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
                print("Epoch " + j + ": " + evaluate(test_data) + " / " + n_test);
            else
                print("Epoch " + j + " complete");
        }
        stopTraining = false;
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
        activations[0] = x.copy();
        DoubleMatrix1D activation = x.copy();
        DoubleMatrix1D z;
        
        //feed forward
        for(int i = 0; i < biases.length; i++)
        {
            //calculate w*a+b for layer i
            z = weights[i].zMult(activation, null)
                    .assign(biases[i], Functions.plus);
            //add result to array of z vectors
            zs[i] = z.copy();
            //set new activation for layer i to sigmoid(z)
            activation = z.assign(Sigmoid.sigmoid);
            activations[i+1] = activation;
        }
        
        //pass backwards
        //find the cost derivative of the activations and the expected results
        DoubleMatrix1D costDeriv = cost_derivative(activations[activations.length-1], y);
        //delta = cost derivative * sigmoid prime of the last z vector
        DoubleMatrix1D delta = costDeriv.assign(zs[zs.length-1].copy().assign(Sigmoid.sigPrime), Functions.mult);
        
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
            z = zs[zs.length-i].copy();
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
            
            eval_sum += max - 10 == test_data[i].getNote().getPosition() ? 1 : 0;
        }
        
        return eval_sum;
    }
    
    public DoubleMatrix1D cost_derivative(DoubleMatrix1D output_activations, DoubleMatrix1D y)
    {
        //A really stupid way of making a copy of output_activations

        DoubleMatrix1D tempThing = output_activations.copy();
        //Return output_activations - y
        return tempThing.assign(y, Functions.minus);
    }
    
    public void write(OutputStream os) {
    	try {
    		DataOutputStream dos = new DataOutputStream(os);
    		dos.write("NTWK".getBytes(StandardCharsets.UTF_8)); // First word: characters for "NTWK"
    		dos.writeInt(numLayers);							   // Second word: num of layers
    		// Next [numLayers] words: Layer sizes
    		for (int i = 0; i < numLayers; i++) {
    			dos.writeInt(sizes[i]);
    		}
    		// Next chunks: Layer 1 ... layer [numLayers - 1]
    		// Chunk contains: Bias of node, Weights of node, for each node
    		for (int i = 0; i < numLayers - 1; i++) {
    			for (int j = 0; j < sizes[i+1]; j++) {
    				dos.writeDouble(biases[i].get(j));
    				for (int x = 0; x < sizes[i]; x++) {
    					dos.writeDouble(weights[i].get(j, x));
    				}
    			}
    		}
    	}
    	catch(Exception e) {
    		System.err.println("Error: " + e.getMessage());
    	}
    }
    public DoubleMatrix1D[] getBiases() {
		return biases;
    }
    public DoubleMatrix2D[] getWeights() {
    	return weights;
    }
    public String toString() {
    	String br =  System.lineSeparator();
    	String s = "";
    	for (int i = 1; i < numLayers; i++) {
    		s += "====Layer " + i + "====" + br;
    		for (int j = 0; j < sizes[i]; j++) {
    			s += "  --Node " + j + "--  " + br;
    			s += "Bias: " + biases[i - 1].get(j) + br;
    			for (int x = 0; x < sizes[i - 1]; x++) {
    				s += "" + (i - 1) + "[" + x + "] --> " + i + "[" + j + "]: " + weights[i - 1].get(j, x) + br; 
    			}
    			s += br;
    		}
    		s += br;
    	}
    	return s;
    }
    
    public void stop() {
    	stopTraining = true;
    }
    
    public int getLayerSize(int layer) {
    	return sizes[layer];
    }
    
    public int getNumLayers() {
    	return numLayers;
    }
    
    public void setConsole(Console c) {
    	console = c;
    }
    
    public <T> void print(T stuffToPrint) {
    	if (console != null) {
    		console.println(stuffToPrint);
    	}
    	else {
    		System.out.println(stuffToPrint);
    	}
    }
}
