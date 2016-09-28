package semanticSimilaritySystems.supervisedMethod.regressors;

import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.Prediction;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;

/**
 * Created by gizem on 10.04.2016.
 */
public class LinearRegressionMethod {

    public static void main(String[] args) throws Exception {

        runLinearRegression();
    }


    public static void runLinearRegression() throws Exception {

        BufferedReader br = null;
        int numFolds = 10;
        br = new BufferedReader(new FileReader("rawData_biomedical.arff"));

        Instances trainData = new Instances(br);

        trainData.setClassIndex(trainData.numAttributes() - 1);
        br.close();

        Random rand = new Random(100000);   // create seeded number generator
        Instances randData = new Instances(trainData);   // create copy of original data
        randData.randomize(rand);         // randomize data with number generator

        randData.stratify(10);

        double accuracy = 0;
//        for(int i = 0; i < 10; i++) {
//            LinearRegression lr = new LinearRegression();
//            lr.buildClassifier(trainData);
//
//            Evaluation evaluation = new Evaluation(trainData);
//
//
//
//            /*******************CROSS VALIDATION*************************/
//            evaluation.crossValidateModel(lr, trainData, numFolds, new Random(1)));
//            /***********************************************************/
//
//
//            accuracy+=evaluation.correlationCoefficient();
//
//            /*******************Evaluation********************/
//            //   double[] prediction_results = evaluation.evaluateModel(lr,trainData);
//            /************************************************/
//
//            evaluateResults(evaluation);
//        }


        for (int n = 0; n < 10; n++) {
            Instances train = randData.trainCV(10, n);
            Instances test = randData.testCV(10, n);

            LinearRegression lr = new LinearRegression();
           // lr.setNumTrees(10);
            lr.buildClassifier(train);

            Evaluation evaluation = new Evaluation(train);
            double[] prediction_results = evaluation.evaluateModel(lr,test);

            evaluateResults(evaluation);
            accuracy+=evaluation.correlationCoefficient();

        }

        System.out.println("AVG ACCURACY: " + accuracy/10);
    }

    public static void evaluateResults(Evaluation evaluation){

        for(Prediction p: evaluation.predictions()){
            System.out.println(p.actual() + " " + p.predicted() );
        }


        System.out.println(evaluation.toSummaryString("\nResults\n======\n", true));
        //  System.out.println(evaluation.toSummaryString(evaluation.correlationCoefficient() + " " + evaluation.errorRate() + " " + evaluation.meanAbsoluteError() + " ");


    }

}
