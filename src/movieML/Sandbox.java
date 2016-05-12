package movieML;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


public class Sandbox {

    public static void main(String[] args){
        List<List<Double>> ratings = DataGen.generateMatrix(1234,0.5, 0.00, 10, 1, 5);

        System.out.println(roundDouble(ratings));
    }

    /**
     * Utility function that allows you to view the ratings generated
     * @param xss
     * @return Doubles rounded to two decimal places
     */
    
    
    
    public static List<List<String>> roundDouble (List<List<Double>> xss) {
        DecimalFormat numberFormat = new DecimalFormat("#.00");
        List<List<String>> ratingsRounded = xss.stream()
                .map(xs -> xs.stream().map(x -> numberFormat.format(x)).collect(Collectors.toList())).collect(Collectors.toList());
        return ratingsRounded;
    }

}