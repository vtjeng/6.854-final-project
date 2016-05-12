package tests;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class Output {

	static final String EOL = System.getProperty("line.separator");
	
	public static void outputCSV(Object[][] data,String filePath) throws IOException{
		Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "utf-8"));		
		
		for(Object[] dataLine : data){
			StringBuilder sb = new StringBuilder();
			for(int i=0;i<dataLine.length-1;i++){
				sb.append(dataLine[i].toString());
				sb.append(",");
			}
			sb.append(dataLine[dataLine.length-1]);
			sb.append(EOL);
			writer.write(sb.toString());
		}
		writer.close();		
	}
	
	public static void outputCSV(int[][] data,String filePath) throws IOException{
		Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "utf-8"));		
		
		for(int[] dataLine : data){
			StringBuilder sb = new StringBuilder();
			for(int i=0;i<dataLine.length-1;i++){
				sb.append(Integer.toString(dataLine[i]));
				sb.append(",");
			}
			sb.append(dataLine[dataLine.length-1]);
			sb.append(EOL);
			writer.write(sb.toString());
		}
		writer.close();		
	}
}
