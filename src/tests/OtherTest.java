package tests;

import charlieJL.Matrix;
import dataGenerators.DataGenerator;
import jl.JL;

public class OtherTest implements JLTest{
	
	@Override
	public void run(JL implementation, DataGenerator dataGenerator, int numPoints) {
		
		Matrix[] dataPoint = new Matrix[numPoints];		
		for(int i=0;i<numPoints;i++){
			dataPoint[i] = dataGenerator.generatePoint();
		}
		
		Matrix[] scaled = new Matrix[numPoints];		
		for(int i=0;i<numPoints;i++){
			scaled[i] = implementation.convert(dataPoint[i]);
		}
		
		double totalError = 0.0;
		int count = 0;		
		
		for(int i=0;i<numPoints;i++){
			for(int j=i+1;j<numPoints;j++){

				double normOrig = dataPoint[i].minus(dataPoint[j]).norm();
								
				double normScaled = scaled[i].minus(scaled[j]).norm();
				
				double error = Math.abs((normScaled-normOrig)/normOrig);
				totalError += error;
				count++;
				
			}
		}
		
		String testName = "test_"+dataGenerator.getName()+"_"+implementation.getName();
		String testInfo = implementation.originalDimension+" -> "+implementation.newDimension;
			
		double averageDeviation = totalError / ((double)count);
		
		//System.out.println(testName+": "+averageDeviation+" was the average deviation "+testInfo);
		System.out.println(implementation.newDimension+","+averageDeviation);
		
	}
}
