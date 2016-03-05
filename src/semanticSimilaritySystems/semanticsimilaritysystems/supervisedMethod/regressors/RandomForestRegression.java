package semanticSimilaritySystems.supervisedMethod.regressors;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.Prediction;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.Instances;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Created by orhan on 31.01.2016.
 */
public class RandomForestRegression {


    public RandomForestRegression(File trainingDocument, File testDocument) {
       // super(trainingDocument, testDocument);
    }

    public static void main(String[] args) throws Exception {

        runRandomForestRegression();
    }

    public static void runRandomForestRegression() throws Exception {

        BufferedReader br = null;
        int numFolds = 3;
        br = new BufferedReader(new FileReader("D:\\IntelligentChatBot\\ipec\\scripts\\trainingData.arff"));

        Instances trainData = new Instances(br);

        trainData.setClassIndex(trainData.numAttributes() - 1);
        br.close();
        RandomForest rf = new RandomForest();
        rf.setNumTrees(10);

        rf.buildClassifier(trainData);
        Evaluation evaluation = new Evaluation(trainData);


        /*******************CROSS VALIDATION*************************/
        //  evaluation.crossValidateModel(rf, trainData, numFolds, new Random(1));
        /***********************************************************/


        /*******************Evaluation********************/
        double[] prediction_results = evaluation.evaluateModel(rf,trainData);
        /************************************************/

        evaluateResults(evaluation);


    }

    public static void evaluateResults(Evaluation evaluation){

        for(Prediction p: evaluation.predictions()){
            System.out.println(p.actual() + " " + p.predicted() );
        }


        System.out.println(evaluation.toSummaryString("\nResults\n======\n", true));
        //  System.out.println(evaluation.toSummaryString(evaluation.correlationCoefficient() + " " + evaluation.errorRate() + " " + evaluation.meanAbsoluteError() + " ");


    }


    public static void randomForestCrossValidation() throws Exception {


        //  System.out.println(evaluation.predictions());
//        System.out.println(evaluation.toSummaryString("\nResults\n======\n", true));
//        System.out.println(evaluation.toClassDetailsString());
//        System.out.println("Results For Class -1- ");
//        System.out.println("Precision=  " + evaluation.precision(0));
//        System.out.println("Recall=  " + evaluation.recall(0));
//        System.out.println("F-measure=  " + evaluation.fMeasure(0));
//        System.out.println("Results For Class -2- ");
//        System.out.println("Precision=  " + evaluation.precision(1));
//        System.out.println("Recall=  " + evaluation.recall(1));
//        System.out.println("F-measure=  " + evaluation.fMeasure(1));

    }
}
