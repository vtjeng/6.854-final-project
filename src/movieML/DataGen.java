package movieML;

import java.util.*;
import java.util.function.DoubleUnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import javax.swing.event.ListSelectionEvent;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by vince_000 on 5/9/2016.
 */
public class DataGen {

    /**
     * Creates a list of lists which represents user preferences.
     *
     * @param sparsity fraction of matrix that is unrated
     *
     * @param numUsers number of users rating movies
     * @param numMovieGroups number of groups of movies that users can rate
     * @param moviesPerGroup movies per movie group that users can rate
     *
     * @implNote: We're specifying unrated movies as 0s here, and we make no special effort to make sure that movies we
     *   want to be rated are not given a rating of exactly 0.
     *
     * @return List (of length numUsers) of user ratings of movies, which are given as List (of length numMovies) of
     *   the ratings. User ratings are intended to range between 1-5, but there might be some spillover around the
     *   boundaries. Unrated movies
     */
	
		public static double[][] generateMatrixArray(long seed,double sparsity, double variance, int numUsers, int numMovieGroups, int moviesPerGroup){
			List<List<Double>> data = generateMatrix(seed,sparsity, variance, numUsers, numMovieGroups, moviesPerGroup);
			
			int numMovies = numMovieGroups * moviesPerGroup;
			
			double[][] answer = new double[numUsers][];
			
			ArrayList<Integer> moviePosition = new ArrayList<Integer>();
			for(int i=0;i<numMovies;i++){
				moviePosition.add(i);
			}
			Collections.shuffle(moviePosition,new Random(seed));
			
			for(int user=0;user<numUsers;user++){
				double[] userRatings = new double[numMovies];
				
				for(int movie=0; movie<numMovies; movie++ ){
					double t = (Double)data.get(user).get(movie);
					userRatings[moviePosition.get(movie)] = t;
				}
				answer[user] = userRatings;
			}
			return answer;
			
		}
	
    public static List<List<Double>> generateMatrix(long seed,double sparsity, double variance, int numUsers, int numMovieGroups, int moviesPerGroup){
        /**
         * TODO: If we have time, make the distribution of rated movies for each user follow the power-law distribution
         *   shown in this paper http://jmlr.csail.mit.edu/proceedings/papers/v18/dror12a/dror12a.pdf
         */

        Random rand = new Random(seed);

        Iterator<Double> userNicenesses =
                rand.doubles().boxed().map(i -> 2*i+2).iterator(); // scales base user niceness to the range [2, 4]; this is how the user shades his answers later.

        Iterator<Double> userVariances =
                DoubleStream.iterate(variance, DoubleUnaryOperator.identity()) // produce an infinite stream returning userVariance
                        .boxed().iterator();

        Stream<Stream<Double>> ratings = Collections.nCopies(numUsers, 0).stream()
                .map(i -> generateUserPreference(userNicenesses.next(), userVariances.next(), numMovieGroups, moviesPerGroup,rand));

        Stream<Stream<Double>> sparsifiedRatings = sparsify(ratings, sparsity,rand);

        return sparsifiedRatings.map(x -> x.collect(Collectors.toList())).collect(Collectors.toList());

    }

    public static Double wipe (Double rating, double sparsity, Random rand){

        if (rand.nextDouble() > sparsity) {
            return 0.0;
        } else {
            return rating;
        }
    }

    public static Stream<Stream<Double>> sparsify (Stream<Stream<Double>> ratings, double sparsity, Random rand){

    	Stream<Stream<Double>> sparsifiedRatings = ratings.map(xs -> xs.map(x -> wipe(x, sparsity, rand)));
        return sparsifiedRatings;
    }

    public static Stream<Double> generateUserPreference(double userNiceness, double userVariance, int numMovieGroups, int moviesPerGroup, Random rand){

        Stream<Double> groupMovieRatings =
                rand.doubles().boxed().limit(numMovieGroups)
                        .map(i -> userNiceness + 2*i-1); // scales base movie ratings to [userNiceness - 1, userNiceness + 1]

        Stream<Double> individualMovieRatings =
                groupMovieRatings.flatMap(i -> Collections.nCopies(moviesPerGroup, i).stream()); // replicates movie ratings for each movie in group

        Stream<Double> noisyIndividualMovieRatings =
                individualMovieRatings.map(rating -> rand.nextGaussian()*Math.sqrt(userVariance) + rating); // TODO: the variance is not a variance actually

        return noisyIndividualMovieRatings;

    }

}