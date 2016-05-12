package dataGenerators;

import java.util.Random;

import charlieJL.Matrix;

public class NormalSausageGenerator implements DataGenerator {

	int vecLen;
	Random rand;

	public NormalSausageGenerator(int vecLen) {
		this.vecLen = vecLen;
		rand = new Random(System.currentTimeMillis());
	}
	
	@Override
	public Matrix generatePoint() {
		double principleSize = rand.nextGaussian();
		Matrix answer = new Matrix(vecLen,1);
		for(int i=0;i<vecLen;i++){
			answer.data[i][0] = rand.nextGaussian()+principleSize;
		}
		return answer;
	}

	@Override
	public String getName() {
		return "normalSausageGenerator";
	}
	
	
}
