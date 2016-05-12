package tests;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import charlieJL.Matrix;
import dataGenerators.DataGenerator;
import jl.JL;

public class AllPairsDistrubutionTest implements JLTest {
	
	public static final int NUM_BINS = 40;
	
	@Override
	public void run(JL implementation,DataGenerator dataGenerator,int numPoints) {
		
		Matrix[] dataPoint = new Matrix[numPoints];		
		for(int i=0;i<numPoints;i++){
			dataPoint[i] = dataGenerator.generatePoint();
		}
		
		Matrix[] scaled = new Matrix[numPoints];		
		for(int i=0;i<numPoints;i++){
			scaled[i] = implementation.convert(dataPoint[i]);
		}
		
		double maxNorm = 0.0;
		
		for(Matrix m:dataPoint){
			maxNorm = Math.max(maxNorm,2.0*m.norm());
		}
		
		int[][] results = new int[NUM_BINS][(NUM_BINS*4)/3];
		
		for(int i=0;i<numPoints;i++){
			for(int j=i+1;j<numPoints;j++){

				double normOrig = dataPoint[i].minus(dataPoint[j]).norm();
				double normScaled = scaled[i].minus(scaled[j]).norm();
				
				int origBin = (int)((normOrig*NUM_BINS)/maxNorm);
				int scaledBin = (int)((normScaled*NUM_BINS)/maxNorm);
				
				results[origBin][scaledBin]++;
			}
		}
		
		String testName = "test_"+dataGenerator.getName()+"_"+implementation.getName();
		
		
		
		try {
			Output.outputCSV(results,testName+".csv");
		} catch (IOException e) {
			System.err.println("test failed to output data");
		}
		
		System.out.println(testName+" executed successfully");
	}


	
}
