package movieML;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

/*
 * The full u data set, 100000 ratings by 943 users on 1682 items.
 * Each user has rated at least 20 movies.  Users and items are
 * numbered consecutively from 1.  The data is randomly
 * ordered. This is a tab separated list of 
 * user id | item id | rating | timestamp. 
 * The time stamps are unix seconds since 1/1/1970 UTC   	          
 */

public class Project_6854 {
	double ratings[][];
	int numUsers=0,numMovies=0;
	static final int repeats = 3000;
		
	public static void main (String[] args) throws FileNotFoundException, UnsupportedEncodingException{
		
		System.out.println("- - program started - -");
		long startTime = System.currentTimeMillis();
		JLDimTest();
		long endTime = System.currentTimeMillis();
		System.out.println("program finished in "+(endTime - startTime)+" ms");
	}
	
	public static void JLLookingAtRho() throws FileNotFoundException, UnsupportedEncodingException{
		
		int exportSize = 480;
		
		String dataFile = "data\\Movie100K.data";
		

		String outputFileName = "JLRhoValues"+System.currentTimeMillis()+".csv";
		PrintWriter outputFile = new PrintWriter(outputFileName, "UTF-8");
		double[][] data = loadData(dataFile);

		final int trainingSize = 1000;
		final int testSize = data[0].length - trainingSize;
		final int numUsers = data.length;
				
		double[][] trainingData = selectRows(data,0,trainingSize);
				
		double[][] scaledTrainingData = scale(trainingData);
		
		//int[] dimTest = new int[]{1,2,4,10,20,50,100,200,400,800};
		
		//for(int iter = 0; iter<dimTest.length; iter++){
			//int dim = dimTest[iter];
			int[] count = new int[exportSize];
			
			for(int rep = 0;rep < 1; rep++){
				//double[][] JLData = JLTransform(scaledTrainingData,dim);
		
				//double[][] JLcovariance = covariance(JLData);
				double[][] covariance = covariance(scaledTrainingData);
						
				double stdev = 0.005;	
							
				for(int userA=0; userA<numUsers; userA++){
					for(int userB=userA+1; userB<numUsers; userB++){
						
						//int bin = (int)((exportSize/2)+(JLcovariance[userA][userB]-covariance[userA][userB])/stdev);
						int bin = (int)((exportSize/2)+(covariance[userA][userB])/stdev);
						
						if((bin>=0)&&(bin<exportSize)){
							count[bin]++;
						}
					}
				}
			//}
			
			//outputFile.write(dim+",");
			for(int i=0;i<exportSize;i++){
				outputFile.write(count[i]+",");
			}
			outputFile.write("\n");
			outputFile.flush();
		}
	}
	
	public static void JLDimTest () throws FileNotFoundException, UnsupportedEncodingException{		

		String dataFile = "data\\Movie100K.data";
		
		String outputFileStem = "logging\\JLDimTest"+System.currentTimeMillis();
		
		int NUM_CONFIGS = 4;
		
		PrintWriter[] dataWriters = new PrintWriter[NUM_CONFIGS];
		PrintWriter[] timeWriters = new PrintWriter[NUM_CONFIGS];
		

		for(int i=0;i<NUM_CONFIGS;i++){
			dataWriters[i] = new PrintWriter(outputFileStem+"_data_"+i+".csv", "UTF-8");
			timeWriters[i] = new PrintWriter(outputFileStem+"_time_"+i+".csv", "UTF-8");			
		}

		
		double[][] data = loadData(dataFile);

		final int trainingSize = 1000;
		final int testSize = data[0].length - trainingSize;
		final int numUsers = data.length;
				
		double[][] trainingData = selectRows(data,0,trainingSize);
		
		double[][] testData = selectRows(data,trainingSize,testSize+trainingSize);
		
		double[][] scaledTrainingData = scale(trainingData);
		
		
		int[] dimForTest = new int[]{
			1,2,3,4,5,6,8,10,12,15,18,22,26,31,37,44,
			52,62,74,88,104,123,146,173,205,242,286,
			338,399,471,556,657,776,916,1081
		};
		
		/*
		int[] dimForTest = new int[]{1,2,3,4,5,6,8,10,12,15,18,22,26,31,37,44};
		 */
		for (int rep = 0; rep < repeats; rep++) {
			System.out.println();
			System.out.print("on repeat "+rep+" :");
			for (int iter = 0; iter < dimForTest.length; iter++) {
				for (int config = 0; config < NUM_CONFIGS; config++) {

					int dim = dimForTest[iter];
					
					if(config==0){
						System.out.print(" "+dim);
					}					

					long startTime = System.currentTimeMillis();
					
					double[][] JLData = null;
					
					switch(config){
						case 0:{JLData = JLTransform(scaledTrainingData,dim);break;}
						case 1:{JLData = JLSparseTransform(scaledTrainingData,dim,1);break;}
						case 2:{JLData = JLSparseTransform(scaledTrainingData,dim,5);break;}
						case 3:{JLData = JLSparseTransform(scaledTrainingData,dim,20);break;}
					}

					double[][] covariance = covariance(scaledTrainingData);		
					
					long endTime = System.currentTimeMillis();
					
					double prediction[][] = new double[numUsers][testSize];
					
					//here I compute the prediction	
					for (int user=0; user < numUsers; user++){
						for (int movie=0; movie < testSize; movie++){	
							if(testData[user][movie]==0){continue;}
							double val = 3;				
							double con = 1;								
							for(int other=0;other<numUsers;other++){
								if(user==other){continue;}
								if(testData[other][movie]==0){continue;}
								if(covariance[user][other]>0.0){
									val += covariance[user][other] * testData[other][movie];
									con += Math.abs(covariance[user][other]);
								}
							}				
							if(con>0){
								val /= con;
								prediction[user][movie] = val;
							} else {
								prediction[user][movie] = 3.0;
							}
						}
					}
					double error = computeDifference(testData,prediction);
					double timeTaken = endTime - startTime;
					dataWriters[config].write(Double.toString(error)+",");
					timeWriters[config].write(Double.toString(timeTaken)+",");
				}
			}
			for(int config=0;config<NUM_CONFIGS;config++){
				dataWriters[config].write("\n");
				dataWriters[config].flush();
				timeWriters[config].write("\n");
				timeWriters[config].flush();
			}
		}
			
	}
	
	static double computeDifference(double[][] data1,double[][] data2){
		double totalErrorsSquared = 0;
		int numUsers = data1.length;
		int numMovies = data1[0].length;
		int errorTerms = 0;
		//now lets compute the error of these predictions
		for(int user = 0;user < numUsers; user++){
			for(int movie =0; movie < numMovies; movie++){
				if(Math.abs(data1[user][movie])<0.01){continue;}
				double delta = data1[user][movie] - data2[user][movie];
				totalErrorsSquared += delta*delta;
				errorTerms++;					
			}
		}
		return Math.sqrt(totalErrorsSquared/errorTerms);
	}

	static double[][] loadData(String dataPath) {
		try {
			FileReader in = new FileReader(dataPath);
			BufferedReader br = new BufferedReader(in);
			int numUsers=0, numMovies=0;
			String nextLine = br.readLine();
			while (nextLine != null) {
				
				
				String[] component = nextLine.split("\t");
				numUsers  = Math.max(numUsers , Integer.parseInt(component[0]) );
				numMovies = Math.max(numMovies, Integer.parseInt(component[1]) );
				nextLine = br.readLine();
				
				/*
				String[] component = nextLine.split(":");
				numUsers  = Math.max(numUsers , Integer.parseInt(component[0]) );
				numMovies = Math.max(numMovies, Integer.parseInt(component[2]) );
				nextLine = br.readLine();
				*/
			}
			br.close();
			double[][] matrix = new double[numUsers][numMovies];
			
			in = new FileReader(dataPath);
			br = new BufferedReader(in);
			nextLine = br.readLine();
			while (nextLine != null) {
				
				String[] component = nextLine.split("\t");

				int user  = Integer.parseInt(component[0])-1;
				int movie = Integer.parseInt(component[1])-1;
				int score = Integer.parseInt(component[2]);
				
				matrix[user][movie] = score;				
				nextLine = br.readLine();
				
				/*
				String[] component = nextLine.split(":");

				int user  = Integer.parseInt(component[0])-1;
				int movie = Integer.parseInt(component[2])-1;
				int score = Integer.parseInt(component[4]);
				
				matrix[user][movie] = score;				
				nextLine = br.readLine();
			*/
			}
			br.close();
			return matrix;
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.out.println("Error reading file");
			throw new RuntimeException("failed to load data");
		} 
	}
	
	static double[][] covariance(double[][] matrix){
				
		int numUsers = matrix.length;
		int numMovies = matrix[0].length;
		double[][] covariance = new double[numUsers][numUsers];
			
		for (int userA = 0; userA < numUsers; userA++) {
			for (int userB = userA; userB < numUsers; userB++) {
				for (int movie = 0; movie < numMovies; movie++) {
					covariance[userA][userB] += matrix[userA][movie] * matrix[userB][movie];
					if(userA != userB){
						covariance[userB][userA] += matrix[userA][movie] * matrix[userB][movie];
					}
				}
			}
		}		
		
		double[] variance = new double[numUsers];
		
		for (int userA = 0; userA < numUsers; userA++) {
			for (int movie = 0; movie < numMovies; movie++) {
				variance[userA] += matrix[userA][movie] * matrix[userA][movie];
			}
		}
				
		for (int userA = 0; userA < numUsers; userA++) {
			for (int userB = 0; userB < numUsers; userB++) {
				double scale = Math.sqrt(variance[userA] * variance[userB]);
					covariance[userA][userB] /= scale;					
			}
		}
		return covariance;
	}
	
	static double[][] scale (double[][] matrix){
		int numMovies = matrix[0].length;
		int numUsers = matrix.length;
		for(int user = 0;user < numUsers; user++){
			double sum = 0.0;
			int count = 0;
			for(int movie = 0;movie < numMovies ;movie++){
				if(matrix[user][movie]>0){
					count++;
				}
				sum += matrix[user][movie];
			}
			for(int movie = 0;movie < numMovies ;movie++){
				if(matrix[user][movie]>0){
					matrix[user][movie] -= sum/count;
				}
			}
		}
		//now they are zero mean - next we set them to be unit variance
		for(int user = 0;user < numUsers; user++){
			double sumSq = 0.0;
			for(int movie = 0; movie<numMovies; movie++){
				sumSq += matrix[user][movie]*matrix[user][movie];
			}
			double scale= 1/Math.sqrt(sumSq);
			for(int movie = 0; movie<numMovies; movie++){
				matrix[user][movie] *= scale;
			}
		}		
		return matrix;
	}
	
	static double[][] selectColumns (double[][] matrix, int start,int end){
		double [][] answer = new double[end-start][];
		for(int i=0;i<end-start;i++){
			answer[i] = matrix[i+start];
		}
		return answer;
	}
	
	static double[][] selectRows (double[][] matrix, int start,int end){
		double [][] answer = new double[matrix.length][];
		for(int i=0;i<matrix.length;i++){
			answer[i] = Arrays.copyOfRange(matrix[i], start, end);
		}
		return answer;
	}
	
	static double[][] JLTransform(double[][] matrix,int JLDimensions){
		int numUsers = matrix.length;
		int numMovies = matrix[0].length;
		double scale = 1/Math.sqrt(numMovies);
		Random rand = new Random(System.currentTimeMillis());
		double[][] JLMatrix = new double [numMovies][JLDimensions];
		for(int i=0;i<numMovies;i++){
			for(int j=0;j<JLDimensions;j++){
				JLMatrix[i][j] = rand.nextGaussian() * scale;
			}
		}
		
		double[][] JLData = new double[numUsers][JLDimensions];
		
		for(int user=0;user<numUsers;user++){
			for(int dim=0;dim<JLDimensions;dim++){
				for(int movie=0;movie<numMovies;movie++){
					JLData[user][dim]+=matrix[user][movie] * JLMatrix[movie][dim];
				}
			}
		}
		
		return JLData;
	}
	
	static double[][] JLSparseTransform(double[][] matrix,int JLDimensions,int s){

		if(matrix.length < s){
			throw new RuntimeException("matrix too small");
		}
		int numMovies = matrix[0].length;
		int numUsers = matrix.length;
				
		Random rand = new Random(System.currentTimeMillis());
		double[][] JLData = new double[matrix.length][JLDimensions];
		for(int dim=0;dim<JLDimensions;dim++){
			
			HashMap<Integer,Boolean> contributors = new HashMap<>();
			double scale = 1.0/Math.sqrt(s);
			
			int placed = 0;
			while(placed < s){
				int val = rand.nextInt(numMovies);
				if(!contributors.containsKey(val)){
					contributors.put(val,rand.nextBoolean());
					placed++;
				}
			}
			
			for(int user=0; user<numUsers; user++){
				for(Integer con : contributors.keySet()){
					if(contributors.get(con)){
						JLData[user][dim] += matrix[user][con] * scale;
					} else {
						JLData[user][dim] -= matrix[user][con] * scale;
					}
				}
			}			
		}
		return JLData;
	}
		
}
