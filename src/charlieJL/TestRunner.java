package charlieJL;

import dataGenerators.*;
import jl.VanillaJL;
import tests.AllPairsAverageTest;
import tests.JLTest;

public class TestRunner {
	
	public final static int NUM_POINTS = 1000;
	public final static int ORIGINAL_DIMENSION = 200;
	public final static int NEW_DIMENSION = 10;
		
	public static void main (String[] args){
		
		
		/*
		JL[] JLimplementations = new JL[]{
			new VanillaJL(ORIGINAL_DIMENSION,NEW_DIMENSION)
		};
		DataGenerator[] dataGenerator = new DataGenerator[]{
			new UniformDataGenerator(ORIGINAL_DIMENSION),
			new NormalSausageGenerator(ORIGINAL_DIMENSION)
		};
		JLTest[] JLTests = new JLTest[]{
			//new AllPairsDistrubutionTest()
			new AllPairsAverageTest()
		};
		
		for(JL reducer : JLimplementations){
			for(DataGenerator dataGen : dataGenerator){
				for(JLTest test : JLTests){
					test.run(reducer,dataGen,NUM_POINTS);
				}
			}
		}
		*/
		
		JLTest test= new AllPairsAverageTest();
		for(int targetDimension = 1;targetDimension < 3000;targetDimension = 1+(targetDimension*6)/5){
			test.run(
					new VanillaJL(ORIGINAL_DIMENSION, targetDimension),
					new SparseVectorGenerator(0.8,ORIGINAL_DIMENSION),
					400);
		}
	}
	
}
