package services;

import semanticSimilaritySystems.baseline.SimMetricFunctions;
import semanticSimilaritySystems.core.FileOperations;
import semanticSimilaritySystems.core.SimilarityMeasure;
import slib.utils.ex.SLIB_Exception;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.io.IOException;
import java.util.List;

/**
 * Created by T082123 on 22.09.2016.
 */
@WebService
public class SSESService {

    List<String> stopWordsList;
    FileOperations fileOperations;

    public SSESService() throws IOException {
        fileOperations = new FileOperations();
        stopWordsList = fileOperations.readStopWordsList();
    }
    @WebMethod
    public double calculateSimilarityScoreForGivenPair(String s1, String s2, int methodType) throws SLIB_Exception, IOException {
        double similarityScore = 0;

        System.out.println("REQUEST geldi: " + s1 + " " + s2 + " " + methodType);

        switch (methodType){

            case 1:
                //combined ontology method
                break;
            case 2:
                //qgram
                SimilarityMeasure measure = new SimMetricFunctions();
                String preprocessedS1 = fileOperations.removeStopWordsFromSentence(s1, stopWordsList);
                String preprocessedS2 = fileOperations.removeStopWordsFromSentence(s2, stopWordsList);

                similarityScore = measure.getSimilarity(preprocessedS1, preprocessedS2);
                //System.out.println("Sentence 1: " + preprocessedS1);
                // System.out.println("Sentence 2: " + preprocessedS2);

                System.out.println(similarityScore);

                break;
            case 3:
                //paragraph vector
                break;
            case 4:
                //supervised
                break;
        }


        System.out.println("SCORE: " + similarityScore);
        return similarityScore;
    }
}
