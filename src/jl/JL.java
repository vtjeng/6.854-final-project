package jl;

import charlieJL.Matrix;

public abstract class JL {

	public int originalDimension;
	public int newDimension;
	
	public abstract Matrix convert (Matrix input);
	
	public abstract String getName();
}
