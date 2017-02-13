package semanticSimilaritySystems.supervisedMethod.regressors;

import com.google.common.io.Resources;
import org.simmetrics.StringMetric;
import org.simmetrics.metrics.StringMetrics;
import semanticSimilaritySystems.core.FileOperations;
import semanticSimilaritySystems.core.SimilarityMeasure;
import semanticSimilaritySystems.unsupervisedMethod.combinedOntologyMethod.CombinedOntologyMethod;
import semanticSimilaritySystems.unsupervisedMethod.paragraphVector.WordVectorConstructor;
import slib.utils.ex.SLIB_Exception;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.Prediction;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.trees.RandomForest;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * Created by gizem on 10.04.2016.
 */
public class LinearRegressionMethod implements SimilarityMeasure {

    public static void main(String[] args) throws Exception {
        //  runLinearRegression();
        LinearRegressionMethod linearRegressionMethod = new LinearRegressionMethod();
        linearRegressionMethod.getSimilarity("i love you", "i like you");
    }


    public static void runLinearRegression() throws Exception {

        BufferedReader br = null;
        int numFolds = 10;
        br = new BufferedReader(new FileReader(Resources.getResource("classifierModels/rawData_biomedical.arff").getFile()));

        Instances trainData = new Instances(br);
        trainData.setClassIndex(trainData.numAttributes() - 1);
        br.close();
        Random rand = new Random(100000);   // create seeded number generator
        Instances randData = new Instances(trainData);   // create copy of original data
        randData.randomize(rand);         // randomize data with number generator
        randData.stratify(10);
        double accuracy = 0;

        for (int n = 0; n < 10; n++) {
            Instances train = randData.trainCV(10, n);
            Instances test = randData.testCV(10, n);

            LinearRegression lr = new LinearRegression();
            // lr.setNumTrees(10);
            lr.buildClassifier(train);

            Evaluation evaluation = new Evaluation(train);
            double[] prediction_results = evaluation.evaluateModel(lr, test);

            evaluateResults(evaluation);
            accuracy += evaluation.correlationCoefficient();

        }

        System.out.println("AVG ACCURACY: " + accuracy / 10);
    }

    public static void evaluateResults(Evaluation evaluation) {

        for (Prediction p : evaluation.predictions()) {
            System.out.println(p.actual() + " " + p.predicted());
        }


        System.out.println(evaluation.toSummaryString("\nResults\n======\n", true));
        //  System.out.println(evaluation.toSummaryString(evaluation.correlationCoefficient() + " " + evaluation.errorRate() + " " + evaluation.meanAbsoluteError() + " ");

    }

    public double getSimilarity(String sentence1, String sentence2) {

        double score = 0;
        Instance testInstance = null;
        FileOperations fileOperations = new FileOperations();
        try {
            List<String> stopWordsList = fileOperations.readStopWordsList();

            CombinedOntologyMethod score_wordnet = new CombinedOntologyMethod(stopWordsList);
            double score_1 = score_wordnet.getSimilarityForWordnet(sentence1, sentence2);

            double score2 = score_wordnet.getSimilarityForUMLS(sentence1, sentence2);
            double similarityScoreOfCombined = (score2 + score_1) / 2;

            StringMetric metric = StringMetrics.qGramsDistance();
            double similarityScoreOfQgram = metric.compare(sentence1, sentence2);

            // WordVectorConstructor wordVectorConstructor = new WordVectorConstructor();
            double similarityScoreOfWordVec = 0;
            //wordVectorConstructor.getSimilarity(sentence1, sentence2);

            testInstance = new DenseInstance(3);
            testInstance.setValue(0, similarityScoreOfWordVec);
            testInstance.setValue(1, similarityScoreOfQgram);
            testInstance.setValue(2, similarityScoreOfCombined);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SLIB_Exception e) {
            e.printStackTrace();
        }

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(Resources.getResource("classifierModels/rawData_biomedical.arff").getFile()));
            Instances trainData = new Instances(br);
            trainData.setClassIndex(trainData.numAttributes() - 1);
            br.close();

            LinearRegression linearRegressionModel = new LinearRegression();
            linearRegressionModel.buildClassifier(trainData);
            if (testInstance != null) {
                testInstance.setDataset(trainData);
                score = linearRegressionModel.classifyInstance(testInstance);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return score;
    }
}