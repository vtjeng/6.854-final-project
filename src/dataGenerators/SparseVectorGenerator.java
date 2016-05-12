package dataGenerators;

import java.util.Random;

import charlieJL.Matrix;

/*
 * this class will generate "movie preference data" according to the following model
 * 
 * they will be sparse vectors of -1,0,1 indicating if the person liked the movie
 * 
 */

public class SparseVectorGenerator implements DataGenerator{

	double density;
	int vecLen;
	Random rand;
	
	public SparseVectorGenerator(double density,int vecLen){
		this.density = density;
		this.vecLen = vecLen;
		rand = new Random(System.currentTimeMillis());
	}
	
	@Override
	public Matrix generatePoint() {
		Matrix answer = new Matrix(vecLen,1);
		for(int i=0;i<vecLen;i++){
			if(rand.nextDouble()<density){
				answer.data[i][0] = 1-rand.nextInt(1)*2;
			}
		}
		return answer;
	}

	@Override
	public String getName() {
		return "SparseVectorGenerator";
	}
	
	
	
}
