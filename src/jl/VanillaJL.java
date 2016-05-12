package jl;

import java.util.Random;

import charlieJL.Matrix;

public class VanillaJL extends JL{

	Matrix reducer;
	
	public VanillaJL (int originalDimension,int newDimension){
		this.originalDimension = originalDimension;
		this.newDimension = newDimension;
		
		reducer = new Matrix (newDimension,originalDimension);		
		Random rand = new Random(System.currentTimeMillis());		
		double scale = 1.0/Math.sqrt((double)newDimension);		
		for(int i=0;i<newDimension;i++){
			for(int j=0;j<originalDimension;j++){
				reducer.data[i][j] = rand.nextGaussian()*scale;
			}			
		}
	}

	@Override
	public Matrix convert(Matrix input) {
		if(input.N!=1){
			throw new RuntimeException("attempt to convert non-vector - seen dimensions were "+input.M+" x "+input.N);
		}
		if(input.M!=originalDimension){
			throw new RuntimeException("attempt to convert vector of wrong dimension");
		}		
				
		return reducer.times(input);
	}

	@Override
	public String getName() {
		return "vanillaJL";
	}
	


}
