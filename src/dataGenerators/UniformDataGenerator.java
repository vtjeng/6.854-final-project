package dataGenerators;

import charlieJL.Matrix;

public class UniformDataGenerator implements DataGenerator{

	int vecLen;	
	
	public UniformDataGenerator(int vecLen) {
		this.vecLen = vecLen;
	}
	
	@Override
	public Matrix generatePoint() {
		return Matrix.random(vecLen,1);
	}
	

	@Override
	public String getName() {
		return "uniformGenerator";
	}

}
