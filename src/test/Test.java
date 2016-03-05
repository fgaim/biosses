package test;

import com.google.common.io.Resources;
import edu.ucla.sspace.similarity.PearsonCorrelation;
import edu.ucla.sspace.vector.DenseVector;
import edu.ucla.sspace.vector.DoubleVector;
import slib.utils.ex.SLIB_Exception;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by orhan on 12.02.2016.
 */
public class Test {

    public static double calculateCorrelation(final DoubleVector xArray, final DoubleVector yArray) throws IllegalArgumentException {
        PearsonCorrelation pearsonCorr = new PearsonCorrelation();
        double result = pearsonCorr.sim(xArray,yArray);

        return result;
    }
    public static DoubleVector readCorrelationFiles(String filePath) throws IOException {
        BufferedReader buffer1 = new BufferedReader(new FileReader(new File(Resources.getResource(filePath).getFile())));
        DoubleVector xArr = new DenseVector(100);

        String line;
        int i = 0;
        while ((line=buffer1.readLine())!=null){
            xArr.add(i, Double.valueOf(line));
            i++;
        }
        return xArr;
    }

    public static void evaluateBaselineResults(DoubleVector groundTruthMean) throws IOException {
        DoubleVector cosine = readCorrelationFiles("correlationResult/baselineResults/cosineResult.txt");
        DoubleVector jaccard = readCorrelationFiles("correlationResult/baselineResults/jaccardResult.txt");
        DoubleVector blockDistance = readCorrelationFiles("correlationResult/baselineResults/blockDistanceResult.txt");
        DoubleVector levenshtein = readCorrelationFiles("correlationResult/baselineResults/levenshteinResult.txt");
        DoubleVector longestCommonSubstring = readCorrelationFiles("correlationResult/baselineResults/longestCommonSubstringResult.txt");
        DoubleVector longestCommonSubsequence = readCorrelationFiles("correlationResult/baselineResults/longestCommonSubsequenceResult.txt");
        DoubleVector needLemanWunch = readCorrelationFiles("correlationResult/baselineResults/needLemanWunchResult.txt");
        DoubleVector jaroWinkler = readCorrelationFiles("correlationResult/baselineResults/jaroWinkler.txt");
        DoubleVector overlapCoefficient = readCorrelationFiles("correlationResult/baselineResults/overlapCoefficientResult.txt");
        DoubleVector smithWaterman = readCorrelationFiles("correlationResult/baselineResults/smithWatermanResult.txt");
        DoubleVector qGrams = readCorrelationFiles("correlationResult/baselineResults/qGramsDistanceResult.txt");
        DoubleVector simonWhite = readCorrelationFiles("correlationResult/baselineResults/simonWhite.txt");
        DoubleVector mongeElkan = readCorrelationFiles("correlationResult/baselineResults/mongeElkan.txt");


        double correlation = calculateCorrelation(cosine, groundTruthMean);
        System.out.println("Correlation result between BASELINE-COSINE SIMILARIY and GROUNDTRUTH is: " + correlation);

        correlation = calculateCorrelation(jaccard, groundTruthMean);
        System.out.println("Correlation result between BASELINE-JACCARD and GROUNDTRUTH is: " + correlation);

        correlation = calculateCorrelation(blockDistance, groundTruthMean);
        System.out.println("Correlation result between BASELINE-BLOCK DISTANCE and GROUNDTRUTH is: " + correlation);

        correlation = calculateCorrelation(levenshtein, groundTruthMean);
        System.out.println("Correlation result between BASELINE-LEVENSHTEIN and GROUNDTRUTH is: " + correlation);

        correlation = calculateCorrelation(longestCommonSubstring, groundTruthMean);
        System.out.println("Correlation result between BASELINE-LONGEST COMMON SUBSTRING and GROUNDTRUTH is: " + correlation);

        correlation = calculateCorrelation(longestCommonSubsequence, groundTruthMean);
        System.out.println("Correlation result between BASELINE-LONGEST COMMON SUBSEQUENCE and GROUNDTRUTH is: " + correlation);

        correlation = calculateCorrelation(needLemanWunch, groundTruthMean);
        System.out.println("Correlation result between BASELINE-NEEDLEMANWUNCH and GROUNDTRUTH is: " + correlation);

        correlation = calculateCorrelation(jaroWinkler, groundTruthMean);
        System.out.println("Correlation result between BASELINE-JAROWINKLER and GROUNDTRUTH is: " + correlation);

        correlation = calculateCorrelation(overlapCoefficient, groundTruthMean);
        System.out.println("Correlation result between BASELINE-OVERLAPCOEFFICIENT and GROUNDTRUTH is: " + correlation);

        correlation = calculateCorrelation(smithWaterman, groundTruthMean);
        System.out.println("Correlation result between BASELINE-SMITHWATERMAN and GROUNDTRUTH is: " + correlation);

        correlation = calculateCorrelation(qGrams, groundTruthMean);
        System.out.println("Correlation result between BASELINE-QGRAMS DISTANCE and GROUNDTRUTH is: " + correlation);

        correlation = calculateCorrelation(simonWhite, groundTruthMean);
        System.out.println("Correlation result between BASELINE-SIMON-WHITE DISTANCE and GROUNDTRUTH is: " + correlation);

        correlation = calculateCorrelation(mongeElkan, groundTruthMean);
        System.out.println("Correlation result between BASELINE-MONGEELKAN DISTANCE and GROUNDTRUTH is: " + correlation);
        System.out.println("\n************************************************************************\n");


    }

    public static void evaluateStateOfTheArtSystems(DoubleVector groundTruthMean) throws IOException {

        DoubleVector ADW = readCorrelationFiles("correlationResult/stateOfTheArtMethods/adw.txt");
        DoubleVector semilar = readCorrelationFiles("correlationResult/stateOfTheArtMethods/semilar.txt");
        double correlation = calculateCorrelation(ADW, groundTruthMean);
        System.out.println("Correlation result between STATE_OF_THE_ART-ADW and GROUNDTRUTH is: " + correlation);

        correlation = calculateCorrelation(semilar, groundTruthMean);
        System.out.println("Correlation result between STATE_OF_THE_ART-SEMILAR and GROUNDTRUTH is: " + correlation);
        System.out.println("\n************************************************************************\n");

    }

    public static void evaluateEachAnnotatorCorrelations(DoubleVector groundTruthMean) throws IOException {

        DoubleVector annotatorA = readCorrelationFiles("correlationResult/groundTruth/annotatorA.txt");
        DoubleVector annotatorB = readCorrelationFiles("correlationResult/groundTruth/annotatorB.txt");
        DoubleVector annotatorC = readCorrelationFiles("correlationResult/groundTruth/annotatorC.txt");
        DoubleVector annotatorD = readCorrelationFiles("correlationResult/groundTruth/annotatorD.txt");
        DoubleVector annotatorE = readCorrelationFiles("correlationResult/groundTruth/annotatorE.txt");

        double correlation = calculateCorrelation(annotatorC, annotatorA);
        System.out.println("Correlation result between ANNOTATOR C and MEAN GROUNDTRUTH is: " + correlation);
        System.out.println("\n************************************************************************\n");

    }
    public static  void evaluateOurMethods(DoubleVector groundTruthMean) throws IOException {
        DoubleVector paragraphVector = readCorrelationFiles("correlationResult/ourResults/paragraphvector.txt");
        double correlation = calculateCorrelation(paragraphVector, groundTruthMean);
        System.out.println("Correlation result between OUR METHOD-(PARAGRAPH VECTOR) and GROUNDTRUTH is: " + correlation);

        DoubleVector lsa = readCorrelationFiles("correlationResult/ourResults/lsa.txt");
        correlation = calculateCorrelation(lsa, groundTruthMean);
        System.out.println("Correlation result between OUR METHOD-(LSA) and GROUNDTRUTH is: " + correlation);

        DoubleVector onlyWordnet = readCorrelationFiles("correlationResult/ourResults/a.txt");
        correlation = calculateCorrelation(onlyWordnet, groundTruthMean);
        System.out.println("Correlation result between OUR METHOD-(ONLY WORDNET) and GROUNDTRUTH is: " + correlation);

        System.out.println("\n************************************************************************\n");

    }

    public static void main(String[] args) throws SLIB_Exception, IOException {

        DoubleVector groundTruthMean = readCorrelationFiles("correlationResult/groundTruth/MEAN.txt");
        evaluateBaselineResults(groundTruthMean);
        // evaluateEachAnnotatorCorrelations(groundTruthMean);
        evaluateStateOfTheArtSystems(groundTruthMean);
        evaluateOurMethods(groundTruthMean);

    }
}