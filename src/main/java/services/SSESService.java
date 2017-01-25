package services;

import org.simmetrics.StringMetric;
import org.simmetrics.metrics.BlockDistance;
import org.simmetrics.metrics.StringMetrics;
import semanticSimilaritySystems.baseline.SimMetricFunctions;
import semanticSimilaritySystems.core.FileOperations;
import semanticSimilaritySystems.core.SimilarityMeasure;
import semanticSimilaritySystems.unsupervisedMethod.combinedOntologyMethod.CombinedOntologyMethod;
import semanticSimilaritySystems.unsupervisedMethod.combinedOntologyMethod.UmlsSimilarity;
import semanticSimilaritySystems.unsupervisedMethod.combinedOntologyMethod.WordNetSimilarity;
import similarityMeasures.JaccardSimilarity;
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

        String preprocessedS1 = fileOperations.removeStopWordsFromSentence(s1, stopWordsList);
        String preprocessedS2 = fileOperations.removeStopWordsFromSentence(s2, stopWordsList);


        String thesis_example_1 = "It has recently been shown that Craf is essential for Kras G12D-induced NSCLC.";
        String thesis_example_2 = "It has recently become evident that Craf is essential for the onset of Kras-driven non-small cell lung cancer.";
        switch (methodType){

            case 1:
                //WORDNET
                CombinedOntologyMethod measureOfWordNet = new CombinedOntologyMethod(stopWordsList);
                similarityScore = measureOfWordNet.getSimilarityForWordnet(s1, s2);
                System.out.println("SCORE: " + similarityScore);
                break;

            case 2:
                CombinedOntologyMethod measureOfUmls = new CombinedOntologyMethod(stopWordsList);
                similarityScore = measureOfUmls.getSimilarityForUMLS(s1, s2);
                System.out.println("SCORE:" + similarityScore);
                //UMLS
                break;
            case 3:
                CombinedOntologyMethod score_wordnet = new CombinedOntologyMethod(stopWordsList);
                double score_1 =score_wordnet.getSimilarityForWordnet(s1, s2);

                double score2 = score_wordnet.getSimilarityForUMLS(s1,s2);
                similarityScore = (score2+score_1)/2;
                System.out.println("SCORE: " + similarityScore);
                //COMBINED
                break;

            case 4:
                //qgram
                StringMetric metric = StringMetrics.qGramsDistance();
                similarityScore = metric.compare(preprocessedS1, preprocessedS2); //0.4767
                 //System.out.println("Sentence 1: " + preprocessedS1);
                // System.out.println("Sentence 2: " + preprocessedS2);

                System.out.println(similarityScore);

                break;

            case 5:
                //PARAGRAPH VEC
                break;

            case 6:
                //SUPERVISED!!!
                break;

        }


        System.out.println("SCORE: " + similarityScore);
        return similarityScore;
    }
}
