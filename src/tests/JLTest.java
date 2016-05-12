package tests;

import dataGenerators.DataGenerator;
import jl.JL;

public interface JLTest {

	public void run(JL implementation,DataGenerator dataGenerator,int numPoints);
	
}
